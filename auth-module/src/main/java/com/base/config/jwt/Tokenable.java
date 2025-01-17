package com.base.config.jwt;

public interface Tokenable {

    String identity();
    String role();
    String getUsername();
}
