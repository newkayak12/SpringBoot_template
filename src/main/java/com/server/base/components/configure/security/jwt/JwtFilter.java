package com.server.base.components.configure.security.jwt;

import com.server.base.components.configure.ConfigMsg;
import com.server.base.components.constants.Constants;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component(value = "JwtFilter")
@DependsOn(value = {"constants", "TokenProvider"})
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private final TokenProvider tokenProvider;

    @Override  //실제 필터링 로직 / 토큰의 인증정보를 SecurityContext에 저장
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String jwt = this.resolveToken(httpServletRequest);

        if ( StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt) ) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }


    private String resolveToken( HttpServletRequest request ) {
        String bearerToken = request.getHeader(Constants.TOKEN_NAME);

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.replace("Bearer ", "");
        }

        return null;
    }


    @PostConstruct
    public void enabled(){
        ConfigMsg.msg("JwtFilter");
    }
}


