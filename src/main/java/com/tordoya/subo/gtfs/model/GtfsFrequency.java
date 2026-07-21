package com.tordoya.subo.gtfs.model;

public record GtfsFrequency(
        String tripId,
        String startTime,
        String endTime,
        int headwaySeconds,
        int exactTimes
) {
}