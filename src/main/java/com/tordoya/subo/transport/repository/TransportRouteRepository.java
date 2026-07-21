package com.tordoya.subo.transport.repository;

import com.tordoya.subo.transport.model.TransportRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransportRouteRepository extends JpaRepository<TransportRoute, UUID> {
    List<TransportRoute> findAllByActiveTrue();

    Optional<TransportRoute> findByCode(String code);
}