package com.souvanik.blog.post.controller;

import com.souvanik.blog.common.api.ApiResponse;
import com.souvanik.blog.common.api.PageMeta;
import com.souvanik.blog.post.dto.*;
import com.souvanik.blog.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostService service;

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> create(@RequestBody @Valid CreatePostRequest req) {
        return ResponseEntity.ok(ApiResponse.success(200, service.create(req)));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<PostResponse>> get(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(200, service.getPublishedBySlug(slug)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> list(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable p) {
        Page<PostResponse> page = service.listPublished(p);
        PageMeta meta = PageMeta.builder()
                .page(p.getPageNumber()).size(p.getPageSize())
                .totalElements(page.getTotalElements()).totalPages(page.getTotalPages()).build();
        return ResponseEntity.ok(ApiResponse.success(200, page.getContent(), meta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> update(@PathVariable UUID id,
                                                            @RequestBody @Valid UpdatePostRequest req) {
        return ResponseEntity.ok(ApiResponse.success(200, service.update(id, req)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> comment(@PathVariable UUID id,
                                                                @RequestBody @Valid CreateCommentRequest req) {
        return ResponseEntity.ok(ApiResponse.success(200, service.addComment(id, req)));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> comments(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(200, service.listComments(id)));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Void>> like(@PathVariable UUID id) {
        service.like(id);
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Void>> unlike(@PathVariable UUID id) {
        service.unlike(id);
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }
}