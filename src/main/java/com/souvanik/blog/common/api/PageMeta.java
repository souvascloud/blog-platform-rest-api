package com.souvanik.blog.common.api;

import lombok.Builder;
import lombok.Getter;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Getter
@Builder
public class PageMeta {
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;

}
