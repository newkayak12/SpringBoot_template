package com.server.base.components.configure.security.jwt;

import com.server.base.components.configure.ConfigMsg;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//유효한 자격증명을 제공하지 않고 접근하면
@Component(value = "JwtAuthenticationEntryPoint")
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private  HandlerExceptionResolver resolver;

    @Autowired
    public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        resolver.resolveException(request, response, null, new ExpiredJwtException(null, null, authException.getMessage(), authException.getCause()));
    }

    @PostConstruct
    public void enabled(){
        ConfigMsg.msg("JwtAuthenticationEntryPoint");
    }
}
