package com.commerce.monolithic.autoresponse.success;

import org.springframework.http.HttpStatus;

import com.commerce.monolithic.autoresponse.ApiResponseStatus;

public interface SuccessCode {
	String code();                 // 예: "USER-0001"

	String message();              // null이면 ApiResponseStatus.display 사용

	ApiResponseStatus status();    // OK/CREATED 등

	default HttpStatus httpStatus() {
		return status().getHttpStatus();
	}
}