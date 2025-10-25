package com.commerce.monolithic.domain.store.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.monolithic.autoresponse.error.BusinessException;
import com.commerce.monolithic.domain.store.entity.BigCategory;
import com.commerce.monolithic.domain.store.repository.BigCategoryRepository;
import com.commerce.monolithic.domain.store.repository.SmallCategoryRepository;
import com.commerce.monolithic.domain.store.response.CategoryErrorCode;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class BigCategoryBase {

	private final BigCategoryRepository bigCategoryRepository;
	private final SmallCategoryRepository smallCategoryRepository;

	@Transactional(readOnly = true)
	public List<BigCategory> findAll() {
		return bigCategoryRepository.findAll();
	}

	@Transactional(readOnly = true)
	public BigCategory getOrThrow(UUID id) {
		return bigCategoryRepository.findById(id)
			.orElseThrow(() -> new BusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));
	}

	@Transactional
	public BigCategory save(BigCategory entity) {
		return bigCategoryRepository.save(entity);
	}

	@Transactional
	public void delete(BigCategory entity) {
		bigCategoryRepository.delete(entity);
	}

	@Transactional(readOnly = true)
	public boolean hasChildren(UUID bigCategoryId) {
		return smallCategoryRepository.findAll().stream()
			.anyMatch(s -> s.getBigCategory() != null
				&& bigCategoryId.equals(s.getBigCategory().getId()));
	}
}
