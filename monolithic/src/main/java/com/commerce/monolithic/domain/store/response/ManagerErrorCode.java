package com.commerce.monolithic.domain.store.response;

import com.commerce.monolithic.autoresponse.ApiResponseStatus;
import com.commerce.monolithic.autoresponse.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ManagerErrorCode implements ErrorCode {
	FORBIDDEN("MNG-4403", "권한이 없습니다.", ApiResponseStatus.FORBIDDEN),
	APPLICATION_NOT_FOUND("MNG-4404", "신청을 찾을 수 없습니다.", ApiResponseStatus.NOT_FOUND),
	APPLICATION_ALREADY_PROCESSED("MNG-4409", "이미 처리된 신청입니다.", ApiResponseStatus.CONFLICT),
	APPLICATION_DUPLICATE_PENDING("MNG-4410", "이미 진행 중인 신청이 있습니다.", ApiResponseStatus.CONFLICT),

	STORE_NOT_FOUND("MNG-5404", "스토어를 찾을 수 없습니다.", ApiResponseStatus.NOT_FOUND),
	STORE_NOT_OWNER("MNG-5403", "해당 스토어의 소유자가 아닙니다.", ApiResponseStatus.FORBIDDEN),

	INVALID_STORE_ID("MNG-4201", "스토어 ID가 유효하지 않습니다.", ApiResponseStatus.BAD_REQUEST),
	INVALID_BIG_CATEGORY_ID("MNG-4202", "대분류 ID가 유효하지 않습니다.", ApiResponseStatus.BAD_REQUEST),
	INVALID_DESCRIPTION("MNG-4203", "소개문구가 유효하지 않습니다.", ApiResponseStatus.BAD_REQUEST),
	MANAGER_NOT_FOUND("MNG-4207", "매니저를 찾을 수 없습니다.", ApiResponseStatus.NOT_FOUND),

	APPLICATION_NOT_OWNED("MNG-4408", "해당 신청에 대한 권한이 없습니다.", ApiResponseStatus.FORBIDDEN);

	private final String code;
	private final String message;
	private final ApiResponseStatus status;
}
