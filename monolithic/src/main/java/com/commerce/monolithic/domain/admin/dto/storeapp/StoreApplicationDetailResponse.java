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
@Schema(name = "StoreApplicationDetailResponse", description = "매니저 스토어 오픈 신청 상세 응답")
public class StoreApplicationDetailResponse {

	@Schema(description = "신청 ID (UUID v7/BINARY(16))")
	private final UUID id;

	@Schema(description = "신청한 매니저 ID", example = "7b2a1f7e-5a3b-47f5-9a8a-2f9b6b8a1234")
	private final UUID managerId;

	@Schema(description = "승인 후 매핑된 스토어 ID (미승인 시 null)", example = "c8f9d1a2-3b4c-5d6e-7f80-9123456789ab")
	private final UUID storeId;

	@Schema(description = "신청 설명/비고", example = "신규 지점 오픈 신청입니다.")
	private final String description;

	@Schema(description = "상태", example = "PENDING", allowableValues = {"PENDING", "APPROVED", "REJECTED"})
	private final StoreStatus status;

	@Schema(description = "심사자(Admin) ID (심사 전 null)", example = "1f2e3d4c-5b6a-7980-91a2-b3c4d5e6f708")
	private final UUID reviewedById;

	@Schema(description = "거절 사유(거절 시 세팅)", example = "필수 서류 누락")
	private final String reason;

	@Schema(description = "신청 요청 시각", example = "2025-09-10T10:11:12")
	private final LocalDateTime requestedAt;

	@Schema(description = "심사 완료 시각(승인/거절 시 세팅)", example = "2025-09-11T14:30:00")
	private final LocalDateTime reviewedAt;

	@Schema(description = "생성 시각(BaseTimeEntity)", example = "2025-09-10T12:34:56")
	private final LocalDateTime createdAt;

	public static StoreApplicationDetailResponse from(StoreApplication a) {
		return StoreApplicationDetailResponse.builder()
			.id(a.getId())
			.managerId(a.getManager() != null ? a.getManager().getId() : null)
			.storeId(a.getStore() != null ? a.getStore().getId() : null)
			.description(a.getDescription())
			.status(a.getStatus())
			.reviewedById(a.getReviewedBy() != null ? a.getReviewedBy().getId() : null)
			.reason(a.getReason())
			.requestedAt(a.getRequestedAt())
			.reviewedAt(a.getReviewedAt())
			.createdAt(a.getCreatedAt())
			.build();
	}
}
