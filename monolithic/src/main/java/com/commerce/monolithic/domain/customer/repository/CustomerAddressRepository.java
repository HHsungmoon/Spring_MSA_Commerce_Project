package com.commerce.monolithic.domain.customer.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.commerce.monolithic.autotime.SoftDeleteRepository;
import com.commerce.monolithic.domain.customer.entity.CustomerAddress;

@Repository
public interface CustomerAddressRepository extends SoftDeleteRepository<CustomerAddress, UUID> {
}
