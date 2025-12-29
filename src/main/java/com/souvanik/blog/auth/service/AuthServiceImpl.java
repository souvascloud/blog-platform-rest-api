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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

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
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties props;

    public AuthServiceImpl(UserRepository userRepository,
                           RefreshTokenRepository refreshTokenRepository,
                           JwtService jwtService,
                           PasswordEncoder passwordEncoder,
                           JwtProperties props) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.props = props;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        logger.info("Registering new user email={}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException(ErrorCode.EMAIL_ALREADY_EXISTS, "Email already exists");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException(ErrorCode.USERNAME_ALREADY_EXISTS, "Username already exists");
        }

        User user = User.builder()
                .id(UUID.randomUUID())
                .email(request.getEmail())
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        return issueTokens(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        logger.info("Login attempt email={}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException(
                        ErrorCode.INVALID_CREDENTIALS, "Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException(
                    ErrorCode.INVALID_CREDENTIALS, "Invalid email or password");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ForbiddenException(ErrorCode.USER_BLOCKED, "User is not active");
        }

        return issueTokens(user);
    }

    @Override
    public AuthResponse refresh(RefreshTokenRequest request) {
        logger.debug("Refreshing token");

        RefreshToken token = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedException(
                        ErrorCode.TOKEN_INVALID, "Invalid refresh token"));

        if (token.isRevoked() || token.getExpiresAt().isBefore(Instant.now())) {
            throw new UnauthorizedException(
                    ErrorCode.TOKEN_EXPIRED, "Refresh token expired or revoked");
        }

        return issueTokens(token.getUser());
    }

    @Override
    public void logout(String refreshToken) {
        logger.info("Logout using refresh token");

        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }

    private AuthResponse issueTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user);

        Instant refreshExpiry =
                Instant.now().plusSeconds(props.getRefreshTokenExpiryDays() * 24 * 3600);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiresAt(refreshExpiry)
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);

        logger.info("Issued tokens for userId={}", user.getId());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(props.getAccessTokenExpiryMinutes() * 60)
                .build();
    }
}