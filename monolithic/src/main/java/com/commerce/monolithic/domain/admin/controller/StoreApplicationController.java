package com.commerce.monolithic.domain.admin.controller;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.monolithic.autoresponse.ApiResponse;
import com.commerce.monolithic.domain.admin.dto.storeapp.StoreApplicationApproveRequest;
import com.commerce.monolithic.domain.admin.dto.storeapp.StoreApplicationDetailResponse;
import com.commerce.monolithic.domain.admin.dto.storeapp.StoreApplicationPageResponse;
import com.commerce.monolithic.domain.admin.dto.storeapp.StoreApplicationRejectRequest;
import com.commerce.monolithic.domain.admin.dto.storeapp.StoreApplicationSearchRequest;
import com.commerce.monolithic.domain.admin.response.StoreApplicationSuccessCode;
import com.commerce.monolithic.domain.admin.service.AdminBase;
import com.commerce.monolithic.domain.admin.service.StoreApplicationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "2-3. Admin-StoreApplication", description = "관리자 매니저 신청 관리 API")
@RestController
@RequestMapping("/api/v1/admin/apply")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class StoreApplicationController {

	private final StoreApplicationService storeApplicationService;
	private final AdminBase adminBase;

	@Operation(summary = "매니저 신청 목록", description = "상태/기간 필터와 페이지네이션으로 신청 목록을 조회합니다.")
	@GetMapping
	public ResponseEntity<ApiResponse<StoreApplicationPageResponse>> list(
		@AuthenticationPrincipal UserDetails userDetails,
		@ParameterObject StoreApplicationSearchRequest req
	) {
		UUID adminId = adminBase.requireAdminId(userDetails);
		StoreApplicationPageResponse res = storeApplicationService.list(req);
		return ApiResponse.success(StoreApplicationSuccessCode.LIST_OK, res);
	}

	@Operation(summary = "매니저 신청 상세", description = "신청서/증빙 상세 정보를 조회합니다.")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<StoreApplicationDetailResponse>> detail(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable UUID id
	) {
		UUID adminId = adminBase.requireAdminId(userDetails);
		StoreApplicationDetailResponse res = storeApplicationService.detail(id);
		return ApiResponse.success(StoreApplicationSuccessCode.DETAIL_OK, res);
	}

	@Operation(summary = "매니저 승인", description = "해당 신청을 승인 처리합니다(스토어 생성 허가).")
	@PostMapping("/{id}/approve")
	public ResponseEntity<ApiResponse<Void>> approve(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable UUID id,
		@RequestBody(required = false) @Valid StoreApplicationApproveRequest req
	) {
		UUID adminId = adminBase.requireAdminId(userDetails);
		storeApplicationService.approve(adminId, id, req);
		return ApiResponse.success(StoreApplicationSuccessCode.APPROVE_OK, null);
	}

	@Operation(summary = "매니저 거절", description = "거절 사유를 받아 해당 신청을 거절 처리합니다.")
	@PostMapping("/{id}/reject")
	public ResponseEntity<ApiResponse<Void>> reject(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable UUID id,
		@RequestBody @Valid StoreApplicationRejectRequest req
	) {
		UUID adminId = adminBase.requireAdminId(userDetails);
		storeApplicationService.reject(adminId, id, req);
		return ApiResponse.success(StoreApplicationSuccessCode.REJECT_OK, null);
	}
}
