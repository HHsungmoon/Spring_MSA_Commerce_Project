package com.commerce.monolithic.domain.store.response;

import com.commerce.monolithic.autoresponse.ApiResponseStatus;
import com.commerce.monolithic.autoresponse.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StoreErrorCode implements ErrorCode {

	// 인증/권한
	FORBIDDEN("STR-4403", "권한이 없습니다.", ApiResponseStatus.FORBIDDEN),

	// 유효성/파라미터
	INVALID_STORE_ID("STR-4201", "스토어 ID가 유효하지 않습니다.", ApiResponseStatus.BAD_REQUEST),
	INVALID_BIG_CATEGORY_ID("STR-4202", "대분류 ID가 유효하지 않습니다.", ApiResponseStatus.BAD_REQUEST),

	// 조회/리소스
	STORE_NOT_FOUND("STR-4203", "스토어를 찾을 수 없습니다.", ApiResponseStatus.NOT_FOUND),
	BIG_CATEGORY_NOT_FOUND("STR-4204", "대분류 카테고리를 찾을 수 없습니다.", ApiResponseStatus.NOT_FOUND),
	MANAGER_NOT_FOUND("STR-4205", "매니저를 찾을 수 없습니다.", ApiResponseStatus.NOT_FOUND),
	INVALID_DESCRIPTION("MNG-4203", "소개문구가 유효하지 않습니다.", ApiResponseStatus.BAD_REQUEST),
	APPLICATION_NOT_OWNED("MNG-4408", "해당 신청에 대한 권한이 없습니다.", ApiResponseStatus.FORBIDDEN);

	private final String code;
	private final String message;
	private final ApiResponseStatus status;
}
