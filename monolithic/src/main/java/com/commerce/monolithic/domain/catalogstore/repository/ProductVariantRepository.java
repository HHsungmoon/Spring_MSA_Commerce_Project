package com.commerce.monolithic.domain.catalogstore.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.commerce.monolithic.autotime.SoftDeleteRepository;
import com.commerce.monolithic.catalogstore.entity.ProductVariant;

@Repository
public interface ProductVariantRepository extends SoftDeleteRepository<ProductVariant, UUID> {
}
