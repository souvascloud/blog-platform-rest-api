package com.souvanik.blog.admin.service;

import com.souvanik.blog.admin.dto.AdminStatsResponse;
import com.souvanik.blog.admin.dto.UserAdminResponse;
import com.souvanik.blog.common.exception.ErrorCode;
import com.souvanik.blog.common.exception.ResourceNotFoundException;
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
 * https://opensource.org/licenses/MIT
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

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Page<UserAdminResponse> listUsers(Pageable pageable) {
        return userRepo.findAll(pageable).map(this::toUserAdmin);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void blockUser(UUID userId) {
        User u = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND, "User not found"));
        u.setStatus(UserStatus.BLOCKED);
        logger.info("Blocked user id={}", userId);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void unblockUser(UUID userId) {
        User u = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND, "User not found"));
        u.setStatus(UserStatus.ACTIVE);
        logger.info("Unblocked user id={}", userId);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void changePostStatus(UUID postId, PostStatus status) {
        Post p = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND, "Post not found"));
        p.setStatus(status);
        logger.info("Changed post id={} status={}", postId, status);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePost(UUID postId) {
        Post p = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND, "Post not found"));
        postRepo.delete(p);
        logger.warn("Admin deleted post id={}", postId);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteComment(UUID commentId) {
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND, "Comment not found"));
        commentRepo.delete(c);
        logger.warn("Admin deleted comment id={}", commentId);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public AdminStatsResponse stats() {
        return AdminStatsResponse.builder()
                .totalUsers(userRepo.count())
                .totalPosts(postRepo.count())
                .totalComments(commentRepo.count())
                .totalLikes(likeRepo.count())
                .build();
    }

    private UserAdminResponse toUserAdmin(User u) {
        return UserAdminResponse.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .role(u.getRole())
                .status(u.getStatus())
                .createdAt(u.getCreatedAt())
                .build();
    }
}