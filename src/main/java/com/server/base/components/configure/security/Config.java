package com.server.base.components.configure.security;

import com.server.base.components.configure.ConfigMsg;
import com.server.base.components.configure.security.jwt.JwtAccessDenialHandler;
import com.server.base.components.configure.security.jwt.JwtAuthenticationEntryPoint;
import com.server.base.components.configure.security.jwt.JwtFilter;
import com.server.base.components.configure.security.jwt.JwtSecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.PostConstruct;


@EnableWebSecurity(debug = true)
@Configuration(value = "security_configuration")
@DependsOn(value = {"JwtAuthenticationEntryPoint", "JwtAccessDeniedHandler", "JwtSecurityConfig"} )
@RequiredArgsConstructor
public class Config {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDenialHandler jwtAccessDeniedHandler;
    private final JwtSecurityConfig jwtSecurityConfig;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
       return   httpSecurity
                .csrf().disable() //token 사용이므로 csrf disable\
                .cors().and()

                .exceptionHandling(
                        handler -> {
                            handler.authenticationEntryPoint(jwtAuthenticationEntryPoint);
                            handler.accessDeniedHandler(jwtAccessDeniedHandler);
                        }
                )

                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests() // /api/user/* 는 모두 허용
                .antMatchers("/api/v1/user/sign/in").permitAll()
                .anyRequest().authenticated() //이외는 허용하지 않음
                .and()

                .apply(jwtSecurityConfig).and()
                .build();
    }


    @PostConstruct
    public void enabled(){
        ConfigMsg.msg("Spring Security");
    }

}
