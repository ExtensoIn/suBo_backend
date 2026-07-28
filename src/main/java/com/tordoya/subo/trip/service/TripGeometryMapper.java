package com.tordoya.subo.trip.service;

import com.tordoya.subo.routing.dto.response.LatLngResponse;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TripGeometryMapper {

    private final GeometryFactory geometryFactory =
            new GeometryFactory(new PrecisionModel(), 4326);

    public LineString toLineString(List<LatLngResponse> path) {
        List<LatLngResponse> cleanedPath = removeConsecutiveDuplicates(path);

        if (cleanedPath.size() < 2) {
            throw new IllegalArgumentException(
                    "A route step path must contain at least two different points"
            );
        }

        Coordinate[] coordinates = cleanedPath.stream()
                .map(point -> new Coordinate(point.longitude(), point.latitude()))
                .toArray(Coordinate[]::new);

        LineString lineString = geometryFactory.createLineString(coordinates);
        lineString.setSRID(4326);
        return lineString;
    }

    private List<LatLngResponse> removeConsecutiveDuplicates(List<LatLngResponse> path) {
        if (path == null || path.isEmpty()) {
            return List.of();
        }

        List<LatLngResponse> result = new ArrayList<>();

        for (LatLngResponse point : path) {
            if (result.isEmpty() || !samePosition(result.getLast(), point)) {
                result.add(point);
            }
        }

        return result;
    }

    private boolean samePosition(LatLngResponse first, LatLngResponse second) {
        return Double.compare(first.latitude(), second.latitude()) == 0
                && Double.compare(first.longitude(), second.longitude()) == 0;
    }
}