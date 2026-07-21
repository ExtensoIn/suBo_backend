package com.tordoya.subo.gtfs.model;

public record GtfsStopTime(
        String tripId,
        String arrivalTime,
        String departureTime,
        String stopId,
        int stopSequence,
        Integer timepoint
) {
}