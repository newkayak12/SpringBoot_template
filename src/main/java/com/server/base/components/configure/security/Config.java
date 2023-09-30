package com.server.base.components.configure.security;

import com.server.base.components.configure.security.jwt.JwtAccessDeniedHandler;
import com.server.base.components.configure.security.jwt.JwtAuthenticationEntryPoint;
import com.server.base.components.configure.security.jwt.JwtSecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity(debug = true)
@Configuration(value = "security_configuration")
@DependsOn(value = {"JwtAuthenticationEntryPoint", "JwtAccessDeniedHandler", "JwtSecurityConfig"} )
@RequiredArgsConstructor
public class Config {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtSecurityConfig jwtSecurityConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
       return   httpSecurity
                .csrf().disable() //token 사용이므로 csrf disable
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //stateless
                .and()

                .authorizeHttpRequests()
                .antMatchers("/api/user/*").permitAll() // /api/user/* 는 모두 허용
                .anyRequest().authenticated() //이외는 허용하지 않음
                .and()

                .apply(jwtSecurityConfig)
                .and()

                .build();
    }

}
