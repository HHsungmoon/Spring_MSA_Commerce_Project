package com.commerce.monolithic.domain.admin.dto.category;

import java.util.UUID;

import com.commerce.monolithic.domain.store.entity.BigCategory;
import com.commerce.monolithic.domain.store.entity.SmallCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "CategoryDataResponse", description = "카테고리 단건 응답")
public class CategoryDataResponse {

	@Schema(description = "카테고리 ID", example = "f1c2a7b1-5c1a-4a35-9b9e-82c8a0d12c9e")
	private final UUID id;

	@Schema(description = "대분류 여부(true: 대분류, false: 소분류)", example = "true")
	private final boolean big;

	@Schema(description = "카테고리 이름", example = "분식")
	private final String name;

	@Schema(description = "부모 대분류 ID(소분류일 경우만 값 존재)", example = "c8f9d1a2-3b4c-5d6e-7f80-9123456789ab")
	private final UUID parentId;

	public static CategoryDataResponse of(BigCategory b) {
		return CategoryDataResponse.builder()
			.id(b.getId())
			.big(true)
			.name(b.getName())
			.parentId(null)
			.build();
	}

	public static CategoryDataResponse of(SmallCategory s) {
		return CategoryDataResponse.builder()
			.id(s.getId())
			.big(false)
			.name(s.getName())
			.parentId(s.getBigCategory() != null ? s.getBigCategory().getId() : null)
			.build();
	}
}
