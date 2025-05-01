package com.base.config.security;

import com.base.authenticate.dto.SecurityRole;
import com.base.config.security.jwt.TokenableAuthenticationDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import uuid.generator.UuidGenerator;

public class WithCustomMockSecurityContextFactory implements WithSecurityContextFactory<WithMockSecurity> {

    @Override
    public SecurityContext createSecurityContext(WithMockSecurity annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        String username = annotation.username();
        SecurityRole role = annotation.role();
        TokenableAuthenticationDetails principal = new TokenableAuthenticationDetails(
            UuidGenerator.generate(), username, "", role
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            principal, "password", principal.getAuthorities()
        );
        context.setAuthentication(authentication);
        return context;
    }
}
