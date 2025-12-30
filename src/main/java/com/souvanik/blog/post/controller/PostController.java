package com.souvanik.blog.post.controller;

import com.souvanik.blog.common.SwaggerExamples;
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
            description = "Creates a new blog post for the authenticated user.",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Post created successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.POST_CREATE_SUCCESS
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.UNAUTHORIZED
                            )
                    )
            )
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
            description = "Fetches a published post using its slug."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Post fetched successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.POST_FETCH_SUCCESS
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Post not found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.RESOURCE_NOT_FOUND
                            )
                    )
            )
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
                    description = "Posts fetched successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.POST_LIST_SUCCESS
                            )
                    )
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
            description = "Updates a post. Only the post owner can update it.",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Post updated successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.POST_UPDATE_SUCCESS
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.FORBIDDEN
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> update(@PathVariable UUID id,
                                                            @RequestBody @Valid UpdatePostRequest req) {
        return ResponseEntity.ok(ApiResponse.success(200, service.update(id, req)));
    }






    @io.swagger.v3.oas.annotations.Operation(
            summary = "Delete post",
            description = """
        Deletes an existing post.
        Only the post owner or an admin can delete the post.
        """,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Post deleted successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.POST_DELETE_SUCCESS
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.UNAUTHORIZED
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.FORBIDDEN
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Post not found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.RESOURCE_NOT_FOUND
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }






    @io.swagger.v3.oas.annotations.Operation(
            summary = "Add comment to post",
            description = "Adds a comment to the specified post by the authenticated user.",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Comment added successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.COMMENT_ADD_SUCCESS
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.UNAUTHORIZED
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Post not found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.RESOURCE_NOT_FOUND
                            )
                    )
            )
    })
    @PostMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> comment(
            @PathVariable UUID id,
            @RequestBody @Valid CreateCommentRequest req) {

        return ResponseEntity.ok(
                ApiResponse.success(200, service.addComment(id, req))
        );
    }





    @io.swagger.v3.oas.annotations.Operation(
            summary = "Get comments for a post",
            description = "Returns all comments associated with the specified post."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Comments fetched successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.COMMENT_LIST_SUCCESS
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Post not found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.RESOURCE_NOT_FOUND
                            )
                    )
            )
    })
    @GetMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> comments(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.success(200, service.listComments(id))
        );
    }





    @io.swagger.v3.oas.annotations.Operation(
            summary = "Like a post",
            description = "Adds a like to the specified post by the authenticated user.",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Post liked successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.POST_LIKE_SUCCESS
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.UNAUTHORIZED
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Post not found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.RESOURCE_NOT_FOUND
                            )
                    )
            )
    })
    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Void>> like(@PathVariable UUID id) {
        service.like(id);
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }





    @io.swagger.v3.oas.annotations.Operation(
            summary = "Remove like from post",
            description = "Removes the authenticated user's like from the specified post.",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Like removed successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.POST_UNLIKE_SUCCESS
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.UNAUTHORIZED
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Post not found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.RESOURCE_NOT_FOUND
                            )
                    )
            )
    })
    @DeleteMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Void>> unlike(@PathVariable UUID id) {
        service.unlike(id);
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }
}