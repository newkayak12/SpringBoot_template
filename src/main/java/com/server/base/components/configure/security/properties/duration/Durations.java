package com.server.base.components.configure.security.properties.duration;

import lombok.Getter;

@Getter
public enum Durations {
    HOUR_1(1000L * 60L * 60L * 1L),
    HOUR_3(1000L * 60L * 60L * 3L),
    HOUR_6(1000L * 60L * 60L * 6L),
    HOUR_10(1000L * 60L * 60L * 10L),
    INFINITE(Long.MAX_VALUE);

    private Long time;
    Durations(Long time){
        this.time = time;
    }
}
