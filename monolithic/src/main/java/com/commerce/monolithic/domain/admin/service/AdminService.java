package com.commerce.monolithic.domain.admin.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.monolithic.domain.admin.dto.AdminDataResponse;
import com.commerce.monolithic.domain.admin.entity.Admin;
import com.commerce.monolithic.domain.admin.repository.AdminRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminService {

	private final AdminRepository adminRepository;
	private final AdminBase adminBase;

	@Transactional(readOnly = true)
	public AdminDataResponse getMe(UUID adminId) {
		Admin admin = adminBase.getAdminOrThrow(adminId);
		return AdminDataResponse.from(admin);
	}
}
