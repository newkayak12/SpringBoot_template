package com.base.config.restdoc;

import org.springframework.util.StringUtils;


public record PathParameter(String name, Boolean optional, String description) {


    public static PathParameter of(String name) {
        return new PathParameter(name, false, name);
    }

    public static PathParameter of(String name, String description) {
        return new PathParameter(name, false, StringUtils.hasText(description) ? description : name);
    }

    public static PathParameter of(String name, Boolean optional, String description) {
        return new PathParameter(name, optional, StringUtils.hasText(description) ? description : name);
    }

}
