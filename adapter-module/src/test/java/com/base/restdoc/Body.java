package com.base.restdoc;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.util.StringUtils;

public record Body(String name, JsonFieldType jsonType, Boolean optional, String description) {


    public static Body of(String name, JsonFieldType jsonType) {
        return Body.of(name, jsonType, false);
    }

    public static Body of(String name, JsonFieldType jsonType, Boolean optional) {
        return Body.of(name, jsonType, optional, name);
    }

    public static Body of(String name, JsonFieldType jsonType, String description) {
        return Body.of(name, jsonType, false, StringUtils.hasText(description) ? description : name);
    }

    public static Body of(String name, JsonFieldType jsonType, Boolean optional, String description) {
        return new Body(name, jsonType, optional, StringUtils.hasText(description) ? description : name);
    }


}
