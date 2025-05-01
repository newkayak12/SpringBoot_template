package com.base.config.security.jwt;

import com.base.authenticate.dto.AuthenticationDetails;
import com.base.authenticate.dto.SecurityRole;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record TokenableAuthenticationDetails(
    String id,
    String username,
    String password,
    SecurityRole securityRole
) implements UserDetails, Tokenable {

    public TokenableAuthenticationDetails {
    }

    public static TokenableAuthenticationDetails from(AuthenticationDetails details) {
        return new TokenableAuthenticationDetails(
            details.id(), details.username(), details.password(), details.securityRole()
        );
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
        return id;
    }

    @Override
    public String role() {
        return securityRole.name();
    }

    @Override
    public String getUsername() {
        return username;
    }
}
