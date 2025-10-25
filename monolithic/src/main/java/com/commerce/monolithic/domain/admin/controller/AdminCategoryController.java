package com.commerce.monolithic.domain.admin.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.monolithic.autoresponse.ApiResponse;
import com.commerce.monolithic.autoresponse.error.BusinessException;
import com.commerce.monolithic.domain.admin.dto.category.CategoryCreateRequest;
import com.commerce.monolithic.domain.admin.dto.category.CategoryDataResponse;
import com.commerce.monolithic.domain.admin.dto.category.CategoryTreeResponse;
import com.commerce.monolithic.domain.admin.dto.category.CategoryUpdateRequest;
import com.commerce.monolithic.domain.admin.response.AdminErrorCode;
import com.commerce.monolithic.domain.admin.response.AdminSuccessCode;
import com.commerce.monolithic.domain.admin.service.AdminBase;
import com.commerce.monolithic.domain.admin.service.AdminCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "2-2. Admin-Category", description = "관리자 카테고리 관리 API")
@RestController
@RequestMapping("/api/v1/admin/categories")
@AllArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {

	private final AdminCategoryService categoryService;
	private final AdminBase adminBase;

	@Operation(summary = "카테고리 트리 조회", description = "대분류/소분류를 트리 형태로 조회합니다.")
	@GetMapping
	public ResponseEntity<ApiResponse<CategoryTreeResponse>> getTree(@AuthenticationPrincipal UserDetails userDetails) {
		UUID adminId = adminBase.requireAdminId(userDetails);
		CategoryTreeResponse res = categoryService.getTree();
		return ApiResponse.success(AdminSuccessCode.CATEGORY_LIST_OK, res);
	}

	@Operation(summary = "카테고리 생성", description = "대분류/소분류 공용 생성 (소분류는 parentId 필수).")
	@PostMapping
	public ResponseEntity<ApiResponse<CategoryDataResponse>> create(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody CategoryCreateRequest req) {
		UUID adminId = adminBase.requireAdminId(userDetails);
		CategoryDataResponse res = categoryService.create(req);
		return ApiResponse.success(AdminSuccessCode.CATEGORY_CREATE_OK, res);
	}

	@Operation(summary = "카테고리 수정", description = "이름/슬러그/부모(소분류)를 수정합니다.")
	@PatchMapping("/{categoryId}")
	public ResponseEntity<ApiResponse<CategoryDataResponse>> update(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable String categoryId,
		@RequestBody CategoryUpdateRequest req) {
		UUID adminId = adminBase.requireAdminId(userDetails);
		CategoryDataResponse res = categoryService.update(adminId, req);
		return ApiResponse.success(AdminSuccessCode.CATEGORY_UPDATE_OK, res);
	}

	@Operation(summary = "카테고리 삭제", description = "대분류는 하위 소분류가 없을 때만 삭제할 수 있습니다.")
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponse<Void>> delete(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable String categoryId,
		@Parameter(description = "대분류 삭제 여부(true: 대분류, false: 소분류)", required = true, example = "true")
		@RequestParam("big") boolean big
	) {
		// 관리자 인증 (미사용이어도 인증 강제 겸 로그 목적)
		UUID adminId = adminBase.requireAdminId(userDetails);

		final UUID id;
		try {
			id = UUID.fromString(categoryId);
		} catch (Exception e) {
			throw new BusinessException(AdminErrorCode.CATEGORY_NOT_FOUND);
		}

		if (big) {
			categoryService.deleteBig(id);
		} else {
			categoryService.deleteSmall(id);
		}

		return ApiResponse.success(AdminSuccessCode.CATEGORY_DELETE_OK, null);
	}
}
