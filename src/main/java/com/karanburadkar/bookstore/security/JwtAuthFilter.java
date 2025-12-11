package com.karanburadkar.bookstore.security;

import com.karanburadkar.bookstore.exception.authentication.InvalidJwtTokenException;
import com.karanburadkar.bookstore.exception.database.UserNotFoundException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.karanburadkar.bookstore.entity.User;
import com.karanburadkar.bookstore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final HandlerExceptionResolver exceptionResolver;

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public JwtAuthFilter(
            HandlerExceptionResolver handlerExceptionResolver,
            UserRepository userRepository, JwtService jwtService
    ) {
        this.exceptionResolver = handlerExceptionResolver;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {

            log.debug("incoming request: {} {}", request.getMethod(), request.getRequestURI());
//            response.setHeader("Access-Token-", "http://localhost:5500");

            long start = System.currentTimeMillis();

            // 1. Skip if user already authenticated or request for public apis
            if (shouldSkip(request) || SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }

            // 2. Extract Jwt token from bearer
            final String token = resolveToken(request);
            if (token == null && !shouldSkip(request)) {
                log.warn("Missing Jwt token");
                throw new InvalidJwtTokenException("Missing jwt token");
            }

            // 3. Extract username from token
            String username = jwtService.getUsernameFromToken(token);
            if (username == null || jwtService.isExpiredToken(token)) {
                throw new InvalidJwtTokenException("Invalid token signature or expired token");
            }

            // 4. get user from repository
            User user = userRepository.findUserWithRolesByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("No user found"));

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            // 5. set user is authenticated
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            log.debug("JWT filter executed in {} ms", System.currentTimeMillis() - start);

            // 6. Continue filters
            filterChain.doFilter(request, response);

        } catch (JwtException | UserNotFoundException ex) {
            log.warn("JWT Authentication failed: {}", ex.getMessage());
            SecurityContextHolder.clearContext();
            exceptionResolver.resolveException(request, response, null, ex);
        } catch (Exception ex) {
            log.error("Unexpected error in JwtAuthFilter", ex);
            SecurityContextHolder.clearContext();
            exceptionResolver.resolveException(request, response, null, ex);
        }

    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private boolean shouldSkip(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/public") ||
                uri.startsWith("/auth/login") ||
                uri.startsWith("/auth/signup") ||
                uri.startsWith("/auth/signup/publisher");
    }
}
