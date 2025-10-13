package com.commerce.monolithic.domain.orderpayship.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.commerce.monolithic.autotime.SoftDeleteRepository;
import com.commerce.monolithic.domain.orderpayship.entity.Order;

@Repository
public interface OrderRepository extends SoftDeleteRepository<Order, UUID> {
}
