package com.base.authenticate.dto;

import com.base.authenticate.model.Role;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record AuthenticationDetails(
    String username,
    String password,
    SecurityRole securityRole
) implements UserDetails {

    public AuthenticationDetails {
    }

    public AuthenticationDetails(String username, String password, Role role) {
        this(username, password, SecurityRole.findSecurityRole(role));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(securityRole);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
