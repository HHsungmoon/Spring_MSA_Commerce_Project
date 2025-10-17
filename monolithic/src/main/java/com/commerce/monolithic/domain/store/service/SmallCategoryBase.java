package com.commerce.monolithic.domain.store.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.monolithic.autoresponse.error.BusinessException;
import com.commerce.monolithic.domain.store.entity.SmallCategory;
import com.commerce.monolithic.domain.store.repository.SmallCategoryRepository;
import com.commerce.monolithic.domain.store.response.CategoryErrorCode;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SmallCategoryBase {

	private final SmallCategoryRepository smallCategoryRepository;

	@Transactional(readOnly = true)
	public List<SmallCategory> findAll() {
		return smallCategoryRepository.findAll();
	}

	@Transactional(readOnly = true)
	public SmallCategory getOrThrow(UUID id) {
		return smallCategoryRepository.findById(id)
			.orElseThrow(() -> new BusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));
	}

	// 성능 필요시 existsByBigCategoryId(...)로 교체
	@Transactional(readOnly = true)
	public boolean hasChildren(UUID bigCategoryId) {
		return smallCategoryRepository.findAll().stream()
			.anyMatch(s -> s.getBigCategory() != null && bigCategoryId.equals(s.getBigCategory().getId()));
	}

	@Transactional
	public SmallCategory save(SmallCategory entity) {
		return smallCategoryRepository.save(entity);
	}

	@Transactional
	public void delete(SmallCategory entity) {
		smallCategoryRepository.delete(entity);
	}
}
