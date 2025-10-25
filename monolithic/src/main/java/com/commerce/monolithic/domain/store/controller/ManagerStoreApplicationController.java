package com.commerce.monolithic.domain.store.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.monolithic.autoresponse.ApiResponse;
import com.commerce.monolithic.domain.store.dto.ManagerStoreAppCreateRequest;
import com.commerce.monolithic.domain.store.response.ManagerErrorCode;
import com.commerce.monolithic.domain.store.response.ManagerSuccessCode;
import com.commerce.monolithic.domain.store.service.ManagerStoreApplicationService;
import com.commerce.monolithic.domain.store.service.StoreBase;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/manager/store-applications")
@AllArgsConstructor
public class ManagerStoreApplicationController {

	private final ManagerStoreApplicationService service;
	private final StoreBase storeBase;

	@Operation(summary = "스토어 신청 생성", description = "신규 생성 또는 기존 스토어 수정(소개문구/대분류 변경) 신청을 생성합니다.")
	@PostMapping
	public ResponseEntity<ApiResponse<UUID>> create(
		@AuthenticationPrincipal UserDetails user,
		@RequestBody ManagerStoreAppCreateRequest req
	) {
		UUID managerId = storeBase.requireManagerId(user);
		UUID appId = service.create(managerId, req);
		return ApiResponse.success(ManagerSuccessCode.APP_CREATE_OK, appId);
	}

	@Operation(summary = "스토어 신청 취소", description = "본인 소유 + PENDING 상태일 때만 취소(삭제)할 수 있습니다.")
	@DeleteMapping("/{applicationId}")
	public ResponseEntity<ApiResponse<Void>> withdraw(
		@AuthenticationPrincipal UserDetails user,
		@PathVariable String applicationId
	) {
		UUID managerId = storeBase.requireManagerId(user);
		UUID appId = storeBase.parseUUID(applicationId, ManagerErrorCode.APPLICATION_NOT_FOUND);
		service.withdraw(managerId, appId);
		return ApiResponse.success(ManagerSuccessCode.APP_WITHDRAW_OK, null);
	}
}
