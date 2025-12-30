package com.souvanik.blog.post.service.impl;

import com.souvanik.blog.auth.security.util.SecurityUtil;
import com.souvanik.blog.common.exception.ConflictException;
import com.souvanik.blog.common.exception.ErrorCode;
import com.souvanik.blog.common.exception.ForbiddenException;
import com.souvanik.blog.common.exception.ResourceNotFoundException;
import com.souvanik.blog.common.util.SlugUtil;
import com.souvanik.blog.post.dto.*;
import com.souvanik.blog.post.model.*;
import com.souvanik.blog.post.repository.CommentRepository;
import com.souvanik.blog.post.repository.PostLikeRepository;
import com.souvanik.blog.post.repository.PostRepository;
import com.souvanik.blog.post.repository.TagRepository;
import com.souvanik.blog.post.service.PostService;
import com.souvanik.blog.user.model.User;
import com.souvanik.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private final PostRepository postRepo;
    private final TagRepository tagRepo;
    private final CommentRepository commentRepo;
    private final PostLikeRepository likeRepo;
    private final UserRepository userRepo;

    @PreAuthorize("isAuthenticated()")
    @Override
    public PostResponse create(CreatePostRequest req) {
        User me = getCurrentUser();

        String baseSlug = SlugUtil.toSlug(req.getTitle());
        String slug = generateUniqueSlug(baseSlug);

        Post post = new Post();
        post.setAuthor(me);
        post.setTitle(req.getTitle());
        post.setSlug(slug);
        post.setContent(req.getContent());
        post.setStatus(
                req.getStatus() != null ? req.getStatus() : PostStatus.DRAFT
        );
        post.setTags(resolveTags(req.getTags()));

        postRepo.save(post);

        logger.info("Created post slug={} by {}", slug, me.getEmail());
        return toResponse(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPublishedBySlug(String slug) {
        Post post = postRepo.findBySlugAndStatus(slug, PostStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Post not found"
                ));
        return toResponse(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> listPublished(Pageable pageable) {
        return postRepo
                .findByStatus(PostStatus.PUBLISHED, pageable)
                .map(this::toResponse);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public PostResponse update(UUID id, UpdatePostRequest req) {

        User me = getCurrentUser();

        Post post = postRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Post not found"
                ));

        assertOwner(post, me);

        if (req.getTitle() != null && !req.getTitle().equals(post.getTitle())) {

            post.setTitle(req.getTitle());

            // Slug changes ONLY for draft posts
            if (post.getStatus() == PostStatus.DRAFT) {
                post.setSlug(generateUniqueSlug(
                        SlugUtil.toSlug(req.getTitle())
                ));
            }
        }

        if (req.getContent() != null) {
            post.setContent(req.getContent());
        }


        if (req.getStatus() != null) {
            post.setStatus(req.getStatus());
        }

        if (req.getTags() != null) {
            post.setTags(resolveTags(req.getTags()));
        }

        logger.info("Post updated id={} by user={}", id, me.getEmail());

        return toResponse(post);
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public void delete(UUID id) {
        User me = getCurrentUser();

        Post post = postRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Post not found"
                ));

        assertOwner(post, me);

        postRepo.delete(post);
        logger.info("Deleted post id={} by {}", id, me.getEmail());
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public CommentResponse addComment(UUID postId, CreateCommentRequest req) {
        User me = getCurrentUser();

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Post not found"
                ));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(me);
        comment.setContent(req.getContent());
        comment.setStatus(CommentStatus.VISIBLE);

        commentRepo.save(comment);

        logger.info("Added comment to post={} by {}", postId, me.getEmail());

        return CommentResponse.builder()
                .id(comment.getId())
                .author(me.getUsername())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> listComments(UUID postId) {
        return commentRepo
                .findByPostIdAndStatus(postId, CommentStatus.VISIBLE)
                .stream()
                .map(c -> CommentResponse.builder()
                        .id(c.getId())
                        .author(c.getAuthor().getUsername())
                        .content(c.getContent())
                        .createdAt(c.getCreatedAt())
                        .build()
                )
                .toList();
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public void like(UUID postId) {
        User me = getCurrentUser();

        if (likeRepo.existsByPostIdAndUserId(postId, me.getId())) {
            throw new ConflictException(
                    ErrorCode.ALREADY_LIKED,
                    "Post already liked"
            );
        }

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Post not found"
                ));

        PostLike like = new PostLike();
        like.setPost(post);
        like.setUser(me);

        likeRepo.save(like);

        logger.info("Liked post={} by {}", postId, me.getEmail());
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public void unlike(UUID postId) {
        User me = getCurrentUser();

        if (!likeRepo.existsByPostIdAndUserId(postId, me.getId())) {
            throw new ConflictException(
                    ErrorCode.NOT_LIKED,
                    "Post not liked yet"
            );
        }

        likeRepo.deleteByPostIdAndUserId(postId, me.getId());

        logger.info("Unliked post={} by {}", postId, me.getEmail());
    }


    private User getCurrentUser() {
        String email = SecurityUtil.getCurrentUserEmail();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "User not found"
                ));
    }

    private void assertOwner(Post post, User user) {
        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new ForbiddenException(
                    ErrorCode.FORBIDDEN,
                    "Not owner of post"
            );
        }
    }

    private Set<Tag> resolveTags(Set<String> names) {
        if (names == null || names.isEmpty()) {
            return Set.of();
        }

        return names.stream()
                .map(name -> {
                    String normalized = name.toLowerCase();
                    return tagRepo.findByName(normalized)
                            .orElseGet(() -> {
                                Tag tag = new Tag();
                                tag.setName(normalized);
                                return tagRepo.save(tag);
                            });
                })
                .collect(Collectors.toSet());
    }

    private String generateUniqueSlug(String base) {
        String slug = base;
        int counter = 1;

        while (postRepo.existsBySlug(slug)) {
            slug = base + "-" + counter++;
        }
        return slug;
    }

    private PostResponse toResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .slug(post.getSlug())
                .content(post.getContent())
                .status(post.getStatus())
                .author(post.getAuthor().getUsername())
                .tags(
                        post.getTags()
                                .stream()
                                .map(Tag::getName)
                                .collect(Collectors.toSet())
                )
                .likes(likeRepo.countByPostId(post.getId()))
                .createdAt(post.getCreatedAt())
                .build();
    }

}