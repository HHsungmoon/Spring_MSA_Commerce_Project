package com.commerce.monolithic.domain.customer.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.commerce.monolithic.autotime.SoftDeleteRepository;
import com.commerce.monolithic.customer.entity.Customer;

@Repository
public interface CustomerRepository extends SoftDeleteRepository<Customer, UUID> {
}
