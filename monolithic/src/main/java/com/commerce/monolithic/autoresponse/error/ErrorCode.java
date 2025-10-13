package com.commerce.monolithic.autoresponse.error;

import org.springframework.http.HttpStatus;

import com.commerce.monolithic.autoresponse.ApiResponseStatus;

public interface ErrorCode {
	String code();

	String message();

	ApiResponseStatus status();

	default HttpStatus httpStatus() {
		return status().getHttpStatus();
	}
}