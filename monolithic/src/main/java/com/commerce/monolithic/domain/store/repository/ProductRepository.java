package com.commerce.monolithic.domain.store.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.commerce.monolithic.autotime.SoftDeleteRepository;
import com.commerce.monolithic.domain.store.entity.Product;

@Repository
public interface ProductRepository extends SoftDeleteRepository<Product, UUID> {
}
