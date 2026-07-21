package com.tordoya.subo.gtfs.model;

public record GtfsTrip(
        String routeId,
        String serviceId,
        String tripId,
        String headsign,
        Integer directionId,
        String shapeId
) {
}