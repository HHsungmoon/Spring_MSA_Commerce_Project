package com.commerce.monolithic.domain.admin.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "CategoryCreateRequest", description = "카테고리 생성 요청")
public class CategoryCreateRequest {

	@Schema(description = "대분류 여부(true: 대분류, false: 소분류)", example = "true")
	private boolean big;

	@Schema(description = "부모 대분류 ID(소분류 생성 시 필수)", example = "f1c2a7b1-5c1a-4a35-9b9e-82c8a0d12c9e")
	private String parentId;

	@Schema(description = "카테고리 이름", example = "한식")
	private String name;

	@Schema(description = "슬러그", example = "korean-food")
	private String slug;
}
