package com.base.restdoc;

import org.springframework.util.StringUtils;

public record Part(String name, Boolean optional, String description) {


    public static Part of(String name) {
        return new Part(name, false, name);
    }

    public static Part of(String name, String description) {
        return new Part(name, false, StringUtils.hasText(description) ? description : name);
    }

    public static Part of(String name, Boolean optional, String description) {
        return new Part(name, optional, StringUtils.hasText(description) ? description : name);
    }
}
