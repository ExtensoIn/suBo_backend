package com.tordoya.subo.gtfs.service;

import com.tordoya.subo.gtfs.config.GtfsProperties;
import com.tordoya.subo.transport.model.RoutePattern;
import com.tordoya.subo.transport.model.TransportMode;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class GtfsTravelTimeService {

    private final GtfsProperties properties;

    public GtfsTravelTimeService(
            GtfsProperties properties
    ) {
        this.properties = properties;
    }

    public Duration estimateTotalTravelTime(RoutePattern pattern) {
        TransportMode mode = pattern.getRoute().getMode();
        return switch (mode) {
            case MINIBUS ->
                    Duration.ofMinutes(properties.temporalDefault().minibusTravelMinutes());
            case TRUFI ->
                    Duration.ofMinutes(properties.temporalDefault().trufiTravelMinutes());
            case PUMAKATARI ->
                    Duration.ofMinutes(properties.temporalDefault().pumakatariTravelMinutes());
            case TELEFERICO ->
                    throw new IllegalArgumentException("Teleferico travel time must be calculated from real stop-to-stop travel times");
        };
    }
}