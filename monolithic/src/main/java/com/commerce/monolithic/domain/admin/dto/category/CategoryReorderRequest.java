package com.commerce.monolithic.domain.admin.dto.category;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "CategoryReorderRequest", description = "카테고리 정렬 변경 요청")
public class CategoryReorderRequest {

	@Schema(description = "정렬 변경 항목 목록")
	private List<Item> items;

	@Getter
	@Schema(name = "CategoryReorderItem", description = "카테고리 정렬 변경 항목")
	public static class Item {
		@Schema(description = "카테고리 ID(UUID)", example = "9c3b4b9e-0a7f-4c8e-9f6d-1234567890ab")
		private String categoryId;

		@Schema(description = "변경할 정렬 순서", example = "7")
		private Integer orderNo;
	}
}
