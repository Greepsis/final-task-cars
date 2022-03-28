package com.senla.cars.rest.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtToken jwtToken;

    public JwtTokenFilter(JwtToken jwtToken){
        this.jwtToken = jwtToken;
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = resolveToken((HttpServletRequest) servletRequest);
        if (token != null && jwtToken.validateJwtToken(token)){
            Authentication authentication = jwtToken.getAuthentication(token);
            if (authentication != null){
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
