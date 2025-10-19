package com.commerce.monolithic.domain.admin.response;

import com.commerce.monolithic.autoresponse.ApiResponseStatus;
import com.commerce.monolithic.autoresponse.success.SuccessCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StoreApplicationSuccessCode implements SuccessCode {
	LIST_OK("APP-0200", "신청 목록 조회 성공", ApiResponseStatus.OK),
	DETAIL_OK("APP-0201", "신청 상세 조회 성공", ApiResponseStatus.OK),
	APPROVE_OK("APP-0202", "신청 승인 성공", ApiResponseStatus.OK),
	REJECT_OK("APP-0203", "신청 거절 성공", ApiResponseStatus.OK);

	private final String code;
	private final String message;
	private final ApiResponseStatus status;
}
