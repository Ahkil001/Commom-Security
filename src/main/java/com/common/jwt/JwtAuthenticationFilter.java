package com.common.jwt;

import java.io.IOException;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.common.context.CustomerContext;
import com.common.context.CustomerContextHolder;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtValidator jwtValidator;

	public JwtAuthenticationFilter(JwtValidator jwtValidator) {
		this.jwtValidator = jwtValidator;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {

			String token = resolveToken(request);

			if (StringUtils.hasText(token) && jwtValidator.validateToken(token)) {
				Claims claims = jwtValidator.getClaims(token);

				CustomerContext context = CustomerContext.builder()
				        .userId(claims.get(JwtConstants.USER_ID, Long.class))
				        .customerId(claims.get(JwtConstants.CUSTOMER_ID, Long.class))
				        .username(claims.get(JwtConstants.USERNAME, String.class))
				        .role(claims.get(JwtConstants.ROLE, String.class))
				        .build();


				CustomerContextHolder.setContext(context);
			}

			filterChain.doFilter(request, response);

		} finally {
			CustomerContextHolder.clearContext();
		}
	}

	private String resolveToken(HttpServletRequest request) {

		String bearerToken = request.getHeader(JwtConstants.AUTHORIZATION);

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtConstants.TOKEN_PREFIX)) {

			return bearerToken.substring(JwtConstants.TOKEN_PREFIX.length());
		}

		return null;
	}
}