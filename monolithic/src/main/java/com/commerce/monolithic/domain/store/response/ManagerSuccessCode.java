package com.commerce.monolithic.domain.store.response;

import com.commerce.monolithic.autoresponse.ApiResponseStatus;
import com.commerce.monolithic.autoresponse.success.SuccessCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ManagerSuccessCode implements SuccessCode {
	APP_CREATE_OK("MNG-0100", "스토어 신청 생성 성공", ApiResponseStatus.OK),
	APP_DETAIL_OK("MNG-0101", "스토어 신청 상세 조회 성공", ApiResponseStatus.OK),
	APP_WITHDRAW_OK("MNG-0102", "스토어 신청 철회 성공", ApiResponseStatus.OK),

	STORE_LIST_OK("MNG-0200", "내 스토어 목록 조회 성공", ApiResponseStatus.OK),
	STORE_DETAIL_OK("MNG-0201", "내 스토어 상세 조회 성공", ApiResponseStatus.OK),
	STORE_UPDATE_OK("MNG-0202", "내 스토어 수정 성공", ApiResponseStatus.OK);

	private final String code;
	private final String message;
	private final ApiResponseStatus status;
}
