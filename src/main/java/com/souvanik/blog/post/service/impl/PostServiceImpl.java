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

    @Override
    public PostResponse create(CreatePostRequest req) {
        String email = SecurityUtil.getCurrentUsername();
        User me = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "User not found"));

        String baseSlug = SlugUtil.toSlug(req.getTitle());
        String slug = uniqueSlug(baseSlug);

        Post post = new Post();
        post.setAuthor(me);
        post.setTitle(req.getTitle());
        post.setSlug(slug);
        post.setContent(req.getContent());
        post.setStatus(req.getStatus() == null ? PostStatus.DRAFT : req.getStatus());
        post.setTags(resolveTags(req.getTags()));

        postRepo.save(post);
        logger.info("Created post slug={} by {}", slug, email);
        return toResponse(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPublishedBySlug(String slug) {
        Post post = postRepo.findBySlugAndStatus(slug, PostStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "Post not found"));
        return toResponse(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> listPublished(Pageable pageable) {
        return postRepo.findByStatus(PostStatus.PUBLISHED, pageable).map(this::toResponse);
    }

    @Override
    public PostResponse update(UUID id, UpdatePostRequest req) {
        String email = SecurityUtil.getCurrentUsername();
        Post post = postRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "Post not found"));
        if (!post.getAuthor().getEmail().equals(email)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN, "Not owner of post");
        }
        if (req.getTitle() != null) {
            post.setTitle(req.getTitle());
            post.setSlug(uniqueSlug(SlugUtil.toSlug(req.getTitle())));
        }
        if (req.getContent() != null) post.setContent(req.getContent());
        if (req.getStatus() != null) post.setStatus(req.getStatus());
        if (req.getTags() != null) post.setTags(resolveTags(req.getTags()));
        logger.info("Updated post id={} by {}", id, email);
        return toResponse(post);
    }

    @Override
    public void delete(UUID id) {
        String email = SecurityUtil.getCurrentUsername();
        Post post = postRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "Post not found"));
        if (!post.getAuthor().getEmail().equals(email)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN, "Not owner of post");
        }
        postRepo.delete(post);
        logger.info("Deleted post id={} by {}", id, email);
    }

    @Override
    public CommentResponse addComment(UUID postId, CreateCommentRequest req) {
        String email = SecurityUtil.getCurrentUsername();
        User me = userRepo.findByEmail(email).orElseThrow();
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "Post not found"));

        Comment c = new Comment();
        c.setPost(post);
        c.setAuthor(me);
        c.setContent(req.getContent());
        c.setStatus(CommentStatus.VISIBLE);
        commentRepo.save(c);
        logger.info("Added comment to post={} by {}", postId, email);
        return CommentResponse.builder()
                .id(c.getId()).author(me.getUsername()).content(c.getContent()).createdAt(c.getCreatedAt()).build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> listComments(UUID postId) {
        return commentRepo.findByPostIdAndStatus(postId, CommentStatus.VISIBLE)
                .stream().map(c -> CommentResponse.builder()
                        .id(c.getId()).author(c.getAuthor().getUsername())
                        .content(c.getContent()).createdAt(c.getCreatedAt()).build()).toList();
    }

    @Override
    public void like(UUID postId) {
        String email = SecurityUtil.getCurrentUsername();
        User me = userRepo.findByEmail(email).orElseThrow();
        if (likeRepo.existsByPostIdAndUserId(postId, me.getId())) {
            throw new ConflictException(ErrorCode.ALREADY_LIKED, "Already liked");
        }
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "Post not found"));
        PostLike l = new PostLike();
        l.setPost(post);
        l.setUser(me);
        likeRepo.save(l);
        logger.info("Liked post={} by {}", postId, email);
    }

    @Override
    public void unlike(UUID postId) {
        String email = SecurityUtil.getCurrentUsername();
        User me = userRepo.findByEmail(email).orElseThrow();
        likeRepo.deleteByPostIdAndUserId(postId, me.getId());
        logger.info("Unliked post={} by {}", postId, email);
    }

    // helpers
    private Set<Tag> resolveTags(Set<String> names) {
        if (names == null) return Set.of();
        return names.stream().map(n ->
                tagRepo.findByName(n.toLowerCase())
                        .orElseGet(() -> tagRepo.save(new Tag() {{
                            setName(n.toLowerCase());
                        }}))
        ).collect(Collectors.toSet());
    }

    private String uniqueSlug(String base) {
        String slug = base;
        int i = 1;
        while (postRepo.existsBySlug(slug)) slug = base + "-" + (i++);
        return slug;
    }

    private PostResponse toResponse(Post p) {
        return PostResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .slug(p.getSlug())
                .content(p.getContent())
                .status(p.getStatus())
                .author(p.getAuthor().getUsername())
                .tags(p.getTags().stream().map(Tag::getName).collect(Collectors.toSet()))
                .likes(likeRepo.countByPostId(p.getId()))
                .createdAt(p.getCreatedAt())
                .build();
    }
}