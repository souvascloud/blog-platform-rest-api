package com.souvanik.blog.common.util;

import java.text.Normalizer;
import java.util.Locale;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public final class SlugUtil {

    private SlugUtil() {}

    public static String toSlug(String input) {
        if (input == null) return null;

        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[^\\w\\s-]", "")
                .trim()
                .replaceAll("[\\s_-]+", "-")
                .toLowerCase(Locale.ENGLISH);
    }
}
