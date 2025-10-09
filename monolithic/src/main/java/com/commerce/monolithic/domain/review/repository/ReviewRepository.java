package com.commerce.monolithic.domain.review.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.commerce.monolithic.autotime.SoftDeleteRepository;
import com.commerce.monolithic.review.entity.Review;

@Repository
public interface ReviewRepository extends SoftDeleteRepository<Review, UUID> {
}
