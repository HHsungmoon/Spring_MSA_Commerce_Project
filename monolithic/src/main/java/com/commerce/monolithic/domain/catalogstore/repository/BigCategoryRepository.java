package com.commerce.monolithic.domain.catalogstore.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.commerce.monolithic.autotime.SoftDeleteRepository;
import com.commerce.monolithic.catalogstore.entity.BigCategory;

@Repository
public interface BigCategoryRepository extends SoftDeleteRepository<BigCategory, UUID> {
}
