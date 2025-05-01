package com.base.config.security.jwt;

public interface Tokenable {

    String identity();

    String role();

    String getUsername();
}
