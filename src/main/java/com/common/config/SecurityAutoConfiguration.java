package com.common.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.common.jwt.JwtAuthenticationFilter;
import com.common.jwt.JwtValidator;

@AutoConfiguration
public class SecurityAutoConfiguration {

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(JwtValidator jwtValidator) {

		return new JwtAuthenticationFilter(jwtValidator);

	}

}