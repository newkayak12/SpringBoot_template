package com.base.config.security.jwt;

import com.base.authenticate.dto.SecurityRole;
import com.base.authenticate.model.Role;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record AuthenticationDetails(
    String id,
    String username,
    String password,
    SecurityRole securityRole
) implements UserDetails, Tokenable {

    public AuthenticationDetails {
    }

    public AuthenticationDetails(String id, String username, String password, Role role) {
        this(id, username, password, SecurityRole.findSecurityRole(role));
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
    public String identity() {
        return id();
    }

    @Override
    public String role() {
        return securityRole().name();
    }

    @Override
    public String getUsername() {
        return username;
    }
}
