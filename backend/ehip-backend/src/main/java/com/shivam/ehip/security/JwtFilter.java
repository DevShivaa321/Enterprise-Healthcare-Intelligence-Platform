package com.shivam.ehip.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;        //pass request to next filter
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;   //incoming request
import jakarta.servlet.http.HttpServletResponse;   //response to client

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;  //Represents authenticated user
import org.springframework.security.core.authority.SimpleGrantedAuthority;  //Represents user role (e.g., ROLE_ADMIN)
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; //Adds request details (IP, session etc.)
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;     //  Ensures filter runs once per reques

import com.shivam.ehip.login.CustomUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;


/* It intercepts every request, extracts JWT token, validates it, and sets authentication. */

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userService;

    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)

            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");


        if (request.getServletPath().startsWith("/auth")) {
            /*  Skip login/signup endpoints -- User doesn’t have token yet */
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Then skip authentication
            filterChain.doFilter(request, response);
            return;
        }
        try {

            String token = authHeader.substring(7);     // Removes "Bearer "

            String loginName = jwtUtil.extractLoginName(token);

            // Prevents duplicate authentication
            if (loginName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                var user = userService.loadUserByLoginName(loginName);

                if (jwtUtil.validateToken(token, user.getLoginName())) {

                    String role = jwtUtil.extractRole(token);

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
                            java.util.List.of(new SimpleGrantedAuthority("ROLE_" + role)));          // Creates authenticated user

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));     // Adds request info: -IP address  -Session info

                    SecurityContextHolder.getContext().setAuthentication(auth);        // Spring knows user is authenticated -- Role-based access works
                }
            }

        } catch (ExpiredJwtException ex) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token Expired");
            return;
        } catch (Exception ex) {
            logger.error("JWT Error occurred", ex);
        }

        filterChain.doFilter(request, response);       // Pass request to: Next filter -- Controller

    }
}
