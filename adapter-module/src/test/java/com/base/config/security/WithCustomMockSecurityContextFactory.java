package com.base.config.security;

import com.base.authenticate.dto.SecurityRole;
import com.base.config.security.jwt.AuthenticationDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithCustomMockSecurityContextFactory implements WithSecurityContextFactory<WithMockSecurity> {

    @Override
    public SecurityContext createSecurityContext(WithMockSecurity annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        String username = annotation.username();
        SecurityRole role = annotation.role();
        AuthenticationDetails principal = new AuthenticationDetails(username, "", role);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            principal, "password", principal.getAuthorities()
        );
        context.setAuthentication(authentication);
        return context;
    }
}
