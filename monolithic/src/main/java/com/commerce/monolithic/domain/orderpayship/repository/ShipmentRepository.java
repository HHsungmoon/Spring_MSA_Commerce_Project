package com.commerce.monolithic.domain.orderpayship.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.commerce.monolithic.autotime.SoftDeleteRepository;
import com.commerce.monolithic.domain.orderpayship.entity.Shipment;

@Repository
public interface ShipmentRepository extends SoftDeleteRepository<Shipment, UUID> {
}
