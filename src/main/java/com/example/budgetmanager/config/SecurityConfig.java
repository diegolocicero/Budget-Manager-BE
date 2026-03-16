package com.example.budgetmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // disabilito CSRF perchè l'app e stateless e usa token JWT (no cookies)
            .csrf(csrf -> csrf.disable())
            
            //  setto la sessione a stateless
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 3. Definiamo le regole di accesso
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() //  espongo pubblicamente solo le API di auth
                .anyRequest().authenticated()                 
            )
            
            //  configuro il server come resource server con JWT come metodo di auth
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}