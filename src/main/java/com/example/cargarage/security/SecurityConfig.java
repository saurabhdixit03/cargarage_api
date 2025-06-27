package com.example.cargarage.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ CORS support
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            	    .requestMatchers("/admin/register", "/admin/login", "/admin/session", "/admin/logout").permitAll()
            	    .requestMatchers("/user/register", "/user/login", "/user/session", "/user/logout").permitAll()
            	    .requestMatchers("/employee/login", "/employee/session", "/employee/logout").permitAll() // ✅ Employee open endpoints
            	    .requestMatchers("/ws-status/**").permitAll()
            	    .requestMatchers("/admin/**").hasRole("ADMIN")
            	    .requestMatchers("/user/**").hasRole("USER")
            	    .requestMatchers("/employee/**").hasRole("EMPLOYEE") // ✅ Secured employee routes
            	    .anyRequest().authenticated()
            	)

            	

            .sessionManagement(session -> session.sessionFixation().migrateSession())
            .addFilterBefore(new SessionFilter(), UsernamePasswordAuthenticationFilter.class)
            .formLogin(login -> login.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

 /*   @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }  */
}
