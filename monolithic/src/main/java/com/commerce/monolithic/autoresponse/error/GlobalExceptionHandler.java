package com.commerce.monolithic.autoresponse.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.commerce.monolithic.autoresponse.ApiResponse;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	// 비즈니스 예외 → 설정된 ErrorCode로 응답
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException ex) {
		log.warn("BusinessException: code={}, msg={}", ex.getErrorCode().getCode(), ex.getMessage());
		return ApiResponse.from(ex.getErrorCode());
	}

	// DTO 바인딩/검증 실패(@Valid)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
		log.warn("Validation failed: {}", ex.getMessage());
		return ApiResponse.from(CommonErrorCode.BAD_REQUEST);
	}

	// 메서드 레벨 검증(@Validated) 등
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<Void>> handleConstraint(ConstraintViolationException ex) {
		return ApiResponse.from(CommonErrorCode.BAD_REQUEST);
	}

	// 요청 본문 파싱/타입 오류
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiResponse<Void>> handleNotReadable(HttpMessageNotReadableException ex) {
		return ApiResponse.from(CommonErrorCode.BAD_REQUEST);
	}

	// 쿼리 파라미터 누락
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ApiResponse<Void>> handleMissingParam(MissingServletRequestParameterException ex) {
		return ApiResponse.from(CommonErrorCode.BAD_REQUEST);
	}

	// DB 제약 위반(UNIQUE, NOT NULL 등) → 보통 400 또는 409
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(DataIntegrityViolationException ex) {
		// 필요하면 별도 ErrorCode를 만들어 409(CONFLICT)로 내려도 됨
		return ApiResponse.from(CommonErrorCode.BAD_REQUEST);
	}

	// 사용자 조회 실패 등을 404로
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleUsernameNotFound(UsernameNotFoundException ex) {
		return ApiResponse.from(CommonErrorCode.NOT_FOUND);
	}

	// 마지막 캐치올 → 500
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleEtc(Exception ex) {
		log.error("Unhandled exception", ex);
		return ApiResponse.from(CommonErrorCode.INTERNAL_ERROR);
	}
}
