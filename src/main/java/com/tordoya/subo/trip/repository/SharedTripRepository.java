package com.tordoya.subo.trip.repository;

import com.tordoya.subo.trip.model.SharedTrip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SharedTripRepository extends JpaRepository<SharedTrip, UUID> {
}