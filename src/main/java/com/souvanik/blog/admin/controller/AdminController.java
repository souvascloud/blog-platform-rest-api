package com.souvanik.blog.admin.controller;

import com.souvanik.blog.admin.dto.AdminStatsResponse;
import com.souvanik.blog.admin.dto.UpdateUserRoleRequest;
import com.souvanik.blog.admin.dto.UserAdminResponse;
import com.souvanik.blog.admin.service.AdminService;
import com.souvanik.blog.common.api.ApiResponse;
import com.souvanik.blog.common.api.PageMeta;
import com.souvanik.blog.post.model.PostStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.souvanik.blog.common.swagger.admin.AdminSwaggerExamples.*;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Tag(
        name = "Admin",
        description = "Administrative APIs for user and post moderation"
)
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;



    @Operation(
            summary = "List all users",
            description = "Returns a paginated list of all users in the system",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Users fetched successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = ADMIN_USERS_LIST_SUCCESS
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<?>> listUsers(
            @PageableDefault(size = 20) Pageable pageable) {

        Page<UserAdminResponse> page = adminService.listUsers(pageable);

        PageMeta meta = PageMeta.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return ResponseEntity.ok(ApiResponse.success(200, page.getContent(), meta));
    }



    @Operation(
            summary = "Block a user",
            description = "Blocks a user account and prevents authentication",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User blocked successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = ADMIN_USER_BLOCK_SUCCESS
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/users/{id}/block")
    public ResponseEntity<ApiResponse<Void>> blockUser(@PathVariable UUID id) {
        adminService.blockUser(id);
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }



    @Operation(
            summary = "Unblock a user",
            description = "Unblocks a previously blocked user",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User unblocked successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = ADMIN_USER_UNBLOCK_SUCCESS
                            )
                    )
            )
    })
    @PostMapping("/users/{id}/unblock")
    public ResponseEntity<ApiResponse<Void>> unblockUser(@PathVariable UUID id) {
        adminService.unblockUser(id);
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }



    @Operation(
            summary = "Change post status",
            description = "Updates post status (PUBLISHED / DRAFT / BLOCKED)",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Post status updated successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = ADMIN_POST_STATUS_UPDATE_SUCCESS
                            )
                    )
            )
    })
    @PostMapping("/posts/{id}/status")
    public ResponseEntity<ApiResponse<Void>> changePostStatus(
            @PathVariable UUID id,
            @RequestParam @NotNull PostStatus status) {

        adminService.changePostStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }



    @Operation(
            summary = "Delete any post",
            description = "Deletes a post regardless of ownership",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Post deleted successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = ADMIN_POST_DELETE_SUCCESS
                            )
                    )
            )
    })
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable UUID id) {
        adminService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }


    @Operation(
            summary = "Delete a comment",
            description = "Deletes any comment for moderation purposes",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Comment deleted successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = ADMIN_COMMENT_DELETE_SUCCESS
                            )
                    )
            )
    })
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable UUID id) {
        adminService.deleteComment(id);
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }



    @Operation(
            summary = "Get platform statistics",
            description = "Returns high-level platform statistics",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Statistics fetched successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = ADMIN_STATS_SUCCESS
                            )
                    )
            )
    })
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<AdminStatsResponse>> stats() {
        return ResponseEntity.ok(ApiResponse.success(200, adminService.stats()));
    }



    @Operation(
            summary = "Update user role",
            description = "Updates the role of a user. Only admins can change user roles.",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User role updated successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = ADMIN_USER_ROLE_UPDATE_SUCCESS
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid role"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse<Void>> updateUserRole(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserRoleRequest request) {

        adminService.updateUserRole(id, request.getRole());
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }
}
