package com.base.authenticate.dto;

import static com.base.authenticate.model.Role.ADMIN;
import static com.base.authenticate.model.Role.MANAGER;
import static com.base.authenticate.model.Role.USER;

import com.base.authenticate.model.Role;
import java.util.Arrays;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

public enum SecurityRole implements GrantedAuthority {
    ROLE_ADMIN(ADMIN),
    ROLE_MANAGER(MANAGER),
    ROLE_USER(USER);

    @Getter
    private final Role role;

    SecurityRole(Role role) {
        this.role = role;
    }

    public static final SecurityRole findSecurityRole(Role role) {
        return Arrays.stream(SecurityRole.values())
            .filter(securityRole -> securityRole.getRole().equals(role))
            .findFirst()
            .orElse(null);
    }

    @Override
    public String getAuthority() {
        return this.name();
    }
}
