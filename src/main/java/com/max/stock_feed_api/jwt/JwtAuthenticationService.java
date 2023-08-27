package com.max.stock_feed_api.jwt;

import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class JwtAuthenticationService {
    private JwtTokenUtil jwtTokenUtil;

    public String createToken(@NonNull String username) {
        return jwtTokenUtil.generateToken(username);
    }
}
