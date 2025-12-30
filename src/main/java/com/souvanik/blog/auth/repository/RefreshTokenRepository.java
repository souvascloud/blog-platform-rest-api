package com.souvanik.blog.auth.repository;

import com.souvanik.blog.auth.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUserId(UUID userId);

    @Modifying
    @Query("""
        UPDATE RefreshToken rt
        SET rt.revoked = true
        WHERE rt.user.id = :userId
          AND rt.revoked = false
    """)
    int revokeAllByUserId(@Param("userId") UUID userId);
}
