package com.reservationbroker.reservation.config;

import com.reservationbroker.reservation.jwt.filters.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Za Spring Security 6+
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Omogućava @PreAuthorize anotacije
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Onemogući CSRF zaštitu jer koristimo JWT
                .csrf(csrf -> csrf.disable())

                // Konfiguriši CORS
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(List.of(
                            "*"

                    )); // Dozvoli frontend sa lokalnih i mrežnih IP adresa
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setExposedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))

                // Postavi politiku sesija na stateless
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Postavi AuthenticationProvider
                .authenticationProvider(authenticationProvider)

                // Dodaj JWT filter pre UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Konfiguriši autorizaciju zahteva
                .authorizeHttpRequests(auth -> auth
                        // Dozvoli javni pristup za specifične endpoint-e
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/api/reservations/create", "/api/reservations/create-with-marketing").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/public/**", "/images/**").permitAll() // Allow public access to images

                        // Svi ostali zahtevi zahtevaju autentifikaciju
                        .anyRequest().authenticated()
                )

                // Onemogući Frame Options ako koristiš H2 konzolu ili slično
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }


}
