package com.base.config;

import lombok.Getter;

public class URL {

    public static class  Sample {
        public static final String FIND_ALL = "/v1/samples";
        public static final String CREATE = "/v1/samples";
        public static final String UPDATE = "/v1/samples/{id:\\d+}";


    }
}
