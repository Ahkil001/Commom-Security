package com.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.common.jwt.JwtAuthenticationFilter;
import com.common.jwt.JwtValidator;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	private final JwtValidator jwtValidator;

	public SecurityConfiguration(JwtValidator jwtValidator) {
		this.jwtValidator = jwtValidator;
	}

	@Bean
	JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtValidator);
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
				.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}