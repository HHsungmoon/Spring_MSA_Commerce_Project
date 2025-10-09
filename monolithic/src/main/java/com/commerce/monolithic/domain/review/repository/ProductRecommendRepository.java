package com.commerce.monolithic.domain.review.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.commerce.monolithic.autotime.SoftDeleteRepository;
import com.commerce.monolithic.review.entity.ProductRecommend;

@Repository
public interface ProductRecommendRepository extends SoftDeleteRepository<ProductRecommend, UUID> {
}
