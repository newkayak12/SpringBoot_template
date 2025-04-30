package com.base.config.security;

import com.base.authenticate.dto.SecurityRole;
import com.base.config.security.jwt.JwtFilter;
import com.base.config.security.jwt.JwtProvider;
import com.base.config.security.properties.jwt.JwtProperties;
import com.base.config.security.properties.jwt.Whitelist;
import com.base.config.security.properties.xxs.Blacklist;
import com.base.config.security.xss.CrossSiteScriptingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import password.encoder.PasswordEncodeUtility;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final Whitelist jwtPath;
    private final Blacklist xssPath;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint entryPoint;
    private final JwtProvider provider;


    @Bean
    public JwtProvider jwtProvider(JwtProperties properties) {
        return new JwtProvider(properties);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncodeUtility.getInstance();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
            String.format("%s > %s\n", SecurityRole.ROLE_ADMIN.name(), SecurityRole.ROLE_MANAGER.name())
        );
        stringBuilder.append(
            String.format("%s > %s\n", SecurityRole.ROLE_MANAGER.name(), SecurityRole.ROLE_USER.name())
        );
        roleHierarchy.setHierarchy(stringBuilder.toString());
        return roleHierarchy;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        return security
            .csrf(CsrfConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .exceptionHandling(
                exceptionHandling -> exceptionHandling.authenticationEntryPoint(entryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
            )
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .formLogin(FormLoginConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(
                authorizeRequests -> authorizeRequests.requestMatchers(jwtPath.array())
                    .permitAll().anyRequest().authenticated()
            )
            .addFilterBefore(new JwtFilter(provider), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new CrossSiteScriptingFilter(xssPath), RequestCacheAwareFilter.class)
            .build();
    }

}
