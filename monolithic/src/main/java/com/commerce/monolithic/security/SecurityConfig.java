package com.commerce.monolithic.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private static final List<String> ALLOWED_ORIGINS = List.of(
		"http://localhost:3000",
		"http://localhost:5173",
		"http://localhost:8080"
	);

	// API endponit 허용할거 작성.
	public static final String[] PERMIT_URLS = {
		// 기본/Swagger
		"/response",
		"/favicon.ico",
		"/v3/api-docs",
		"/swagger-ui/index.html",
		"/swagger-ui/**",
		"/v3/api-docs/**",
		"/swagger-resources/**",
		"/swagger-ui.html",
		"/webjars/**",

		"/api/auth/register",
		"/api/auth/login"
	};

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthorizationFilter jwtAuthFilter) throws
		Exception {
		//boolean isTestProfile = "test".equals(System.getProperty("spring.profiles.active"));

		http
			.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
			.formLogin(form -> form.disable())
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(PERMIT_URLS).permitAll()
				.requestMatchers("/api/v1/customers/**").hasRole("CUSTOMER")
				.requestMatchers("/api/v1/manager/**").hasRole("MANAGER")
				.requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()
			)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint((_, response, _) -> {
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
					response.setContentType("application/json");
					response.getWriter().write("{\"response\": \"Unauthorized access\"}");
				})
				.accessDeniedHandler((req, res, accessDeniedEx) -> {
					res.setStatus(HttpStatus.FORBIDDEN.value());
					res.setContentType("application/json");
					res.getWriter().write("{\"response\": \"Forbidden: 권한이 없습니다.\"}");
				})
			);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();

		config.setAllowedOrigins(ALLOWED_ORIGINS);
		config.setAllowedMethods(List.of("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true);

		source.registerCorsConfiguration("/**", config);
		return request -> {
			String origin = request.getHeader("Origin");
			if (origin != null && !ALLOWED_ORIGINS.contains(origin)) {
				// System.out.println("CORS 차단: " + origin + "는 허용되지 않은 Origin입니다!");
				throw new RuntimeException("CORS 차단: " + origin + "는 허용되지 않은 Origin입니다!");
			}
			return config;
		};
	}

	@Bean
	public AuthenticationManager authenticationManager(
		AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}

