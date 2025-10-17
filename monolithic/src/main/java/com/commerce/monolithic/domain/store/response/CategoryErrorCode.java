package com.commerce.monolithic.domain.store.response;

import com.commerce.monolithic.autoresponse.ApiResponseStatus;
import com.commerce.monolithic.autoresponse.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryErrorCode implements ErrorCode {
	CATEGORY_NOT_FOUND("CATEGORY-4040", "카테고리를 찾을 수 없습니다.", ApiResponseStatus.NOT_FOUND),
	PARENT_NOT_FOUND("CATEGORY-4041", "상위 카테고리를 찾을 수 없습니다.", ApiResponseStatus.BAD_REQUEST),
	CATEGORY_HAS_CHILDREN("CATEGORY-4090", "하위 소분류가 존재하여 삭제할 수 없습니다.", ApiResponseStatus.CONFLICT),
	CATEGORY_DUPLICATE_NAME("CATEGORY-4091", "동일 이름/슬러그의 카테고리가 이미 존재합니다.", ApiResponseStatus.CONFLICT),
	INVALID_CATEGORY_TYPE("CATEGORY-4001", "카테고리 타입이 유효하지 않습니다.", ApiResponseStatus.BAD_REQUEST);

	private final String code;
	private final String message;
	private final ApiResponseStatus status;
}