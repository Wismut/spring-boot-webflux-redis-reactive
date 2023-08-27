package com.max.stock_feed_api.jwt;

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
import static org.springframework.util.StringUtils.hasText;

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
        if (!request.getRequestURI().contains("/api/companies") &&
                !request.getRequestURI().contains("/api/stocks")) {
            filterChain.doFilter(request, response);
            return;
        }
        final var authHeader = request.getHeader("Authorization");
        final var jwt = authHeader.substring(7);
        final var username = jwtService.getUsernameFromToken(jwt);
        var user = userRepository.findByUsername(username);
        if (isNull(user) || !hasText(user.getToken()) || !jwtService.validateToken(jwt, username)) {
            return;
        }
        filterChain.doFilter(request, response);
    }
}
