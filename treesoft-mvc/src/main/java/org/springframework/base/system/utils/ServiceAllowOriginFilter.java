package org.springframework.base.system.utils;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/ServiceAllowOriginFilter.class */
public class ServiceAllowOriginFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String originHeader = request.getHeader("Origin");
        if (!StringUtils.isEmpty(originHeader)) {
            response.setHeader("Access-Control-Allow-Credentials", "true");
        } else {
            originHeader = "*";
        }
        response.setHeader("Access-Control-Allow-Origin", originHeader);
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,Accept-Ranges,AppKey,Nonce,Timestamp,Signature,AccessToken,SSOLoginBackUrl");
        response.addHeader("X-Frame-Options", "SAMEORIGIN");
        response.addHeader("Referer-Policy", "origin");
        response.addHeader("Content-Security-Policy", "object-src 'self'");
        response.addHeader("X-Permitted-Cross-Domain-Policies", "master-only");
        response.addHeader("X-Content-Type-Options", "nosniff");
        response.addHeader("X-XSS-Protection", "1; mode=block");
        response.addHeader("X-Download-Options", "noopen");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
    }
}
