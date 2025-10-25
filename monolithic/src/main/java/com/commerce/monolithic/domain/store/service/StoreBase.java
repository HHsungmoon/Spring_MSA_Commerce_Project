package com.commerce.monolithic.domain.store.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.monolithic.autoresponse.error.BusinessException;
import com.commerce.monolithic.autoresponse.error.ErrorCode;
import com.commerce.monolithic.domain.store.entity.Store;
import com.commerce.monolithic.domain.store.repository.ManagerRepository;
import com.commerce.monolithic.domain.store.repository.StoreRepository;
import com.commerce.monolithic.domain.store.response.ManagerErrorCode;
import com.commerce.monolithic.domain.store.response.StoreErrorCode;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class StoreBase {

	private final StoreRepository storeRepository;
	private final ManagerRepository managerRepository;

	public UUID parseUUID(String src, ErrorCode onError) {
		try {
			return UUID.fromString(src);
		} catch (Exception e) {
			throw new BusinessException(onError);
		}
	}

	@Transactional(readOnly = true)
	public List<Store> findAll() {
		// SoftDeleteRepository 기반: 삭제되지 않은 데이터만 반환됨(@SQLRestriction)
		return storeRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Store getOrThrow(UUID storeId) {
		return storeRepository.findById(storeId)
			.orElseThrow(() -> new BusinessException(StoreErrorCode.STORE_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public List<Store> findByManager(UUID managerId) {
		return storeRepository.findByManager_Id(managerId);
	}

	@Transactional(readOnly = true)
	public UUID requireManagerId(UserDetails user) {
		return requireManagerId(user.getUsername());
	}

	@Transactional(readOnly = true)
	public UUID requireManagerId(String email) {
		return managerRepository.findByEmail(email)
			.map(m -> m.getId())
			.orElseThrow(() -> new BusinessException(ManagerErrorCode.MANAGER_NOT_FOUND));
	}

}
