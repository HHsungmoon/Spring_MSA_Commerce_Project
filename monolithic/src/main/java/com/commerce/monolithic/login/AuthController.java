package com.commerce.monolithic.login;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.monolithic.login.dto.LoginRequestDto;
import com.commerce.monolithic.login.dto.LoginResponseDto;
import com.commerce.monolithic.login.dto.PasswordChangeRequestDto;
import com.commerce.monolithic.login.dto.SignupRequestDto;
import com.commerce.monolithic.login.service.AuthService;
import com.commerce.monolithic.login.service.RefreshTokenService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final RefreshTokenService refreshTokenService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto req) {
		LoginResponseDto res = authService.login(req.getEmail(), req.getPassword());
		return ResponseEntity.ok(res);
	}

	@PostMapping("/signup")
	public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequestDto req) {
		authService.tempSignup(req);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/** 비밀번호 변경 */
	@PostMapping("/password/change")
	public ResponseEntity<Void> changePassword(@Valid @RequestBody PasswordChangeRequestDto req) {

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/refresh")
	public ResponseEntity<LoginResponseDto> refreshToken(
		@RequestHeader(value = "X-Refresh-Token", required = false) String refreshTokenHeader,
		@RequestParam(value = "refreshToken", required = false) String refreshTokenParam,
		@RequestBody(required = false) String refreshTokenBody // 단순 문자열 바디를 허용하는 경우
	) {
		String token = pickFirstNonEmpty(refreshTokenHeader, refreshTokenParam, refreshTokenBody);
		//LoginResponseDto res = refreshTokenService.refresh(token);
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(SecurityException.class)
	public ResponseEntity<String> handleSecurity(SecurityException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	private static String pickFirstNonEmpty(String... values) {
		for (String v : values) {
			if (StringUtils.hasText(v))
				return v;
		}
		throw new IllegalArgumentException("Refresh token is required");
	}
}
