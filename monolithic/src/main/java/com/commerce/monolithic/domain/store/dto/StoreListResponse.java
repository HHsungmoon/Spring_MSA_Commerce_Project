package com.commerce.monolithic.domain.store.dto;

import java.util.UUID;

import com.commerce.monolithic.configenum.GlobalEnum;
import com.commerce.monolithic.domain.store.entity.Store;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "StoreListResponse", description = "스토어 목록 아이템 응답")
public class StoreListResponse {
	private final UUID id;
	private final String name;
	private final GlobalEnum.StoreStatus status;

	public static StoreListResponse form(Store s) {
		return StoreListResponse.builder()
			.id(s.getId())
			.name(s.getName())
			.status(s.getStatus())
			.build();
	}
}
