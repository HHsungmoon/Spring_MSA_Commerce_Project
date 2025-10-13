package com.commerce.monolithic.autoresponse.error;

import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.commerce.monolithic.autoresponse.ApiResponse;

/**
 * 전역 예외 → 표준 응답 스키마 변환
 * 실패 응답은 항상 data=null
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiResponse<Void>> business(BusinessException ex) {
		return ApiResponse.from(ex.getErrorCode());
	}

	@ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
	public ResponseEntity<ApiResponse<Void>> validation(Exception ex) {
		return ApiResponse.from(CommonErrorCode.BAD_REQUEST);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiResponse<Void>> accessDenied(AccessDeniedException ex) {
		return ApiResponse.from(CommonErrorCode.FORBIDDEN);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ApiResponse<Void>> notFound(NoSuchElementException ex) {
		return ApiResponse.from(CommonErrorCode.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> any(Exception ex) {
		return ApiResponse.from(CommonErrorCode.INTERNAL_ERROR);
	}
}
