package com.commerce.monolithic.autoresponse.error;

import org.springframework.http.HttpStatus;

import com.commerce.monolithic.autoresponse.ApiResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {

	BAD_REQUEST("COMMON-0001", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
	UNAUTHORIZED("COMMON-0002", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
	FORBIDDEN("COMMON-0003", "권한이 없습니다.", HttpStatus.FORBIDDEN),
	NOT_FOUND("COMMON-0004", "요청한 자원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	CONFLICT("COMMON-0005", "요청이 현재 리소스 상태와 충돌합니다.", HttpStatus.CONFLICT),
	INTERNAL_ERROR("COMMON-9999", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

	private final String code;
	private final String message;
	private final HttpStatus status;

	@Override
	public String code() {
		return "";
	}

	@Override
	public String message() {
		return "";
	}

	@Override
	public ApiResponseStatus status() {
		return null;
	}
}