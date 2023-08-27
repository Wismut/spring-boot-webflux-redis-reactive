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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.hasText;

@XSlf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtService;
    private final JwtUserDetailsService jwtUserDetailsService;
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
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
//        if (!hasText(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
        jwt = authHeader.substring(7);
        username = jwtService.getUsernameFromToken(jwt);
        var user = userRepository.findByUsername(username);
        if (isNull(user) || !hasText(user.getToken()) || !jwtService.validateToken(jwt, username)) {
            return;
        }
//        if (StringUtils.hasText(username)
//                && isNull(SecurityContextHolder.getContext().getAuthentication())) {
//            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
//            if (jwtService.validateToken(jwt, userDetails)) {
//                SecurityContext context = SecurityContextHolder.createEmptyContext();
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                context.setAuthentication(authToken);
//                SecurityContextHolder.setContext(context);
//            }
//        }
        filterChain.doFilter(request, response);
    }
}
