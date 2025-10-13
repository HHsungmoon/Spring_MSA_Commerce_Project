package com.commerce.monolithic.domain.admin.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.commerce.monolithic.autotime.SoftDeleteRepository;
import com.commerce.monolithic.domain.admin.entity.Admin;

@Repository
public interface AdminRepository extends SoftDeleteRepository<Admin, UUID> {

	Optional<Admin> findByEmail(String email);
}
