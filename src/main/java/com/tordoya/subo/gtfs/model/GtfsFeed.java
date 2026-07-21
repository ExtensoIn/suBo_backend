package com.tordoya.subo.gtfs.model;

import java.util.List;

public record GtfsFeed(
        List<GtfsAgency> agencies,
        List<GtfsStop> stops,
        List<GtfsRoute> routes,
        List<GtfsTrip> trips,
        List<GtfsStopTime> stopTimes,
        List<GtfsShapePoint> shapePoints,
        List<GtfsCalendar> calendars,
        List<GtfsFrequency> frequencies
) {
    public GtfsFeed {
        agencies = List.copyOf(agencies);
        stops = List.copyOf(stops);
        routes = List.copyOf(routes);
        trips = List.copyOf(trips);
        stopTimes = List.copyOf(stopTimes);
        shapePoints = List.copyOf(shapePoints);
        calendars = List.copyOf(calendars);
        frequencies = List.copyOf(frequencies);
    }
}