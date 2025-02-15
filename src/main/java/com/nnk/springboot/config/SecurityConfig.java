package com.nnk.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


