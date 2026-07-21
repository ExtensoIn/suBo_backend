package com.tordoya.subo.transport.repository;

import com.tordoya.subo.transport.model.TransportStop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransportStopRepository extends JpaRepository<TransportStop, UUID> {
    List<TransportStop> findAllByActiveTrue();
}