package com.commerce.monolithic.autoresponse;

import org.springframework.http.HttpStatus;

public enum ApiResponseStatus {
	OK(HttpStatus.OK, "정상 처리되었습니다."),
	CREATED(HttpStatus.CREATED, "생성되었습니다.");

	private final HttpStatus httpStatus;
	private final String display;

	ApiResponseStatus(HttpStatus httpStatus, String display) {
		this.httpStatus = httpStatus;
		this.display = display;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getDisplay() {
		return display;
	}
}

