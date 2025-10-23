package com.commerce.monolithic.domain.store.dto;

import com.commerce.monolithic.configenum.GlobalEnum.CommonStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "StoreSearchRequest", description = "내 스토어 목록 조회 조건")
public class StoreSearchRequest {
	@Schema(description = "상태 필터", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "BANNED"})
	private CommonStatus status;

	@Schema(description = "키워드(이름/설명)", example = "치킨")
	private String keyword;

	@Min(0)
	@Schema(description = "페이지(0-base)", example = "0", defaultValue = "0")
	private Integer page = 0;

	@Min(1)
	@Max(200)
	@Schema(description = "페이지 크기", example = "20", defaultValue = "20")
	private Integer size = 20;
}
