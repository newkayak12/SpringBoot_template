package com.base.authenticate.entity;


import com.base.authenticate.model.LoginId;
import com.base.authenticate.model.Password;
import com.base.authenticate.model.Role;
import com.base.config.TimeBasedUuidStrategy;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sample")
@Entity
public class Authenticate {

    @Id
    @TimeBasedUuidStrategy
    private String id;
    @Embedded
    private LoginId username;
    @Embedded
    private Password password;
    private Role role;
}
