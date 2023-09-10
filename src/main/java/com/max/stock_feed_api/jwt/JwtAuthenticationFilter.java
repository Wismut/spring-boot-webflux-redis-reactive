package com.max.stock_feed_api.jwt;

import com.max.stock_feed_api.api_key.ApiKeyController;
import com.max.stock_feed_api.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.isNull;

@XSlf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        if (!request.getRequestURI().contains(ApiKeyController.PATH)) {
            filterChain.doFilter(request, response);
            return;
        }
        final var authHeader = request.getHeader("Authorization");
        final var jwt = authHeader.substring(7);
        final var username = jwtService.getUsernameFromToken(jwt);
        final var user = userRepository.findByUsername(username);
        if (isNull(user) || !jwtService.validateToken(jwt, username)) {
            log.warn("Try to access to protected recourses");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
