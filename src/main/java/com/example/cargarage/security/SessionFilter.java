package com.example.cargarage.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class SessionFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        if (session != null) {

            // ✅ Check for Admin Authentication
            if (session.getAttribute("adminEmail") != null) {
                UserDetails adminDetails = User.withUsername(session.getAttribute("adminEmail").toString())
                        .password("[PROTECTED]")
                        .roles("ADMIN")
                        .build();

                var authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        adminDetails, null, adminDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // ✅ Check for User Authentication
            else if (session.getAttribute("userEmail") != null) {
                UserDetails userDetails = User.withUsername(session.getAttribute("userEmail").toString())
                        .password("[PROTECTED]")
                        .roles("USER")
                        .build();

                var authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // ✅ Check for Employee Authentication
            else if (session.getAttribute("employeeEmail") != null) {
                UserDetails employeeDetails = User.withUsername(session.getAttribute("employeeEmail").toString())
                        .password("[PROTECTED]")
                        .roles("EMPLOYEE")
                        .build();

                var authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        employeeDetails, null, employeeDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }
}
