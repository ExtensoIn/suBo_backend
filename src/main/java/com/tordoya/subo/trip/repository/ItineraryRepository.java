package com.tordoya.subo.trip.repository;

import com.tordoya.subo.trip.model.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItineraryRepository extends JpaRepository<Itinerary, UUID> {
}