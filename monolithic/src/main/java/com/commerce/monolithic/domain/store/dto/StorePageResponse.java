package com.commerce.monolithic.domain.store.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "StorePageResponse", description = "내 스토어 목록 페이지 응답")
public class StorePageResponse {
	@Schema(description = "목록")
	private final List<StoreSummary> items;
	@Schema(description = "페이지", example = "0")
	private final int page;
	@Schema(description = "크기", example = "20")
	private final int size;
	@Schema(description = "총 개수", example = "137")
	private final int total;
}