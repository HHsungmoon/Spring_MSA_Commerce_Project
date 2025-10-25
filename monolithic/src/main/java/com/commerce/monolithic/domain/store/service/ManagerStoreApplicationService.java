package com.commerce.monolithic.domain.store.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.monolithic.autoresponse.error.BusinessException;
import com.commerce.monolithic.configenum.GlobalEnum.StoreStatus;
import com.commerce.monolithic.domain.admin.entity.StoreApplication;
import com.commerce.monolithic.domain.admin.repository.StoreApplicationRepository;
import com.commerce.monolithic.domain.store.dto.ManagerStoreAppCreateRequest;
import com.commerce.monolithic.domain.store.entity.Manager;
import com.commerce.monolithic.domain.store.entity.Store;
import com.commerce.monolithic.domain.store.repository.BigCategoryRepository;
import com.commerce.monolithic.domain.store.repository.ManagerRepository;
import com.commerce.monolithic.domain.store.repository.StoreRepository;
import com.commerce.monolithic.domain.store.response.ManagerErrorCode;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ManagerStoreApplicationService {

	private final StoreApplicationRepository storeApplicationRepository;
	private final ManagerRepository managerRepository;
	private final StoreRepository storeRepository;
	private final BigCategoryRepository bigCategoryRepository;

	/** 신청 생성: 신규 생성 또는 기존 스토어 수정(소개문구/대분류 변경) */
	@Transactional
	public UUID create(UUID managerId, ManagerStoreAppCreateRequest req) {
		Manager manager = managerRepository.findById(managerId)
			.orElseThrow(() -> new BusinessException(ManagerErrorCode.MANAGER_NOT_FOUND));

		String description = normalizeDescription(req.getDescription());

		// (옵션) 기존 스토어 수정 신청
		Store store = null;
		if (req.getStoreId() != null && !req.getStoreId().isBlank()) {
			UUID storeId = parseUUID(req.getStoreId(), ManagerErrorCode.INVALID_STORE_ID);
			store = storeRepository.findById(storeId)
				.orElseThrow(() -> new BusinessException(ManagerErrorCode.INVALID_STORE_ID));
			// 권한 검사
			if (store.getManager() == null || !managerId.equals(store.getManager().getId())) {
				throw new BusinessException(ManagerErrorCode.FORBIDDEN);
			}
		}

		// (옵션) 대분류 변경 요청 ID 검증 → 엔티티 확장 전까진 description 끝에 주석으로 기록
		if (req.getTargetBigCategoryId() != null && !req.getTargetBigCategoryId().isBlank()) {
			UUID bigId = parseUUID(req.getTargetBigCategoryId(), ManagerErrorCode.INVALID_BIG_CATEGORY_ID);
			bigCategoryRepository.findById(bigId)
				.orElseThrow(() -> new BusinessException(ManagerErrorCode.INVALID_BIG_CATEGORY_ID));
			description = appendBigMeta(description, bigId);
		}

		StoreApplication app = new StoreApplication();
		app.setManager(manager);
		app.setStore(store); // 신규 생성 신청이면 null
		app.setDescription(description);
		app.setStatus(StoreStatus.PENDING);
		app.setRequestedAt(LocalDateTime.now());

		StoreApplication saved = storeApplicationRepository.save(app);
		return saved.getId();
	}

	/** 신청 취소(삭제): 본인 소유 + PENDING 에서만 가능 */
	@Transactional
	public void withdraw(UUID managerId, UUID applicationId) {
		StoreApplication app = storeApplicationRepository.findById(applicationId)
			.orElseThrow(() -> new BusinessException(ManagerErrorCode.APPLICATION_NOT_FOUND));

		if (app.getManager() == null || !managerId.equals(app.getManager().getId())) {
			throw new BusinessException(ManagerErrorCode.APPLICATION_NOT_OWNED);
		}
		if (app.getStatus() != StoreStatus.PENDING) {
			throw new BusinessException(ManagerErrorCode.APPLICATION_ALREADY_PROCESSED);
		}
		storeApplicationRepository.delete(app);
	}

	// ===== utils =====

	private String normalizeDescription(String desc) {
		if (desc == null || desc.isBlank()) {
			throw new BusinessException(ManagerErrorCode.INVALID_DESCRIPTION);
		}
		return desc.trim();
	}

	private UUID parseUUID(String src, ManagerErrorCode onError) {
		try {
			return UUID.fromString(src);
		} catch (Exception e) {
			throw new BusinessException(onError);
		}
	}

	private String appendBigMeta(String desc, UUID bigId) {
		return desc + "  [TARGET_BIG_CATEGORY=" + bigId + "]";
	}
}
