package com.commerce.monolithic.domain.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.monolithic.autoresponse.ApiResponse;
import com.commerce.monolithic.domain.auth.dto.LoginRequestDto;
import com.commerce.monolithic.domain.auth.dto.LoginResponseDto;
import com.commerce.monolithic.domain.auth.dto.SignupRequestDto;
import com.commerce.monolithic.domain.auth.response.AuthSuccessCode;
import com.commerce.monolithic.domain.auth.service.AuthService;
import com.commerce.monolithic.domain.auth.service.RefreshTokenService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final RefreshTokenService refreshTokenService;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupRequestDto req) {
		authService.signup(req);
		return ApiResponse.success(AuthSuccessCode.SIGNUP_CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto req) {
		LoginResponseDto res = authService.login(req);
		return ApiResponse.success(AuthSuccessCode.LOGIN_OK, res);
	}

}
