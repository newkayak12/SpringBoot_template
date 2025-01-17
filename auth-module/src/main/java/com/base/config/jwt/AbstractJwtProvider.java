package com.base.config.jwt;


public interface AbstractJwtProvider <E extends Tokenable> {

    String tokenize(E tokenable);
    Boolean validate(String token);
    E decrypt(String token);
}
