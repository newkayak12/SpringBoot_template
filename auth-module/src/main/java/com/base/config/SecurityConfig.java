package com.base.config;

import com.base.config.jwt.JwtFilter;
import com.base.config.jwt.JwtProvider;
import com.base.config.properties.jwt.JwtProperties;
import com.base.config.properties.jwt.Whitelist;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(value = {Whitelist.class, JwtProperties.class})
@RequiredArgsConstructor
public class SecurityConfig {
    private final Whitelist jwtPath;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint entryPoint;
    private final JwtProvider provider;


    @Bean
    public JwtProvider jwtProvider(JwtProperties properties) {
        return new JwtProvider(properties);
    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        return security
            .csrf(CsrfConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .exceptionHandling(exceptionHandling -> {
                exceptionHandling.authenticationEntryPoint(entryPoint)
                                 .accessDeniedHandler(accessDeniedHandler);
            })
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(FormLoginConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorizeRequests -> {
                authorizeRequests.requestMatchers(jwtPath.array()).permitAll()
                                 .anyRequest().authenticated();
            })
            .addFilterBefore(new JwtFilter(provider), UsernamePasswordAuthenticationFilter.class)
            .build();
    }

}
