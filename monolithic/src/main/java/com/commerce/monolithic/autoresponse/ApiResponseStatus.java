package com.commerce.monolithic.autoresponse;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ApiResponseStatus {
	OK(HttpStatus.OK, "정상 처리되었습니다."),
	CREATED(HttpStatus.CREATED, "생성이 완료되었습니다."),
	NO_CONTENT(HttpStatus.NO_CONTENT, "정상 처리되었습니다. Data는 없습니다."),
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
	NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 리소스를 찾을 수 없습니다."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
	FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
	INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
	CONFLICT(HttpStatus.CONFLICT, "충돌문제가 발생했습니다.");

	private final HttpStatus httpStatus;
	private final String display;

	ApiResponseStatus(HttpStatus httpStatus, String display) {
		this.httpStatus = httpStatus;
		this.display = display;
	}
}
