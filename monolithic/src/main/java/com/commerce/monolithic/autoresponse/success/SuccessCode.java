package com.commerce.monolithic.autoresponse.success;

import org.springframework.http.HttpStatus;

import com.commerce.monolithic.autoresponse.ApiResponseStatus;

public interface SuccessCode {
	String getCode();

	String getMessage();

	ApiResponseStatus getStatus();

	default HttpStatus httpStatus() {
		return getStatus().getHttpStatus();
	}
}
