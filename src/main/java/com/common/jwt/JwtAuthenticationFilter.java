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
import com.common.context.UserContextHolder;
import com.common.exception.UnauthorizedException;
import com.common.jwt.JwtConstants;
import com.common.jwt.JwtValidator;

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

			UserContext context = new UserContext();

			context.setUserId(UUID.fromString(claims.get("userId", String.class)));

			context.setUsername(claims.getSubject());

			context.setEmail(claims.get("email", String.class));

			context.setRoles(claims.get("roles", List.class));

			UserContextHolder.setContext(context);

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					context.getUsername(), null, Collections.emptyList());

			SecurityContextHolder.getContext().setAuthentication(authentication);

			filterChain.doFilter(request, response);

		} catch (Exception e) {

			throw new UnauthorizedException("Invalid JWT Token");

		} finally {

			UserContextHolder.clear();

			SecurityContextHolder.clearContext();

		}

	}

}