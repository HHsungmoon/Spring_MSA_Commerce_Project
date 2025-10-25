package com.commerce.monolithic.domain.admin.dto.category;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.commerce.monolithic.domain.store.entity.BigCategory;
import com.commerce.monolithic.domain.store.entity.SmallCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "CategoryTreeResponse", description = "카테고리 트리 응답 (대분류-소분류)")
public class CategoryTreeResponse {

	@Schema(description = "대분류 목록")
	private final List<BigNode> items;

	@Getter
	@Builder
	public static class BigNode {
		private final UUID id;
		private final String name;
		private final List<SmallNode> children;

		public static BigNode of(BigCategory b, List<SmallCategory> smalls) {
			return BigNode.builder()
				.id(b.getId())
				.name(b.getName())
				.children(smalls.stream().map(SmallNode::of).collect(Collectors.toList()))
				.build();
		}
	}

	@Getter
	@Builder
	public static class SmallNode {
		private final UUID id;
		private final String name;

		public static SmallNode of(SmallCategory s) {
			return SmallNode.builder()
				.id(s.getId())
				.name(s.getName())
				.build();
		}
	}
}
