package com.max.reactivedockercompose.jwt;


public interface AuthenticationService {
    String signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SignInRequest request);
}
