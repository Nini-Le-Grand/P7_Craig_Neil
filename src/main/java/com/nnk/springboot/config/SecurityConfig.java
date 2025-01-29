/*
package com.nnk.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                           .requestMatchers("/css/**", "/js/**").permitAll()
                           .anyRequest().authenticated())
                   .formLogin(form -> form
                           .defaultSuccessUrl("/home", true)
                           .permitAll()
                   )
                   .logout(logout -> logout
                           .logoutUrl("/logout")
                           .logoutSuccessUrl("/login?logout")
                           .permitAll()
                   )
                   .sessionManagement(sessionManagement -> sessionManagement
                           .maximumSessions(1)
                           .expiredUrl("/login?sessionExpired=true"))
                   .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
*/
