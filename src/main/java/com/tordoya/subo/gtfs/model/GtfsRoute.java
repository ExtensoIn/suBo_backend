package com.tordoya.subo.gtfs.model;

public record GtfsRoute(
        String routeId,
        String agencyId,
        String shortName,
        String longName,
        int routeType,
        Integer continuousPickup,
        Integer continuousDropOff
) {
}