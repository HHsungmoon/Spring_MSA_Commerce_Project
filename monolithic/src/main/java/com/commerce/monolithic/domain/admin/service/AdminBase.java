package com.commerce.monolithic.domain.admin.service;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.monolithic.autoresponse.error.BusinessException;
import com.commerce.monolithic.domain.admin.entity.Admin;
import com.commerce.monolithic.domain.admin.repository.AdminRepository;
import com.commerce.monolithic.domain.admin.response.AdminErrorCode;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AdminBase {

	private final AdminRepository adminRepository;

	public UUID requireAdminId(UserDetails userDetails) {
		if (userDetails == null || userDetails.getUsername() == null) {
			throw new BusinessException(AdminErrorCode.UNAUTHORIZED);
		}
		try {
			return UUID.fromString(userDetails.getUsername());
		} catch (IllegalArgumentException ex) {
			throw new BusinessException(AdminErrorCode.UNAUTHORIZED);
		}
	}

	@Transactional(readOnly = true)
	public Admin getAdmin(UUID adminId) {
		return adminRepository.findById(adminId)
			.orElseThrow(() -> new BusinessException(AdminErrorCode.ADMIN_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public Admin getAdmin(UserDetails userDetails) {
		UUID adminId = requireAdminId(userDetails);
		return adminRepository.findById(adminId)
			.orElseThrow(() -> new BusinessException(AdminErrorCode.ADMIN_NOT_FOUND));
	}
}
