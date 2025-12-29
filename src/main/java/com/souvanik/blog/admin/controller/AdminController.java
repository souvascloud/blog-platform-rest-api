package com.souvanik.blog.admin.controller;

import com.souvanik.blog.admin.dto.AdminStatsResponse;
import com.souvanik.blog.admin.dto.UserAdminResponse;
import com.souvanik.blog.admin.service.AdminService;
import com.souvanik.blog.common.api.ApiResponse;
import com.souvanik.blog.common.api.PageMeta;
import com.souvanik.blog.post.model.PostStatus;
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

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<?>> listUsers(@PageableDefault(size = 20) Pageable pageable) {
        Page<UserAdminResponse> page = adminService.listUsers(pageable);
        PageMeta meta = PageMeta.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
        return ResponseEntity.ok(ApiResponse.success(200, page.getContent(), meta));
    }

    @PostMapping("/users/{id}/block")
    public ResponseEntity<ApiResponse<Void>> block(@PathVariable UUID id) {
        adminService.blockUser(id);
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }

    @PostMapping("/users/{id}/unblock")
    public ResponseEntity<ApiResponse<Void>> unblock(@PathVariable UUID id) {
        adminService.unblockUser(id);
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }

    @PostMapping("/posts/{id}/status")
    public ResponseEntity<ApiResponse<Void>> changeStatus(
            @PathVariable UUID id,
            @RequestParam PostStatus status) {
        adminService.changePostStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable UUID id) {
        adminService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable UUID id) {
        adminService.deleteComment(id);
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<AdminStatsResponse>> stats() {
        return ResponseEntity.ok(ApiResponse.success(200, adminService.stats()));
    }
}
