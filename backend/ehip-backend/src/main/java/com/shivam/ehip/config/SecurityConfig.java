package com.shivam.ehip.config;

import com.shivam.ehip.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter=jwtFilter;
    }

    private static final String PATIENTS_API = "/patients/**";
    private static final String DOCTORS_API = "/doctors/**";
    private static final String MEDICAL_HISTORY_API = "/medical-history/**";
    private static final String RISK_API = "/risk/**";
    private static final String DOCTOR= "DOCTOR";
    private static final String ADMIN= "ADMIN";
    private static final String RECEPTIONIST= "RECEPTIONIST";

    @Bean                                      //Registers this configuration in Spring
    public SecurityFilterChain filterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())       //REST APIs use JWT, not sessions  -- Disables CSRF protection -- Cross-Site Request Forgery
                .cors(Customizer.withDefaults())                // : tells Security to use your MVC CorsConfig
                //CSRF is protection against fake requests using session cookies.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))     // No session stored on server -- server does not remember user — every request must carry its own authentication (JWT).

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.OPTIONS, "/**")
                        .permitAll()                          // <-- ADD THIS: let preflight through, no auth needed

                        .requestMatchers("/auth/**")
                        .permitAll()                             // Login/signup allowed without token

                        //.anyRequest()
                        //.authenticated()

                        .requestMatchers(HttpMethod.GET, PATIENTS_API)
                        .hasAnyRole(ADMIN, DOCTOR, RECEPTIONIST)

                        .requestMatchers(HttpMethod.POST, PATIENTS_API)
                        .hasAnyRole(ADMIN, RECEPTIONIST)

                        .requestMatchers(HttpMethod.PUT, PATIENTS_API)
                        .hasAnyRole(ADMIN,DOCTOR, RECEPTIONIST)

                        .requestMatchers(HttpMethod.DELETE, PATIENTS_API)
                        .hasAnyRole(ADMIN)

                        .requestMatchers(MEDICAL_HISTORY_API)
                        .hasAnyRole(ADMIN,DOCTOR)

                        .requestMatchers(DOCTORS_API)
                        .hasAnyRole(ADMIN,DOCTOR, RECEPTIONIST)

                        .requestMatchers(RISK_API)
                        .hasAnyRole(ADMIN,DOCTOR, RECEPTIONIST)

                )

                .addFilterBefore(                          // Insert your JWT filter before Spring login filter
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
