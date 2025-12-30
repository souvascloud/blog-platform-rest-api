package com.souvanik.blog.admin.service.impl;

import com.souvanik.blog.admin.dto.AdminStatsResponse;
import com.souvanik.blog.admin.dto.UserAdminResponse;
import com.souvanik.blog.admin.service.AdminService;
import com.souvanik.blog.common.exception.ResourceNotFoundException;
import com.souvanik.blog.common.exception.ErrorCode;
import com.souvanik.blog.post.model.Comment;
import com.souvanik.blog.post.model.Post;
import com.souvanik.blog.post.model.PostStatus;
import com.souvanik.blog.post.repository.CommentRepository;
import com.souvanik.blog.post.repository.PostLikeRepository;
import com.souvanik.blog.post.repository.PostRepository;
import com.souvanik.blog.user.model.User;
import com.souvanik.blog.user.model.UserStatus;
import com.souvanik.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final UserRepository userRepo;
    private final PostRepository postRepo;
    private final CommentRepository commentRepo;
    private final PostLikeRepository likeRepo;

    // =========================================================
    // USERS
    // =========================================================

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Page<UserAdminResponse> listUsers(Pageable pageable) {
        logger.debug("Listing users page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        return userRepo.findAll(pageable).map(this::toUserAdmin);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void blockUser(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND, "User not found"));

        if (user.getStatus() == UserStatus.BLOCKED) {
            logger.debug("User id={} already blocked", userId);
            return;
        }

        user.setStatus(UserStatus.BLOCKED);
        logger.info("Blocked user id={}", userId);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void unblockUser(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND, "User not found"));

        if (user.getStatus() == UserStatus.ACTIVE) {
            logger.debug("User id={} already active", userId);
            return;
        }

        user.setStatus(UserStatus.ACTIVE);
        logger.info("Unblocked user id={}", userId);
    }

    // =========================================================
    // POSTS
    // =========================================================

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void changePostStatus(UUID postId, PostStatus status) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND, "Post not found"));

        post.setStatus(status);
        logger.info("Admin changed post id={} status={}", postId, status);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePost(UUID postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND, "Post not found"));

        postRepo.delete(post);
        logger.warn("Admin deleted post id={}", postId);
    }

    // =========================================================
    // COMMENTS
    // =========================================================

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteComment(UUID commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND, "Comment not found"));

        commentRepo.delete(comment);
        logger.warn("Admin deleted comment id={}", commentId);
    }

    // =========================================================
    // STATS
    // =========================================================

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public AdminStatsResponse stats() {
        logger.debug("Fetching admin platform statistics");

        return AdminStatsResponse.builder()
                .totalUsers(userRepo.count())
                .totalPosts(postRepo.count())
                .totalComments(commentRepo.count())
                .totalLikes(likeRepo.count())
                .build();
    }

    // =========================================================
    // MAPPERS
    // =========================================================

    private UserAdminResponse toUserAdmin(User user) {
        return UserAdminResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
