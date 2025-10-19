package com.commerce.monolithic.domain.admin.response;

import com.commerce.monolithic.autoresponse.ApiResponseStatus;
import com.commerce.monolithic.autoresponse.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StoreApplicationErrorCode implements ErrorCode {
	APPLICATION_NOT_FOUND("APP-4404", "신청을 찾을 수 없습니다.", ApiResponseStatus.NOT_FOUND),
	ALREADY_PROCESSED("APP-4090", "이미 처리된 신청입니다.", ApiResponseStatus.CONFLICT);

	private final String code;
	private final String message;
	private final ApiResponseStatus status;
}
