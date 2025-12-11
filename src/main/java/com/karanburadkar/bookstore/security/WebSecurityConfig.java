package com.karanburadkar.bookstore.security;

import com.karanburadkar.bookstore.security.oauth2.OAuth2SuccessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static com.karanburadkar.bookstore.entity.type.RoleType.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    private final JwtAuthFilter jwtAuthFilter;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    public WebSecurityConfig(JwtAuthFilter jwtAuthFilter, HandlerExceptionResolver handlerExceptionResolver, OAuth2SuccessHandler oAuth2SuccessHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionConfig ->
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( "/public/**", "/auth/signup/**", "/auth/login/**", "/login/**", "/").permitAll()
                        .requestMatchers("/users/**", "/management/**")
                            .hasAnyRole(MANAGEMENT.name(), ADMIN.name())
                        .requestMatchers("/books/**", "/author/**", "/bookCategory/**", "/publishers/**", "/reviews/**")
                            .hasAnyRole(CUSTOMER.name(), PUBLISHER.name(), MANAGEMENT.name(), ADMIN.name())
                        .requestMatchers("/customers/**", "/payment/**", "/orders/**")
                            .hasAnyRole(CUSTOMER.name(), MANAGEMENT.name(), ADMIN.name())
                        .anyRequest().authenticated()
                )
                .oauth2Login(oAuth2 -> oAuth2
                        .failureHandler((
                                (request, response, exception)
                                        -> {
                                    log.warn("OAuth2 failure: {}", exception.getMessage(), exception);
                                    response.sendRedirect("/api/login");
                                }
                        ))
                        .successHandler(oAuth2SuccessHandler)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling( exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer
                                .accessDeniedHandler((
                                        (request, response, accessDeniedException)
                                                -> handlerExceptionResolver.resolveException(request, response, null, accessDeniedException)))
                );

        return httpSecurity.build();
    }
}
