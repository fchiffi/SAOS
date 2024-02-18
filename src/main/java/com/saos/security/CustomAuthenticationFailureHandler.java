package com.saos.security;

import java.io.IOException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	// Classe per la gestione degli errori di autenticazione
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.AuthenticationException exception) throws IOException, ServletException {
		
		System.out.println(exception.getMessage());

		response.sendRedirect("/login");

	}

}
