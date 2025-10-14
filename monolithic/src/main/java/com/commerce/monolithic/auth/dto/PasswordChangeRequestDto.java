package com.commerce.monolithic.auth.dto;

import lombok.Getter;

@Getter
public class PasswordChangeRequestDto {
	private String currentPassword;
	private String newPassword;
}
