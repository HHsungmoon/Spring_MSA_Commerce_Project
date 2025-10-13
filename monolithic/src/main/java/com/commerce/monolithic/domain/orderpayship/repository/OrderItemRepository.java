package com.commerce.monolithic.domain.orderpayship.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.commerce.monolithic.autotime.SoftDeleteRepository;
import com.commerce.monolithic.domain.orderpayship.entity.OrderItem;

@Repository
public interface OrderItemRepository extends SoftDeleteRepository<OrderItem, UUID> {
}
