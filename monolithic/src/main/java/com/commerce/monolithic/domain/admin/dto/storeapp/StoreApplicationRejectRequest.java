package com.commerce.monolithic.domain.admin.dto.storeapp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(name = "StoreApplicationRejectRequest", description = "매니저 거절 요청")
public class StoreApplicationRejectRequest {

	@NotBlank
	@Schema(description = "거절 사유", example = "제출 서류 식별 불가")
	private String reason;
}
