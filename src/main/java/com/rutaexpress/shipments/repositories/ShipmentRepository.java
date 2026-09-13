package com.rutaexpress.shipments.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rutaexpress.shipments.entities.Shipment;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

}