package com.souvanik.blog.common.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Schema(description = "Pagination metadata")
@Getter
@Builder
public class PageMeta {
    @Schema(example = "0")
    private int page;

    @Schema(example = "10")
    private int size;

    @Schema(example = "100")
    private long totalElements;

    @Schema(example = "10")
    private int totalPages;

}
