package com.souvanik.blog.admin.service;

import com.souvanik.blog.admin.dto.AdminStatsResponse;
import com.souvanik.blog.admin.dto.UserAdminResponse;
import com.souvanik.blog.post.model.PostStatus;
import com.souvanik.blog.user.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public interface AdminService {

    Page<UserAdminResponse> listUsers(Pageable pageable);

    void blockUser(UUID userId);

    void unblockUser(UUID userId);

    void changePostStatus(UUID postId, PostStatus status);

    void deletePost(UUID postId);

    void deleteComment(UUID commentId);

    AdminStatsResponse stats();

    void updateUserRole(UUID userId, Role role);
}