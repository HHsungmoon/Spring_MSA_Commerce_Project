package com.commerce.monolithic.domain.auth.dto;

import lombok.Getter;

@Getter
public class PasswordChangeRequestDto {
	private String currentPassword;
	private String newPassword;
}
