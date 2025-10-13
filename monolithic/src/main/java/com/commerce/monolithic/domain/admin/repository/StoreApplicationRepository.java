package com.commerce.monolithic.domain.admin.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.commerce.monolithic.autotime.SoftDeleteRepository;
import com.commerce.monolithic.domain.admin.entity.StoreApplication;

@Repository
public interface StoreApplicationRepository extends SoftDeleteRepository<StoreApplication, UUID> {

}
