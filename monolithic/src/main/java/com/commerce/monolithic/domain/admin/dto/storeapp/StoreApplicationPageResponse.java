package com.commerce.monolithic.domain.admin.dto.storeapp;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Schema(name = "StoreApplicationPageResponse", description = "매니저 신청 페이지 응답")
public class StoreApplicationPageResponse {

	@Schema(description = "요약 항목 리스트")
	private final List<StoreApplicationSummary> items;

	@Schema(description = "요청 페이지 번호(0-base)", example = "0")
	private final int page;

	@Schema(description = "페이지 크기", example = "20")
	private final int size;

	@Schema(description = "총 항목 수", example = "137")
	private final int total;
}
