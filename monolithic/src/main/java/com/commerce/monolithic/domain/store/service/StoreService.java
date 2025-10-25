package com.commerce.monolithic.domain.store.service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.monolithic.autoresponse.error.BusinessException;
import com.commerce.monolithic.domain.store.dto.StoreDetailResponse;
import com.commerce.monolithic.domain.store.dto.StoreListResponse;
import com.commerce.monolithic.domain.store.entity.Manager;
import com.commerce.monolithic.domain.store.entity.Store;
import com.commerce.monolithic.domain.store.repository.ManagerRepository;
import com.commerce.monolithic.domain.store.response.StoreErrorCode;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StoreService {

	private final StoreBase storeBase;
	private final ManagerRepository managerRepository;

	/** 1) 고객 스토어 목록 조회: 전체 등록된 매장 (id, name, status) */
	@Transactional(readOnly = true)
	public List<StoreListResponse> getAllStores() {
		return storeBase.findAll().stream()
			.sorted(Comparator.comparing(Store::getName, String.CASE_INSENSITIVE_ORDER))
			.map(StoreListResponse::form)
			.collect(Collectors.toList());
	}

	/** 2) 고객 스토어 상세 조회 */
	@Transactional(readOnly = true)
	public StoreDetailResponse getStoreDetail(UUID storeId) {
		Store store = storeBase.getOrThrow(storeId);
		return StoreDetailResponse.form(store);
	}

	/** 5) 매니저 스토어 목록 조회(본인) */
	@Transactional(readOnly = true)
	public List<StoreListResponse> getMyStores(UUID managerId) {
		Manager m = managerRepository.findById(managerId)
			.orElseThrow(() -> new BusinessException(StoreErrorCode.MANAGER_NOT_FOUND));

		return storeBase.findByManager(m.getId()).stream()
			.sorted(Comparator.comparing(Store::getName, String.CASE_INSENSITIVE_ORDER))
			.map(StoreListResponse::form)
			.collect(Collectors.toList());
	}
}
