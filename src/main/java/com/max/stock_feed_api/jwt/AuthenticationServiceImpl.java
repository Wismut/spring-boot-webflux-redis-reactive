package com.max.stock_feed_api.jwt;


import com.max.stock_feed_api.user.Role;
import com.max.stock_feed_api.user.User;
import com.max.stock_feed_api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;
import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationService jwtService;

    @Override
    public String signup(@NonNull SignUpRequest request) {
        final var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .apiKey(randomUUID().toString())
                .role(Role.USER).build();
        userRepository.save(user);
        return "User " + user.getUsername() + " was successfully created with API key = " + user.getApiKey();
    }

    @Override
    public JwtAuthenticationResponse signin(@NonNull SignInRequest request) {
        final var user = userRepository.findByUsername(request.getUsername());
        if (isNull(user)) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        var jwt = jwtService.createToken(user.block().getUsername());
        return JwtAuthenticationResponse.builder().jwt(jwt).build();
    }
}
