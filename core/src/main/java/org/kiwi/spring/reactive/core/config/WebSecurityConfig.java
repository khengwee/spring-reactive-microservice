package org.kiwi.spring.reactive.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @Value("${spring.security.auth-url-path-pattern}")
    private String authenticatedUrlPathPattern;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/*").permitAll()
                .pathMatchers(authenticatedUrlPathPattern).authenticated()
                .and().httpBasic()
                .and()
                .build();
    }

    @Bean
    ReactiveUserDetailsService userDetailService() {
        User.UserBuilder user = User.withUsername(username)
                .roles("USER")
                .password("{noop}"+password);

        return new MapReactiveUserDetailsService(user.build());
    }
}
