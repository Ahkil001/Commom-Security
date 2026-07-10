package com.common.jwt;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.common.context.UserContext;

import io.jsonwebtoken.Claims;

@Component
public class JwtClaimsExtractor {

	@SuppressWarnings("unchecked")
	public UserContext extract(Claims claims) {

		return UserContext.builder().userId(UUID.fromString(claims.get(JwtConstants.USER_ID, String.class)))
				.username(claims.getSubject()).email(claims.get(JwtConstants.EMAIL, String.class))
				.roles((List<String>) claims.get(JwtConstants.ROLES)).build();

	}

}