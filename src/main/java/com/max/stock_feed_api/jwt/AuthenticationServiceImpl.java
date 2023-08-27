package com.max.stock_feed_api.jwt;


import com.max.stock_feed_api.user.Role;
import com.max.stock_feed_api.user.User;
import com.max.stock_feed_api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationService jwtService;

    @Override
    public String signup(@NonNull SignUpRequest request) {
        var user = User.builder().
                username(request.getUsername()).
                password(passwordEncoder.encode(request.getPassword())).
                role(Role.USER).build();
        userRepository.save(user);
        return "User " + request.getUsername() + " was successfully created";
    }

    @Override
    public JwtAuthenticationResponse signin(@NonNull SignInRequest request) {
        var user = userRepository.findByUsername(request.getUsername());
        if (isNull(user)) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        var jwt = jwtService.createToken(user.getUsername());
        user.setToken(jwt);
        userRepository.update(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
}
