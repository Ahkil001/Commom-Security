package com.common.jwt;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.common.context.UserContext;
import com.common.exception.UnauthorizedException;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtValidator jwtValidator;

	public JwtAuthenticationFilter(JwtValidator jwtValidator) {

		this.jwtValidator = jwtValidator;

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header = request.getHeader(JwtConstants.HEADER);

		if (header == null || !header.startsWith(JwtConstants.PREFIX)) {

			filterChain.doFilter(request, response);
			return;
		}

		try {

			String token = header.substring(7);

			Claims claims = jwtValidator.validate(token);
			UserContext context = UserContext.builder().userId(UUID.fromString(claims.get("userId", String.class)))
					.username(claims.getSubject()).email(claims.get("email", String.class))
					.roles(claims.get("roles", List.class)).active(claims.get("active", String.class)).build();

			UserContext.setContext(context);

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					context.getUsername(), null, Collections.emptyList());

			SecurityContextHolder.getContext().setAuthentication(authentication);

			filterChain.doFilter(request, response);

		} catch (Exception e) {

			throw new UnauthorizedException("Invalid JWT Token");

		} finally {

			UserContext.clear();

			SecurityContextHolder.clearContext();

		}

	}

}