package io.maxafrica.gpserver.security;

import io.maxafrica.gpserver.exceptions.ResourceNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
	
	@Override
	public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authExcp)
			throws IOException {
		// TODO Auto-generated method stub

		logger.error("Responding with unauthorized error. Message - {}", authExcp.getMessage());
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Sorry, You're not authorized to access this resource.");
	}

	@ExceptionHandler(value = {ResourceNotFoundException.class})
	public void commence(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
		// 403 HTTP Response
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to do that: "+exception.getMessage());
	}

	@ExceptionHandler (value = {Exception.class})
	public void commence(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {
		// 500 HTTP Response
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error: " + exception.getMessage());
	}

}
