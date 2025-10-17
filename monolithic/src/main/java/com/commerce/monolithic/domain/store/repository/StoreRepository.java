package com.commerce.monolithic.domain.store.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.commerce.monolithic.autotime.SoftDeleteRepository;
import com.commerce.monolithic.domain.store.entity.Store;

@Repository
public interface StoreRepository extends SoftDeleteRepository<Store, UUID> {
}
