package com.max.reactivedockercompose.registration;

import com.max.reactivedockercompose.jwt.AuthenticationService;
import com.max.reactivedockercompose.jwt.SignUpRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/register")
public class RegistrationController {
    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest request) {
        final String response = authenticationService.signup(request);
        return ResponseEntity.ok(response);
    }
}
