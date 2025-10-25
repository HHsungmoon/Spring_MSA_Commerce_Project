package com.commerce.monolithic.domain.admin.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
	public CategoryTreeResponse getTree() {
		List<BigCategory> bigs = bigBase.findAll();
		List<SmallCategory> smalls = smallBase.findAll();

		Map<UUID, List<SmallCategory>> byBig = smalls.stream()
			.filter(s -> s.getBigCategory() != null)
			.collect(Collectors.groupingBy(s -> s.getBigCategory().getId()));

		List<CategoryTreeResponse.BigNode> items = bigs.stream()
			.map(b -> CategoryTreeResponse.BigNode.of(b, byBig.getOrDefault(b.getId(), Collections.emptyList())))
			.collect(Collectors.toList());

		return CategoryTreeResponse.builder().items(items).build();
	}

	@Transactional(readOnly = true)
	public List<CategoryDataResponse> getList() {
		List<CategoryDataResponse> resp = new ArrayList<>();
		bigBase.findAll().forEach(b -> resp.add(CategoryDataResponse.of(b)));
		smallBase.findAll().forEach(s -> resp.add(CategoryDataResponse.of(s)));
		resp.sort(Comparator.comparing(CategoryDataResponse::isBig).reversed()
			.thenComparing(CategoryDataResponse::getName));
		return resp;
	}

	@Transactional
	public CategoryDataResponse create(CategoryCreateRequest req) {
		if (req.isBig()) {
			// 중복 이름 체크 (대분류)
			boolean dup = bigBase.findAll().stream()
				.anyMatch(b -> b.getName().equalsIgnoreCase(req.getName()));
			if (dup)
				throw new BusinessException(AdminErrorCode.CATEGORY_DUPLICATE_NAME);

			BigCategory b = new BigCategory();
			b.setName(req.getName());
			BigCategory saved = bigBase.save(b);
			return CategoryDataResponse.of(saved);
		} else {
			// 소분류: parentId 필수 + 유효성
			if (req.getParentId() == null || req.getParentId().isBlank()) {
				throw new BusinessException(AdminErrorCode.PARENT_NOT_FOUND);
			}
			UUID parent = parseUUID(req.getParentId(), AdminErrorCode.PARENT_NOT_FOUND);
			BigCategory big = bigBase.getOrThrow(parent); // 찾지 못하면 Base에서 예외

			// 중복 이름 체크 (같은 대분류 하에서 소분류 이름 중복 방지)
			boolean dup = smallBase.findAll().stream()
				.anyMatch(s -> s.getBigCategory() != null
					&& parent.equals(s.getBigCategory().getId())
					&& s.getName().equalsIgnoreCase(req.getName()));
			if (dup)
				throw new BusinessException(AdminErrorCode.CATEGORY_DUPLICATE_NAME);

			SmallCategory s = new SmallCategory();
			s.setName(req.getName());
			s.setBigCategory(big);
			SmallCategory saved = smallBase.save(s);
			return CategoryDataResponse.of(saved);
		}
	}

	@Transactional
	public CategoryDataResponse update(UUID id, CategoryUpdateRequest req) {
		if (req.isBig()) {
			BigCategory b = bigBase.getOrThrow(id); // 못 찾으면 Base에서 예외

			// 이름 중복 체크 (자기 자신 제외)
			if (req.getName() != null) {
				boolean dup = bigBase.findAll().stream()
					.anyMatch(x -> !x.getId().equals(b.getId())
						&& x.getName().equalsIgnoreCase(req.getName()));
				if (dup)
					throw new BusinessException(AdminErrorCode.CATEGORY_DUPLICATE_NAME);
				b.setName(req.getName());
			}

			BigCategory saved = bigBase.save(b);
			return CategoryDataResponse.of(saved);
		} else {
			SmallCategory s = smallBase.getOrThrow(id); // 못 찾으면 Base에서 예외

			if (req.getName() != null) {
				// 같은 대분류 내 소분류 이름 중복 체크
				UUID parentId = s.getBigCategory() != null ? s.getBigCategory().getId() : null;
				boolean dup = smallBase.findAll().stream()
					.anyMatch(x -> !x.getId().equals(s.getId())
						&& x.getBigCategory() != null
						&& Objects.equals(parentId, x.getBigCategory().getId())
						&& x.getName().equalsIgnoreCase(req.getName()));
				if (dup)
					throw new BusinessException(AdminErrorCode.CATEGORY_DUPLICATE_NAME);
				s.setName(req.getName());
			}

			if (req.getParentId() != null) {
				UUID newParent = parseUUID(req.getParentId(), AdminErrorCode.PARENT_NOT_FOUND);
				BigCategory nb = bigBase.getOrThrow(newParent);
				// 부모 변경 시 중복 이름 체크
				if (req.getName() != null) {
					boolean dup = smallBase.findAll().stream()
						.anyMatch(x -> !x.getId().equals(s.getId())
							&& x.getBigCategory() != null
							&& newParent.equals(x.getBigCategory().getId())
							&& x.getName().equalsIgnoreCase(req.getName()));
					if (dup)
						throw new BusinessException(AdminErrorCode.CATEGORY_DUPLICATE_NAME);
				}
				s.setBigCategory(nb);
			}

			SmallCategory saved = smallBase.save(s);
			return CategoryDataResponse.of(saved);
		}
	}

	@Transactional
	public void deleteBig(UUID id) {
		BigCategory b = bigBase.getOrThrow(id); // 못 찾으면 Base 예외
		if (bigBase.hasChildren(id)) {
			throw new BusinessException(AdminErrorCode.CATEGORY_HAS_CHILDREN);
		}
		bigBase.delete(b);
	}

	@Transactional
	public void deleteSmall(UUID id) {
		SmallCategory s = smallBase.getOrThrow(id); // 못 찾으면 Base 예외
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
