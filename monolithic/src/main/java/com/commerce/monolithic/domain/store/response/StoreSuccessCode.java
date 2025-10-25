package com.commerce.monolithic.domain.store.response;

import com.commerce.monolithic.autoresponse.ApiResponseStatus;
import com.commerce.monolithic.autoresponse.success.SuccessCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StoreSuccessCode implements SuccessCode {
	
	// 고객용 조회
	STORE_LIST_OK("STR-0200", "스토어 목록 조회 성공", ApiResponseStatus.OK),
	STORE_DETAIL_OK("STR-0201", "스토어 상세 조회 성공", ApiResponseStatus.OK),

	// 매니저용 조회
	MANAGER_STORE_LIST_OK("STR-0202", "매니저 내 스토어 목록 조회 성공", ApiResponseStatus.OK);

	private final String code;
	private final String message;
	private final ApiResponseStatus status;
}
