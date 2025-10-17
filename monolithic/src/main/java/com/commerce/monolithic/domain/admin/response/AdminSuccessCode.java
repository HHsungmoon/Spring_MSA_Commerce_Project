package com.commerce.monolithic.domain.admin.response;

import com.commerce.monolithic.autoresponse.ApiResponseStatus;
import com.commerce.monolithic.autoresponse.success.SuccessCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminSuccessCode implements SuccessCode {
	INFO_OK("ADMIN-0001", "관리자 본인 정보 조회 성공", ApiResponseStatus.OK),
	CATEGORY_LIST_OK("ADMIN-0200", "카테고리 생성 성공", ApiResponseStatus.OK),
	CATEGORY_CREATE_OK("ADMIN-0201", "카테고리 생성 성공", ApiResponseStatus.CREATED),
	CATEGORY_UPDATE_OK("ADMIN-0202", "카테고리 수정 성공", ApiResponseStatus.OK),
	CATEGORY_DELETE_OK("ADMIN-0203", "카테고리 삭제 성공", ApiResponseStatus.OK),
	CATEGORY_REORDER_OK("ADMIN-0204", "카테고리 순서 변경 성공", ApiResponseStatus.OK);

	private final String code;
	private final String message;
	private final ApiResponseStatus status;
}
