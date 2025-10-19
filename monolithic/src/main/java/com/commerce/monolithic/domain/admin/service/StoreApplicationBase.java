package com.commerce.monolithic.domain.admin.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.monolithic.autoresponse.error.BusinessException;
import com.commerce.monolithic.configenum.GlobalEnum.StoreStatus;
import com.commerce.monolithic.domain.admin.entity.StoreApplication;
import com.commerce.monolithic.domain.admin.repository.StoreApplicationRepository;
import com.commerce.monolithic.domain.admin.response.StoreApplicationErrorCode;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class StoreApplicationBase {

	private final StoreApplicationRepository repository;

	@Transactional(readOnly = true)
	public List<StoreApplication> findAll() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public StoreApplication getStoreApplication(UUID id) {
		return repository.findById(id)
			.orElseThrow(() -> new BusinessException(StoreApplicationErrorCode.APPLICATION_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public StoreApplication getStoreApplicationAssertNotProcessed(UUID id) {
		StoreApplication app = getStoreApplication(id);

		if (app == null)
			throw new BusinessException(StoreApplicationErrorCode.APPLICATION_NOT_FOUND);
		StoreStatus s = app.getStatus();
		if (s == StoreStatus.APPROVED || s == StoreStatus.REJECTED) {
			throw new BusinessException(StoreApplicationErrorCode.ALREADY_PROCESSED);
		}
		return app;
	}

	@Transactional
	public StoreApplication save(StoreApplication entity) {
		return repository.save(entity);
	}
}
