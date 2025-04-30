package com.base.authenticate.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Password {

    private final String password;
    private final String oldPassword;

    public Password() {
        this(null, null);
    }

    public Password(String password) {
        this(password, null);
    }

    public Password(String password, String oldPassword) {
        this.password = password;
        this.oldPassword = oldPassword;
    }

}
