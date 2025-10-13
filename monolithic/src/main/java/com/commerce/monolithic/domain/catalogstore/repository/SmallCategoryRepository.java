package com.commerce.monolithic.domain.catalogstore.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.commerce.monolithic.autotime.SoftDeleteRepository;
import com.commerce.monolithic.domain.catalogstore.entity.SmallCategory;

@Repository
public interface SmallCategoryRepository extends SoftDeleteRepository<SmallCategory, UUID> {
}
