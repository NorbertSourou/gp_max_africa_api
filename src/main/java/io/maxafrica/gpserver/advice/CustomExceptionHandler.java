package io.maxafrica.gpserver.advice;

import io.jsonwebtoken.*;
import io.maxafrica.gpserver.exceptions.BadRequestException;
import io.maxafrica.gpserver.exceptions.NoAuthorizationException;
import io.maxafrica.gpserver.exceptions.RequestNotAcceptableException;
import io.maxafrica.gpserver.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.BindException;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ProblemDetail handleSecurityException(Exception exception) {


        log.error("Error Message - {}", exception.getMessage());
        ProblemDetail problemDetail = null;

        if (exception instanceof BadCredentialsException) {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
        }

        if (exception instanceof AccessDeniedException || exception instanceof NoAuthorizationException) {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
        }

        if (exception instanceof ResourceNotFoundException || exception instanceof NoHandlerFoundException) {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), exception.getMessage());
        }

        if (exception instanceof RequestNotAcceptableException) {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(406), exception.getMessage());
        }

        if (exception instanceof BadRequestException || exception instanceof BindException) {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());
        }

        if (exception instanceof ExpiredJwtException || exception instanceof SignatureException || exception instanceof MalformedJwtException || exception instanceof UnsupportedJwtException || exception instanceof IllegalArgumentException) {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
        }

        return problemDetail;
    }
}
