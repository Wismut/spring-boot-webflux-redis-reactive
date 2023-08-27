package com.max.stock_feed_api.jwt;

import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class JwtAuthenticationService {
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;

    public String createToken(@NonNull String username, @NonNull String password) {
        authenticate(username, password);
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(username);
        return jwtTokenUtil.generateToken(userDetails);
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new RuntimeException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("INVALID_CREDENTIALS", e);
        }
    }
}
