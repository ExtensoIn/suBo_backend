package com.tordoya.subo.transport.repository;

import com.tordoya.subo.transport.model.RoutePattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RoutePatternRepository extends JpaRepository<RoutePattern, UUID> {
    @Query("""
        SELECT rp
        FROM RoutePattern rp
        JOIN FETCH rp.route r
        WHERE rp.active = true
          AND r.active = true
        ORDER BY r.code, rp.name
        """)
    List<RoutePattern> findAllActiveWithRoute();
}