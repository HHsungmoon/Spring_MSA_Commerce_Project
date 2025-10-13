package com.commerce.monolithic.autoresponse;

import org.springframework.http.ResponseEntity;

import com.commerce.monolithic.autoresponse.error.ErrorCode;
import com.commerce.monolithic.autoresponse.success.SuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

//최상위 응답 래퍼: 성공/실패 모두 동일 스키마{ success, status, code, message, data }
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ApiResponse<T> {

	private final boolean success;
	private final int status;      // HTTP 숫자
	private final String code;     // 비즈니스 코드
	private final String message;  // 최종 메시지
	private final T data;          // 실패 시 null

	// ---- 성공 응답 (SuccessCode 사용) ----
	public static <T> ResponseEntity<ApiResponse<T>> success(SuccessCode sc, T data) {
		String msg = (sc.message() == null || sc.message().isBlank())
			? sc.status().getDisplay()
			: sc.message();

		return ResponseEntity.status(sc.httpStatus())
			.body(new ApiResponse<>(true, sc.httpStatus().value(), sc.code(), msg, data));
	}

	// ---- 실패 응답 (ErrorCode 사용, data=null) ----
	public static <T> ResponseEntity<ApiResponse<T>> from(ErrorCode ec) {
		String msg = (ec.message() == null || ec.message().isBlank())
			? ec.status().getDisplay()
			: ec.message();

		return ResponseEntity.status(ec.httpStatus())
			.body(new ApiResponse<>(false, ec.httpStatus().value(), ec.code(), msg, null));
	}

}
