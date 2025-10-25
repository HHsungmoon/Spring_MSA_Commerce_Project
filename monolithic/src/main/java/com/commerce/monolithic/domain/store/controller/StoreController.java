package com.commerce.monolithic.domain.store.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.monolithic.autoresponse.ApiResponse;
import com.commerce.monolithic.autoresponse.error.BusinessException;
import com.commerce.monolithic.domain.store.dto.StoreDetailResponse;
import com.commerce.monolithic.domain.store.dto.StoreListResponse;
import com.commerce.monolithic.domain.store.repository.ManagerRepository;
import com.commerce.monolithic.domain.store.response.StoreErrorCode;
import com.commerce.monolithic.domain.store.response.StoreSuccessCode;
import com.commerce.monolithic.domain.store.service.StoreService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/stores")
public class StoreController {

	private final StoreService storeService;
	private final ManagerRepository managerRepository;

	// 1) 고객 스토어 목록 조회: 전체 (id, name, status)
	@Operation(summary = "스토어 목록 (전체)", description = "등록된 모든 매장을 리스트로 반환합니다.")
	@GetMapping
	public ResponseEntity<ApiResponse<List<StoreListResponse>>> getStores() {
		List<StoreListResponse> data = storeService.getAllStores();
		return ApiResponse.success(StoreSuccessCode.STORE_LIST_OK, data);
	}

	// 2) 고객 스토어 상세 조회
	@Operation(summary = "스토어 상세", description = "스토어 ID 기준 상세를 반환합니다.")
	@GetMapping("/{storeId}")
	public ResponseEntity<ApiResponse<StoreDetailResponse>> getStoreDetail(@PathVariable String storeId) {
		final UUID id;
		try {
			id = UUID.fromString(storeId);
		} catch (Exception e) {
			throw new BusinessException(StoreErrorCode.INVALID_STORE_ID);
		}
		StoreDetailResponse data = storeService.getStoreDetail(id);
		return ApiResponse.success(StoreSuccessCode.STORE_DETAIL_OK, data);
	}

	// 5) 매니저 스토어 목록 조회(본인)
	@Operation(summary = "내 스토어 목록(매니저)", description = "로그인한 매니저가 소유한 매장 목록을 반환합니다.")
	@GetMapping("/me")
	public ResponseEntity<ApiResponse<List<StoreListResponse>>> getMyStores(
		@AuthenticationPrincipal UserDetails user
	) {
		String email = user.getUsername();
		UUID managerId = managerRepository.findByEmail(email)
			.map(m -> m.getId())
			.orElseThrow(() -> new BusinessException(StoreErrorCode.MANAGER_NOT_FOUND));

		List<StoreListResponse> data = storeService.getMyStores(managerId);
		return ApiResponse.success(StoreSuccessCode.MANAGER_STORE_LIST_OK, data);
	}
}

