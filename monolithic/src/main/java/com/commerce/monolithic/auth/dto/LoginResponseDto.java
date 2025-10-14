package com.commerce.monolithic.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginResponseDto {
	private String token;
	private String refreshToken;
	private String type;
}
