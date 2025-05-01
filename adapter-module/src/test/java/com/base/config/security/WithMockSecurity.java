package com.base.config.security;

import com.base.authenticate.dto.SecurityRole;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.test.context.support.WithSecurityContext;

@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@WithSecurityContext(factory = WithCustomMockSecurityContextFactory.class)
public @interface WithMockSecurity {

    String username() default "admin";

    SecurityRole role() default SecurityRole.ROLE_ADMIN;
}
