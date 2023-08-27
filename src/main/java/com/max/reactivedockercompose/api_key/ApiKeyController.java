package com.max.reactivedockercompose.api_key;

import com.max.reactivedockercompose.jwt.AuthenticationService;
import com.max.reactivedockercompose.jwt.JwtAuthenticationResponse;
import com.max.reactivedockercompose.jwt.SignInRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/get-api-key")
public class ApiKeyController {
    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<String> getApiKey(@RequestBody SignInRequest request) {
        JwtAuthenticationResponse signin = authenticationService.signin(request);
        return ResponseEntity.ok(signin.getToken());
    }
}
