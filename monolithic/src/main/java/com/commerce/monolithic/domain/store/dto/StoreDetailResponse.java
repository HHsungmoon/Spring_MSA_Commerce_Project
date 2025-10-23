package com.commerce.monolithic.domain.store.dto;

import java.util.UUID;

import com.commerce.monolithic.configenum.GlobalEnum;
import com.commerce.monolithic.domain.store.entity.Store;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "StoreDetailResponse", description = "스토어 상세")
public class StoreDetailResponse {
	@Schema(description = "스토어 ID")
	private final UUID id;

	@Schema(description = "이름")
	private final String name;

	@Schema(description = "상태", allowableValues = {"ACTIVE", "INACTIVE", "BANNED"})
	private final GlobalEnum.StoreStatus status;

	public static StoreDetailResponse from(Store s) {
		return StoreDetailResponse.builder()
			.id(s.getId())
			.name(s.getName())
			.status(s.getStatus())
			.build();
	}
}
