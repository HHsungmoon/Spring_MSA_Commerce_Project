package com.commerce.monolithic.autoresponse;

import org.springframework.http.ResponseEntity;

import com.commerce.monolithic.autoresponse.error.ErrorCode;
import com.commerce.monolithic.autoresponse.success.SuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ApiResponse<T> {

	private final boolean success;
	private final int status;      // HTTP code
	private final String code;     // business code
	private final String message;  // final message shown to client
	private final T data;          // null on response

	// Success
	public static <T> ResponseEntity<ApiResponse<T>> success(SuccessCode sc, T data) {
		String msg = (sc.getMessage() == null || sc.getMessage().isBlank())
			? sc.getStatus().getDisplay()
			: sc.getMessage();

		return ResponseEntity.status(sc.httpStatus())
			.body(new ApiResponse<>(true, sc.httpStatus().value(), sc.getCode(), msg, data));
	}

	public static <T> ResponseEntity<ApiResponse<T>> success(SuccessCode sc) {
		String msg = (sc.getMessage() == null || sc.getMessage().isBlank())
			? sc.getStatus().getDisplay()
			: sc.getMessage();

		return ResponseEntity.status(sc.httpStatus())
			.body(new ApiResponse<>(true, sc.httpStatus().value(), sc.getCode(), msg, null));
	}

	// Error
	public static <T> ResponseEntity<ApiResponse<T>> from(ErrorCode ec) {
		String msg = (ec.getMessage() == null || ec.getMessage().isBlank())
			? ec.getStatus().getDisplay()
			: ec.getMessage();

		return ResponseEntity.status(ec.httpStatus())
			.body(new ApiResponse<>(false, ec.httpStatus().value(), ec.getCode(), msg, null));
	}
}
