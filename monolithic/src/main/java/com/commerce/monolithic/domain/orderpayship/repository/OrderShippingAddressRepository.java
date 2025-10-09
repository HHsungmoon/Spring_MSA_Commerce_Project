package com.commerce.monolithic.domain.orderpayship.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.commerce.monolithic.autotime.SoftDeleteRepository;
import com.commerce.monolithic.orderpayship.entity.OrderShippingAddress;

@Repository
public interface OrderShippingAddressRepository extends SoftDeleteRepository<OrderShippingAddress, UUID> {
}
