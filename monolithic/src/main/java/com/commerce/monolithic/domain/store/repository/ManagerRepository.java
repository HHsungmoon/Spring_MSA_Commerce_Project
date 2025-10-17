package com.commerce.monolithic.domain.store.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.commerce.monolithic.autotime.SoftDeleteRepository;
import com.commerce.monolithic.domain.store.entity.Manager;

@Repository
public interface ManagerRepository extends SoftDeleteRepository<Manager, UUID> {
	Optional<Manager> findByEmail(String email);
}
