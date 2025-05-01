package com.base.authenticate.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class LoginId {

    private final String userId;

    public LoginId() {
        this(null);
    }

    public LoginId(String userId) {
        this.userId = userId;
    }
}
