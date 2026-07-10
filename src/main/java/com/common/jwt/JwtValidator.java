package com.common.jwt;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.common.exception.UnauthorizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtValidator {

	private final Key signingKey;

	public JwtValidator(@Value("${jwt.secret}") String secret) {
		this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
	}

	public boolean validateToken(String token) {

		try {

			Jwts.parser().verifyWith((javax.crypto.SecretKey) signingKey).build().parseSignedClaims(token);

			return true;

		} catch (ExpiredJwtException ex) {
			throw new UnauthorizedException("Token has expired");

		} catch (JwtException | IllegalArgumentException ex) {
			throw new UnauthorizedException("Invalid JWT Token");
		}

	}

	public Claims getClaims(String token) {

		return Jwts.parser().verifyWith((javax.crypto.SecretKey) signingKey).build().parseSignedClaims(token)
				.getPayload();
	}

}