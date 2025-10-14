package com.commerce.monolithic.domain.auth.response;

import com.commerce.monolithic.autoresponse.ApiResponseStatus;
import com.commerce.monolithic.autoresponse.success.SuccessCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthSuccessCode implements SuccessCode {
	SIGNUP_CREATED("AUTH-2010", "회원가입이 완료되었습니다.", ApiResponseStatus.CREATED),
	LOGIN_OK("AUTH-2000", "로그인에 성공했습니다.", ApiResponseStatus.OK),
	LOGOUT_OK("AUTH-2001", "로그아웃 되었습니다.", ApiResponseStatus.OK);

	private final String code;
	private final String message;
	private final ApiResponseStatus status;
}
