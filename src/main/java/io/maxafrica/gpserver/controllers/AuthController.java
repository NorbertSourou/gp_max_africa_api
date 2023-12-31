package io.maxafrica.gpserver.controllers;


import io.maxafrica.gpserver.dto.ApiResponse;
import io.maxafrica.gpserver.dto.JwtAuthenticationResponse;
import io.maxafrica.gpserver.dto.LoginRequest;
import io.maxafrica.gpserver.dto.RegisterUser;
import io.maxafrica.gpserver.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<ApiResponse>  createUser(@RequestBody  RegisterUser registerUser) {
        return this.authService.createUser(registerUser);
    }

    @PostMapping("login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return this.authService.authenticateUser(loginRequest);
    }

    @PostMapping("refresh")
    public ResponseEntity<?> getValidateToken(@RequestBody JwtAuthenticationResponse jwtAuthenticationResponse) {
        return this.authService.getValidateToken(jwtAuthenticationResponse.getRefreshToken());
    }
}
