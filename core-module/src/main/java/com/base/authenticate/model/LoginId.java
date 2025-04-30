package com.base.authenticate.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class LoginId {

    private final String id;

    public LoginId() {
        this(null);
    }

    public LoginId(String id) {
        this.id = id;
    }
}
