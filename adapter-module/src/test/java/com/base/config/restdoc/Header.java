package com.base.config.restdoc;

import org.springframework.util.StringUtils;

public record Header(String name, Boolean optional, String description) {


    public static Header of(String name, Boolean optional, String description) {
        return new Header(name, optional, StringUtils.hasText(description) ? description : name);
    }
}
