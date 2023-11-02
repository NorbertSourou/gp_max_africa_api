package io.maxafrica.gpserver.services;

import io.maxafrica.gpserver.dto.ApiResponse;
import io.maxafrica.gpserver.dto.LoginRequest;
import io.maxafrica.gpserver.dto.RegisterUser;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ApiResponse> createUser(RegisterUser registerUser);

    ResponseEntity<?>  authenticateUser(LoginRequest loginRequest);

    ResponseEntity<?> getValidateToken(String refreshToken);
}
