package com.commerce.monolithic.autoresponse.error;

import org.springframework.http.HttpStatus;

import com.commerce.monolithic.autoresponse.ApiResponseStatus;

public interface ErrorCode {
	String getCode();

	String getMessage();           // nullable; blank면 status.display 사용

	ApiResponseStatus getStatus();

	default HttpStatus httpStatus() {
		return getStatus().getHttpStatus();
	}
}

