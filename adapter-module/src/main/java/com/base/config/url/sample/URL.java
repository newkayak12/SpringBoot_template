package com.base.config.url.sample;

public class URL {

    private URL() {
    }

    public static class SampleV1 {

        private SampleV1() {
        }

        public static final String FIND_ALL = "/api/v1/samples";
        public static final String CREATE = "/api/v1/samples";
        public static final String UPDATE = "/api/v1/samples/{id:(\\w|\\-)+}";


    }
}
