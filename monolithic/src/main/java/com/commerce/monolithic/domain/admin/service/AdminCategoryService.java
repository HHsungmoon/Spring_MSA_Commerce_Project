package com.commerce.monolithic.domain.admin.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.monolithic.autoresponse.error.BusinessException;
import com.commerce.monolithic.domain.admin.dto.category.CategoryCreateRequest;
import com.commerce.monolithic.domain.admin.dto.category.CategoryDataResponse;
import com.commerce.monolithic.domain.admin.dto.category.CategoryTreeResponse;
import com.commerce.monolithic.domain.admin.dto.category.CategoryUpdateRequest;
import com.commerce.monolithic.domain.admin.response.AdminErrorCode;
import com.commerce.monolithic.domain.store.entity.BigCategory;
import com.commerce.monolithic.domain.store.entity.SmallCategory;
import com.commerce.monolithic.domain.store.service.BigCategoryBase;
import com.commerce.monolithic.domain.store.service.SmallCategoryBase;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminCategoryService {

	private final BigCategoryBase bigBase;
	private final SmallCategoryBase smallBase;

	@Transactional(readOnly = true)
	public CategoryTreeResponse getTree(UUID adminId) {
		List<BigCategory> bigs = bigBase.findAll();
		List<SmallCategory> smalls = smallBase.findAll();

		Map<UUID, List<SmallCategory>> smallByBig = smalls.stream()
			.filter(s -> s.getBigCategory() != null)
			.collect(Collectors.groupingBy(s -> s.getBigCategory().getId()));

		List<CategoryTreeResponse.BigNode> items = bigs.stream()
			.map(b -> CategoryTreeResponse.BigNode.of(b, smallByBig.getOrDefault(b.getId(), List.of())))
			.toList();

		return new CategoryTreeResponse(items);
	}

	@Transactional
	public CategoryDataResponse create(UUID adminId, CategoryCreateRequest req) {
		if (req.isBig()) {
			BigCategory saved = bigBase.save(BigCategory.builder()
				.name(req.getName())
				.slug(req.getSlug())
				.build());
			return CategoryDataResponse.from(saved);
		} else {
			UUID parentId = parseUUID(req.getParentId(), AdminErrorCode.PARENT_NOT_FOUND);
			BigCategory parent = bigBase.getOrThrow(parentId);

			SmallCategory saved = smallBase.save(SmallCategory.builder()
				.bigCategory(parent)
				.name(req.getName())
				.slug(req.getSlug())
				.build());
			return CategoryDataResponse.from(saved);
		}
	}

	@Transactional
	public CategoryDataResponse update(UUID adminId, String categoryId, CategoryUpdateRequest req) {
		UUID id = parseUUID(categoryId, AdminErrorCode.CATEGORY_NOT_FOUND);

		if (req.isBig()) {
			BigCategory b = bigBase.getOrThrow(id);
			if (req.getName() != null)
				b.setName(req.getName());
			if (req.getSlug() != null)
				b.setSlug(req.getSlug());
			return CategoryDataResponse.from(b);
		} else {
			SmallCategory s = smallBase.getOrThrow(id);
			if (req.getName() != null)
				s.setName(req.getName());
			if (req.getSlug() != null)
				s.setSlug(req.getSlug());
			if (req.getParentId() != null) {
				UUID parentId = parseUUID(req.getParentId(), AdminErrorCode.PARENT_NOT_FOUND);
				BigCategory parent = bigBase.getOrThrow(parentId);
				s.setBigCategory(parent);
			}
			return CategoryDataResponse.from(s);
		}
	}

	@Transactional
	public void delete(UUID adminId, String categoryId) {
		UUID id = parseUUID(categoryId, AdminErrorCode.CATEGORY_NOT_FOUND);

		// 대분류인지 먼저 확인
		try {
			BigCategory b = bigBase.getOrThrow(id);
			if (smallBase.hasChildren(b.getId())) {
				throw new BusinessException(AdminErrorCode.CATEGORY_HAS_CHILDREN);
			}
			bigBase.delete(b);
			return;
		} catch (BusinessException notBig) {
			// CATEGORY_NOT_FOUND 일 수도 있어 계속 진행
		}

		SmallCategory s = smallBase.getOrThrow(id);
		smallBase.delete(s);
	}

	private UUID parseUUID(String src, AdminErrorCode onError) {
		try {
			return UUID.fromString(src);
		} catch (Exception e) {
			throw new BusinessException(onError);
		}
	}
}
