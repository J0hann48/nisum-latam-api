package com.nisum.nisumapi.config.annotation;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig extends WebSecurityConfiguration {

    @Bean
    @Primary // Asegura que esta configuración se use en las pruebas
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Puedes configurar CSRF según sea necesario para tus pruebas
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll()); // Permitir todas las solicitudes

        return http.build();
    }
}
