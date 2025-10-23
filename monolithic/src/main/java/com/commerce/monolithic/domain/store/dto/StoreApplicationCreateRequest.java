package com.commerce.monolithic.domain.store.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(name = "StoreApplicationCreateRequest", description = "스토어 등록 신청 생성 요청")
public class StoreApplicationCreateRequest {
	@NotBlank
	@Schema(description = "스토어 이름", example = "테스트 상점")
	private String name;

	@Schema(description = "상점 설명", example = "간단한 소개 문구")
	private String description;

	@Schema(description = "연락처", example = "02-123-4567")
	private String phone;

	@Schema(description = "주소", example = "서울시 강남구 ...")
	private String address;

	@NotNull
	@Schema(description = "대분류 ID", example = "f1c2a7b1-5c1a-4a35-9b9e-82c8a0d12c9e")
	private UUID bigCategoryId;

	@Schema(description = "소분류 ID", example = "9c3b4b9e-0a7f-4c8e-9f6d-1234567890ab")
	private UUID smallCategoryId;

	@Schema(description = "신청 메모(선택)", example = "오픈 예정일 11월 말")
	private String note;
}