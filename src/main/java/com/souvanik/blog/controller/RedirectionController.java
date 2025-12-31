package com.souvanik.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */

@Controller
public class RedirectionController {

    @GetMapping("/")
    public String redirect() {
        return "redirect:/swagger-ui.html";
    }
}
