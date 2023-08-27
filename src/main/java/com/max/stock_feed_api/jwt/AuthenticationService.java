package com.max.stock_feed_api.jwt;


public interface AuthenticationService {
    String signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SignInRequest request);
}
