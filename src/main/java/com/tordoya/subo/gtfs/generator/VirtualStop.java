package com.tordoya.subo.gtfs.generator;

import com.tordoya.subo.gtfs.model.GtfsStop;

public record VirtualStop(
        GtfsStop stop,
        int sequence,
        double distanceFromStartMeters
) {
}