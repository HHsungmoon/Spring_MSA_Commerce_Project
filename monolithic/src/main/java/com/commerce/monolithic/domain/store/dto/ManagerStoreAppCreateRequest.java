package com.commerce.monolithic.domain.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "ManagerStoreAppCreateRequest", description = "매니저 스토어 생성/수정 신청 생성 요청")
public class ManagerStoreAppCreateRequest {

	@Schema(description = "소개문구(신청 사유/변경 이유 등)", example = "지점 오픈 신청. 대분류를 한식으로 요청합니다.")
	private String description;

	@Schema(description = "수정 신청일 경우 대상 스토어 ID(신규 생성 신청이면 비움)", example = "b9f1e6f0-6b5c-4d2d-8b43-6f2f5b0a1cde")
	private String storeId;

	@Schema(description = "변경 요청 대분류 ID(옵션, 엔티티 확장 전까지는 description에 메모로 기록)", example = "c3f64d77-2b5f-4f1b-9e4c-07a7c3d1a9f1")
	private String targetBigCategoryId;
}
