package com.commerce.monolithic.autoresponse.error;

import com.commerce.monolithic.autoresponse.ApiResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {
	BAD_REQUEST("COMMON-400", null, ApiResponseStatus.BAD_REQUEST),
	NOT_FOUND("COMMON-404", null, ApiResponseStatus.NOT_FOUND),
	UNAUTHORIZED("COMMON-401", null, ApiResponseStatus.UNAUTHORIZED),
	FORBIDDEN("COMMON-403", null, ApiResponseStatus.FORBIDDEN),
	INTERNAL_ERROR("COMMON-500", null, ApiResponseStatus.INTERNAL_ERROR);

	private final String code;
	private final String message;
	private final ApiResponseStatus status;
}
