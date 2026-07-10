package com.common.jwt;

public final class JwtConstants {

    private JwtConstants() {
    }

    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    // JWT Claims
    public static final String USER_ID = "userId";
    public static final String CUSTOMER_ID = "customerId";
    public static final String ROLE = "role";
    public static final String USERNAME = "username";
}