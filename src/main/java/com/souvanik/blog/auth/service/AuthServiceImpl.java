package com.souvanik.blog.auth.service;

import com.souvanik.blog.auth.dto.AuthResponse;
import com.souvanik.blog.auth.dto.LoginRequest;
import com.souvanik.blog.auth.dto.RefreshTokenRequest;
import com.souvanik.blog.auth.dto.RegisterRequest;
import com.souvanik.blog.auth.model.RefreshToken;
import com.souvanik.blog.auth.repository.RefreshTokenRepository;
import com.souvanik.blog.auth.security.JwtProperties;
import com.souvanik.blog.auth.security.service.JwtService;
import com.souvanik.blog.common.exception.ConflictException;
import com.souvanik.blog.common.exception.ErrorCode;
import com.souvanik.blog.common.exception.ForbiddenException;
import com.souvanik.blog.common.exception.UnauthorizedException;
import com.souvanik.blog.user.model.Role;
import com.souvanik.blog.user.model.User;
import com.souvanik.blog.user.model.UserStatus;
import com.souvanik.blog.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static com.souvanik.blog.auth.security.util.SecurityUtil.DUMMY_PASSWORD_HASH;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties props;

    public AuthServiceImpl(UserRepository userRepository,
                           RefreshTokenRepository refreshTokenRepository,
                           JwtService jwtService,
                           CookieService cookieService,
                           PasswordEncoder passwordEncoder,
                           JwtProperties props) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
        this.cookieService = cookieService;
        this.passwordEncoder = passwordEncoder;
        this.props = props;
    }

    @Override
    public AuthResponse register(RegisterRequest request , HttpServletResponse response) {
        logger.info("Registering new user email={}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException(ErrorCode.EMAIL_ALREADY_EXISTS, "Email already exists");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException(ErrorCode.USERNAME_ALREADY_EXISTS, "Username already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        return issueTokens(user ,response);
    }

    @Override
    public AuthResponse login(LoginRequest request , HttpServletResponse response) {
        logger.info("Login attempt email={}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        String hashToCheck = (user != null)
                ? user.getPasswordHash()
                : DUMMY_PASSWORD_HASH;

        if (!passwordEncoder.matches(request.getPassword(), hashToCheck)) {
            throw new UnauthorizedException(
                    ErrorCode.INVALID_CREDENTIALS, "Invalid email or password");
        }

        if (Objects.requireNonNull(user).getStatus() != UserStatus.ACTIVE) {
            throw new ForbiddenException(ErrorCode.USER_BLOCKED, "User is not active");
        }

        return issueTokens(user , response);
    }

    public AuthResponse refresh(HttpServletRequest request,
                                HttpServletResponse response) {

        logger.debug("Refreshing access token");

        String refreshToken = cookieService.extract(request);

        if (refreshToken == null) {
            throw new UnauthorizedException(
                    ErrorCode.TOKEN_INVALID,
                    "Refresh token missing");
        }

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new UnauthorizedException(
                        ErrorCode.TOKEN_INVALID,
                        "Invalid refresh token"));

        if (token.isRevoked()) {
            throw new UnauthorizedException(
                    ErrorCode.TOKEN_EXPIRED,
                    "Refresh token revoked");
        }

        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new UnauthorizedException(
                    ErrorCode.TOKEN_EXPIRED,
                    "Refresh token expired");
        }

        // Rotate token
        token.setRevoked(true);
        refreshTokenRepository.save(token);

        return issueTokens(token.getUser(), response);
    }


    public void logout(HttpServletRequest request,
                       HttpServletResponse response) {

        logger.info("Logout requested");

        String refreshToken = cookieService.extract(request);

        if (refreshToken != null) {
            refreshTokenRepository.findByToken(refreshToken)
                    .ifPresent(token -> {
                        token.setRevoked(true);
                        refreshTokenRepository.save(token);
                    });
        }

        cookieService.clearRefreshToken(response);

        logger.info("Logout completed");
    }



    @PreAuthorize(
            "isAuthenticated() and " +
                    "(#userId == principal.userId or hasRole('ADMIN'))"
    )
    @Override
    public void logoutAll(UUID userId) {
        logger.info("Logout all sessions for userId={}", userId);

        int revokedCount = refreshTokenRepository.revokeAllByUserId(userId);

        logger.debug("Revoked {} refresh tokens for userId={}", revokedCount, userId);
    }



    private AuthResponse issueTokens(User user,
                                     HttpServletResponse response) {

        String accessToken = jwtService.generateAccessToken(user);

        String refreshToken = jwtService.generateRefreshToken();
        Instant refreshExpiry = Instant.now()
                .plus(Duration.ofDays(props.getRefreshTokenExpiryDays()));

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .user(user)
                .token(refreshToken) // later we can hash
                .expiresAt(refreshExpiry)
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);

        cookieService.setRefreshToken(
                response,
                refreshToken,
                refreshExpiry
        );

        logger.info("Issued tokens for userId={}", user.getId());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(
                        Duration.ofMinutes(
                                props.getAccessTokenExpiryMinutes()
                        ).toSeconds()
                )
                .build();
    }
}