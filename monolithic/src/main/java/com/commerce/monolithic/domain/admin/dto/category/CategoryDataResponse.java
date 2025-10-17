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

	@Schema(description = "카테고리 ID", example = "9c3b4b9e-0a7f-4c8e-9f6d-1234567890ab")
	private final UUID id;

	@Schema(description = "대분류 여부", example = "true")
	private final boolean big;

	@Schema(description = "이름", example = "한식")
	private final String name;

	@Schema(description = "슬러그", example = "korean-food")
	private final String slug;

	@Schema(description = "부모 대분류 ID(소분류일 때만)", example = "f1c2a7b1-5c1a-4a35-9b9e-82c8a0d12c9e")
	private final UUID parentId;

	public static CategoryDataResponse from(BigCategory b) {
		return CategoryDataResponse.builder()
			.id(b.getId())
			.big(true)
			.name(b.getName())
			.slug(b.getSlug())
			.parentId(null)
			.build();
	}

	public static CategoryDataResponse from(SmallCategory s) {
		return CategoryDataResponse.builder()
			.id(s.getId())
			.big(false)
			.name(s.getName())
			.slug(s.getSlug())
			.parentId(s.getBigCategory() != null ? s.getBigCategory().getId() : null)
			.build();
	}
}
