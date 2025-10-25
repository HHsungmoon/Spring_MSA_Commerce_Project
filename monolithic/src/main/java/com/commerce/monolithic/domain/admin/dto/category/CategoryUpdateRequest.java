package com.commerce.monolithic.domain.admin.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "CategoryUpdateRequest", description = "카테고리 수정 요청")
public class CategoryUpdateRequest {

	@Schema(description = "대분류 여부(true: 대분류 수정, false: 소분류 수정)", example = "true")
	private boolean big;

	@Schema(description = "소분류에서 부모 변경 시 대분류 ID", example = "f1c2a7b1-5c1a-4a35-9b9e-82c8a0d12c9e")
	private String parentId;

	@Schema(description = "카테고리 이름", example = "분식")
	private String name;

}
