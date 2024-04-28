package com.trial.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.trial.service.DefaultUserService;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
    private DefaultUserService userService;
	
	@Autowired
    private JwtGeneratorValidator jwtValidator;
	
    @Autowired
    private AuthenticationManager authenticationManager;
       
    @Value("${superadmin.secretKey}")
    private String superAdminKey;
    
    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String tokenFromCookie = extractTokenFromCookie(request);
        if (tokenFromCookie != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails tokenDetails = userService.loadUserByUsername(jwtValidator.extractUsername(tokenFromCookie));
            System.out.println("User Details: " + tokenDetails);
            System.out.println("Validation: " + jwtValidator.validateToken(tokenFromCookie, tokenDetails));
            if (jwtValidator.validateToken(tokenFromCookie, tokenDetails)) {
                UsernamePasswordAuthenticationToken authentication = jwtValidator.getAuthenticationToken(
                        tokenFromCookie, SecurityContextHolder.getContext().getAuthentication(), tokenDetails);
                System.out.println(authentication);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
