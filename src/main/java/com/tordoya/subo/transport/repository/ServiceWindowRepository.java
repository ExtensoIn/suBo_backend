package com.tordoya.subo.transport.repository;

import com.tordoya.subo.transport.model.ServiceWindow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ServiceWindowRepository extends JpaRepository<ServiceWindow, UUID> {
    List<ServiceWindow> findAllByRoutePatternIdAndActiveTrueOrderByDayOfWeekAscStartTimeAsc(UUID routePatternId);
}