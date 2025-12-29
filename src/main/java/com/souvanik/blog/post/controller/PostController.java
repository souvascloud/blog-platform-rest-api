package com.souvanik.blog.post.controller;

import com.souvanik.blog.common.api.ApiResponse;
import com.souvanik.blog.common.api.PageMeta;
import com.souvanik.blog.post.dto.*;
import com.souvanik.blog.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Posts",
        description = "APIs for blog posts, comments, likes and tags"
)

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostService service;


    @Operation(
            summary = "Create a new post",
            description = """
        Creates a new blog post for the authenticated user.
        The post owner is derived from the JWT token.
        """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Post created successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = com.souvanik.blog.post.dto.PostResponse.class
                            ),
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = """
                {
                  "timestamp": "2025-01-01T11:00:00Z",
                  "status": 200,
                  "success": true,
                  "data": {
                    "id": "b1b2c3d4-1234-5678-9999-acde12345678",
                    "title": "Spring Boot REST API Best Practices",
                    "slug": "spring-boot-rest-api-best-practices",
                    "content": "In this post we explore REST API design...",
                    "status": "PUBLISHED",
                    "author": "souvanik",
                    "tags": ["spring", "java", "backend"],
                    "likes": 0,
                    "createdAt": "2025-01-01T10:59:30Z"
                  },
                  "error": null,
                  "meta": null
                }
                """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> create(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Post creation payload",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = com.souvanik.blog.post.dto.CreatePostRequest.class
                    ),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            value = """
                {
                  "title": "Spring Boot REST API Best Practices",
                  "content": "In this post we explore REST API design...",
                  "tags": ["spring", "java", "backend"],
                  "status": "PUBLISHED"
                }
                """
                    )
            )
    )@RequestBody @Valid CreatePostRequest req) {
        return ResponseEntity.ok(ApiResponse.success(200, service.create(req)));
    }




    @Operation(
            summary = "Get published post by slug",
            description = "Returns a published post identified by its unique slug."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Post fetched successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = com.souvanik.blog.post.dto.PostResponse.class
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<PostResponse>> get(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(200, service.getPublishedBySlug(slug)));
    }



    @Operation(
            summary = "List published posts",
            description = """
        Returns a paginated list of published posts.

        Pagination parameters:
        - page: Page number (0-based)
        - size: Number of items per page
        - sort: Sort field (default: createdAt,desc)
        """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Posts fetched successfully"
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> list(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable p) {
        Page<PostResponse> page = service.listPublished(p);
        PageMeta meta = PageMeta.builder()
                .page(p.getPageNumber()).size(p.getPageSize())
                .totalElements(page.getTotalElements()).totalPages(page.getTotalPages()).build();
        return ResponseEntity.ok(ApiResponse.success(200, page.getContent(), meta));
    }



    @Operation(
            summary = "Update post",
            description = """
        Updates an existing post.
        Only the post owner can update the post.
        """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Post updated successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = com.souvanik.blog.post.dto.PostResponse.class
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> update(@PathVariable UUID id,
                                                            @RequestBody @Valid UpdatePostRequest req) {
        return ResponseEntity.ok(ApiResponse.success(200, service.update(id, req)));
    }






    @Operation(
            summary = "Delete post",
            description = """
        Deletes an existing post.
        Only the post owner or an admin can delete the post.
        """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Post deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Post not found"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }






    @Operation(
            summary = "Add comment to post",
            description = "Adds a comment to the specified post by the authenticated user."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Comment added successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PostMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> comment(@PathVariable UUID id,
                                                                @RequestBody @Valid CreateCommentRequest req) {
        return ResponseEntity.ok(ApiResponse.success(200, service.addComment(id, req)));
    }





    @Operation(
            summary = "Get comments for a post",
            description = "Returns all comments associated with the specified post."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Comments fetched successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = com.souvanik.blog.post.dto.CommentResponse.class
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Post not found"
            )
    })
    @GetMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> comments(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(200, service.listComments(id)));
    }





    @Operation(
            summary = "Like a post",
            description = "Adds a like to the specified post by the authenticated user."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Post liked successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Void>> like(@PathVariable UUID id) {
        service.like(id);
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }





    @Operation(
            summary = "Remove like from post",
            description = "Removes the authenticated user's like from the specified post."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Like removed successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Post not found"
            )
    })
    @DeleteMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Void>> unlike(@PathVariable UUID id) {
        service.unlike(id);
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }
}