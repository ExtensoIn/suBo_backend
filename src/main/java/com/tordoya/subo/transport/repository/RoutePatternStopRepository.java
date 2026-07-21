package com.tordoya.subo.transport.repository;

import com.tordoya.subo.transport.model.RoutePatternStop;
import com.tordoya.subo.transport.model.RoutePatternStopId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RoutePatternStopRepository extends JpaRepository<RoutePatternStop, RoutePatternStopId> {
    @Query("""
            SELECT rps
            FROM RoutePatternStop rps
            JOIN FETCH rps.stop
            WHERE rps.routePattern.id = :routePatternId
            ORDER BY rps.id.stopSequence
            """)
    List<RoutePatternStop>
    findOrderedStopsByRoutePatternId(@Param("routePatternId") UUID routePatternId);
}