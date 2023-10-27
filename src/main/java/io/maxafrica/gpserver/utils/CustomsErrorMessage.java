package io.maxafrica.gpserver.utils;

import io.maxafrica.gpserver.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class CustomsErrorMessage {
    public static ResponseEntity<?> getValidatorMessage(BindingResult result) {
        StringBuilder errorMessage = new StringBuilder("Validation failed. Please correct the following errors. ");
        for (FieldError error : result.getFieldErrors()) {
            errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        return ResponseEntity.badRequest().body(new ApiResponse(false, errorMessage.toString()));
    }
}
