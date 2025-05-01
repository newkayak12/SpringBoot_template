package com.base.config.restdoc;

import org.springframework.util.StringUtils;

public record Query(

    String name,
    Boolean optional,
    String description
) {


    public static Query of(String name) {
        return new Query(name, false, name);
    }

    public static Query of(String name, String description) {
        return new Query(name, false, StringUtils.hasText(description) ? description : name);
    }

    public static Query of(String name, Boolean optional, String description) {
        return new Query(name, optional, StringUtils.hasText(description) ? description : name);
    }

}
