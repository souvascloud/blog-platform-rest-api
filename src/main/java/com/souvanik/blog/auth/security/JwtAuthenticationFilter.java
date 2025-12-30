package com.souvanik.blog.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.souvanik.blog.auth.security.service.JwtService;
import com.souvanik.blog.auth.security.util.CustomUserPrincipal;
import com.souvanik.blog.common.api.ApiError;
import com.souvanik.blog.common.api.ApiResponse;
import com.souvanik.blog.common.exception.ErrorCode;
import com.souvanik.blog.user.model.User;
import com.souvanik.blog.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();

        return path.startsWith("/api/v1/auth/")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/actuator");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            String email = jwtService.extractUsername(token);

            if (email != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                User user = userRepository.findByEmail(email).orElse(null);

                if (user != null && jwtService.isTokenValid(token, user)) {

                    List<GrantedAuthority> authorities = List.of(
                            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                    );

                    CustomUserPrincipal principal =
                            new CustomUserPrincipal(
                                    user.getId(),
                                    user.getEmail(),
                                    authorities
                            );

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    principal,
                                    null,
                                    authorities
                            );

                    auth.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            logger.debug("JWT expired: {}", ex.getMessage());
            handleJwtError(response,
                    ErrorCode.TOKEN_EXPIRED.name(),
                    "JWT token has expired");
            return;
        } catch (JwtException | IllegalArgumentException ex) {
            logger.debug("JWT invalid: {}", ex.getMessage());
            handleJwtError(response,
                    ErrorCode.TOKEN_INVALID.name(),
                    "Invalid JWT token");
            return;
        }
    }


    private void handleJwtError(HttpServletResponse response, String code, String message) throws IOException {
        if (response.isCommitted()) {
            return;
        }

        ApiError error = ApiError.builder()
                .code(code)
                .message(message)
                .build();

        ApiResponse<Void> body =
                ApiResponse.error(HttpServletResponse.SC_UNAUTHORIZED, error);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
