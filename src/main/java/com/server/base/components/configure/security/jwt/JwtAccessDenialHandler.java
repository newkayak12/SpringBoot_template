package com.server.base.components.configure.security.jwt;

import com.server.base.components.configure.ConfigMsg;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 필요한 권한이 없을 경우
@Component(value = "JwtAccessDeniedHandler")
public class JwtAccessDenialHandler implements AccessDeniedHandler {

//https://colabear754.tistory.com/172
    private HandlerExceptionResolver resolver;

    @Autowired
    public JwtAccessDenialHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        resolver.resolveException(request, response, null, new UnsupportedJwtException(accessDeniedException.getMessage(), accessDeniedException.getCause()));
    }

    @PostConstruct
    public void enabled(){
        ConfigMsg.msg("JwtAccessDenialHandler");
    }
}
