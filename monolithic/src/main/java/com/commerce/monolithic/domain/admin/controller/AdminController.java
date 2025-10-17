package com.commerce.monolithic.domain.admin.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.monolithic.autoresponse.ApiResponse;
import com.commerce.monolithic.domain.admin.dto.AdminDataResponse;
import com.commerce.monolithic.domain.admin.response.AdminSuccessCode;
import com.commerce.monolithic.domain.admin.service.AdminBase;
import com.commerce.monolithic.domain.admin.service.AdminService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin")
@Tag(name = "2-1. Admin API", description = "관리자만 사용하는 API")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	private final AdminService adminService;
	private final AdminBase adminBase;

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<AdminDataResponse>> getMe(@AuthenticationPrincipal UserDetails userDetails) {
		UUID adminId = adminBase.requireAdminId(userDetails);
		AdminDataResponse res = adminService.getMe(adminId);
		return ApiResponse.success(AdminSuccessCode.INFO_OK, res);
	}
}
