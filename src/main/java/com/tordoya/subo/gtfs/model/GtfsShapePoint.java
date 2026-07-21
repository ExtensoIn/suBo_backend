package com.tordoya.subo.gtfs.model;

public record GtfsShapePoint(
        String shapeId,
        double latitude,
        double longitude,
        int sequence
) {
}