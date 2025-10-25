package com.commerce.monolithic.domain.store.dto;

import java.util.UUID;

import com.commerce.monolithic.configenum.GlobalEnum;
import com.commerce.monolithic.domain.store.entity.Store;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "StoreDetailResponse", description = "스토어 상세 응답")
public class StoreDetailResponse {
	private final UUID id;
	private final String name;
	private final String description;
	private final GlobalEnum.StoreStatus status;

	public static StoreDetailResponse form(Store s) {
		return StoreDetailResponse.builder()
			.id(s.getId())
			.name(s.getName())
			.description(s.getDescription())
			.status(s.getStatus())
			.build();
	}
}
