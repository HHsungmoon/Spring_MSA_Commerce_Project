package com.commerce.monolithic.domain.auth.response;

import com.commerce.monolithic.autoresponse.ApiResponseStatus;
import com.commerce.monolithic.autoresponse.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {
	DUPLICATE_EMAIL("AUTH-4001", "이미 사용 중인 이메일입니다.", ApiResponseStatus.BAD_REQUEST),
	INVALID_CREDENTIALS("AUTH-4002", "이메일 또는 비밀번호가 올바르지 않습니다.", ApiResponseStatus.UNAUTHORIZED),
	CUSTOMER_NOT_FOUND("AUTH-4004", "고객 정보를 찾을 수 없습니다.", ApiResponseStatus.NOT_FOUND),
	TOKEN_INVALID("AUTH-4100", "유효하지 않은 토큰입니다.", ApiResponseStatus.UNAUTHORIZED),
	TOKEN_EXPIRED("AUTH-4101", "토큰이 만료되었습니다.", ApiResponseStatus.UNAUTHORIZED);

	private final String code;
	private final String message;
	private final ApiResponseStatus status;
}
