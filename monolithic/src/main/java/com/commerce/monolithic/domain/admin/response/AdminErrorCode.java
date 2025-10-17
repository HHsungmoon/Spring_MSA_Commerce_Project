package com.commerce.monolithic.domain.admin.response;

import com.commerce.monolithic.autoresponse.ApiResponseStatus;
import com.commerce.monolithic.autoresponse.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminErrorCode implements ErrorCode {
	UNAUTHORIZED("ADMIN-1001", "인증 정보가 없거나 유효하지 않습니다.", ApiResponseStatus.UNAUTHORIZED),
	FORBIDDEN("ADMIN-1002", "해당 리소스에 접근 권한이 없습니다.", ApiResponseStatus.FORBIDDEN),
	ADMIN_NOT_FOUND("ADMIN-1404", "관리자 정보를 찾을 수 없습니다.", ApiResponseStatus.NOT_FOUND),

	CATEGORY_NOT_FOUND("ADMIN-4204", "카테고리를 찾을 수 없습니다.", ApiResponseStatus.NOT_FOUND),
	PARENT_NOT_FOUND("ADMIN-4205", "상위 카테고리를 찾을 수 없습니다.", ApiResponseStatus.BAD_REQUEST),
	CATEGORY_HAS_CHILDREN("ADMIN-4206", "하위 카테고리가 존재하여 삭제할 수 없습니다.", ApiResponseStatus.CONFLICT),
	CATEGORY_DUPLICATE_NAME("ADMIN-4207", "동일 이름의 카테고리가 이미 존재합니다.", ApiResponseStatus.CONFLICT),
	INVALID_CATEGORY_TYPE("ADMIN-4208", "카테고리 타입이 유효하지 않습니다.", ApiResponseStatus.BAD_REQUEST);

	private final String code;
	private final String message;
	private final ApiResponseStatus status;
}
