package com.mycompany.tmdd_java.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthSuccessHandler successHandler;

    public SecurityConfig(CustomAuthSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(antMatcher("/admin/**")).hasAuthority("ROLE_ADMIN")
                .requestMatchers(antMatcher("/vendor/**")).hasAuthority("ROLE_VENDOR")
                .requestMatchers(antMatcher("/customer/**"), antMatcher("/checkout/**")).hasAnyAuthority("ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_VENDOR")
                .requestMatchers(
                    antMatcher("/"),
                    antMatcher("/products/**"),
                    antMatcher("/shops/**"),
                    antMatcher("/cart/**"),
                    antMatcher("/login"),
                    antMatcher("/register"),
                    antMatcher("/vendor-register"),
                    antMatcher("/css/**"),
                    antMatcher("/js/**"),
                    antMatcher("/images/**"),
                    antMatcher("/h2-console/**")
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(successHandler)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}
