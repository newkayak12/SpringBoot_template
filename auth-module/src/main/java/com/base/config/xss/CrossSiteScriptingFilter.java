package com.base.config.xss;

import com.base.config.properties.xxs.Blacklist;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

public class CrossSiteScriptingFilter extends OncePerRequestFilter {
    private final Blacklist path;

    public CrossSiteScriptingFilter(Blacklist path) {
        this.path = path;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        filterChain.doFilter(new RequestWrapper(request), response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String servletPath =  request.getServletPath();
        return !path.path().contains(servletPath);
    }
}
