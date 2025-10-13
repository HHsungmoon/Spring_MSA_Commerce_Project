package com.commerce.monolithic.autoresponse.error;

/** 서비스 레이어에서 던지는 표준 비즈니스 예외 */
public class BusinessException extends RuntimeException {
	private final ErrorCode errorCode;

	public BusinessException(ErrorCode errorCode) {
		super(resolveMessage(errorCode));
		this.errorCode = errorCode;
	}

	public BusinessException(ErrorCode errorCode, Throwable cause) {
		super(resolveMessage(errorCode), cause);
		this.errorCode = errorCode;
	}

	private static String resolveMessage(ErrorCode ec) {
		String msg = ec.message();
		return (msg == null || msg.isBlank()) ? ec.status().getDisplay() : msg;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
