package com.max.stock_feed_api.api_key;

import com.max.stock_feed_api.jwt.AuthenticationService;
import com.max.stock_feed_api.jwt.JwtAuthenticationResponse;
import com.max.stock_feed_api.jwt.SignInRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping()
public class ApiKeyController {
    public static final String PATH = "/api/get-api-key";
    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<String> getApiKey(@RequestBody SignInRequest request) {
        JwtAuthenticationResponse signin = authenticationService.signin(request);
        return ResponseEntity.ok(signin.getToken());
    }
}
