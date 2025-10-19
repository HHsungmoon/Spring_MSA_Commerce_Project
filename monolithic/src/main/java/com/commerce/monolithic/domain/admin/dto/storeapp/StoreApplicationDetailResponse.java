package com.commerce.monolithic.domain.admin.dto.storeapp;

import java.time.LocalDateTime;
import java.util.UUID;

import com.commerce.monolithic.configenum.GlobalEnum.StoreStatus;
import com.commerce.monolithic.domain.admin.entity.StoreApplication;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "StoreApplicationDetailResponse", description = "매니저 신청 상세")
public class StoreApplicationDetailResponse {

	@Schema(description = "신청 ID", example = "6f3a9b1c-2b3d-4a5c-8def-1234567890ab")
	private final UUID id;

	@Schema(description = "상태", example = "PENDING", allowableValues = {"PENDING", "APPROVED", "REJECTED"})
	private final StoreStatus status;

	@Schema(description = "생성 시각", example = "2025-09-10T12:34:56")
	private final LocalDateTime createdAt;

	public static StoreApplicationDetailResponse from(StoreApplication a) {
		return StoreApplicationDetailResponse.builder()
			.id(a.getId())
			.status(a.getStatus())
			.createdAt(a.getCreatedAt())
			.build();
	}
}
