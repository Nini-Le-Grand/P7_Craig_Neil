package com.nnk.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for the application.
 * This class configures HTTP security, authentication, and authorization settings.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * Configures the security filter chain for the application.
     * @param http the {@link HttpSecurity} object used to configure security settings
     * @return a {@link SecurityFilterChain} that manages security rules
     * @throws Exception if an error occurs while configuring security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth.requestMatchers("/register", "/css/**")
                                                      .permitAll()
                                                      .requestMatchers("/admin/**")
                                                      .hasRole("ADMIN")
                                                      .anyRequest()
                                                      .authenticated())
                   .formLogin(form -> form.loginPage("/login")
                                          .defaultSuccessUrl("/bidList/list", true)
                                          .failureUrl("/login?error=true")
                                          .permitAll())
                   .logout(logout -> logout.logoutUrl("/app-logout")
                                           .logoutSuccessUrl("/login?logout")
                                           .permitAll())
                   .sessionManagement(sessionManagement -> sessionManagement.maximumSessions(1)
                                                                            .expiredUrl("/login?sessionExpired=true"))
                   .build();
    }

    /**
     * Provides a {@link PasswordEncoder} bean that uses BCrypt hashing algorithm.
     *
     * @return a {@link BCryptPasswordEncoder} instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


