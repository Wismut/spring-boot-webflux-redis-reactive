package com.max.stock_feed_api.api_key;

import com.max.stock_feed_api.company.CompanyController;
import com.max.stock_feed_api.stock.StockController;
import com.max.stock_feed_api.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@XSlf4j
@Component
@AllArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {
    private final List<String> protectedByApiKey = List.of(CompanyController.PATH, StockController.PATH);
    private final UserRepository userRepository;
    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    private static final String USERNAME_HEADER_NAME = "USERNAME";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var needToCheckApiKey = protectedByApiKey.stream()
                .anyMatch(s -> request.getRequestURI().contains(s));
        if (!needToCheckApiKey) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            final var requestUsernameHeader = request.getHeader(USERNAME_HEADER_NAME);
            final var user = userRepository.findByUsername(requestUsernameHeader);
            final var authentication = ApiKeyAuthenticationService.getAuthentication(user, request.getHeader(AUTH_TOKEN_HEADER_NAME));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            log.warn("Try to access to protected recourses", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            final var writer = response.getWriter();
            writer.print(e.getMessage());
            writer.flush();
            writer.close();
        }
        filterChain.doFilter(request, response);
    }
}
