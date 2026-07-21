package com.tordoya.subo.gtfs.generator;

import com.tordoya.subo.common.geo.GeoUtils;
import com.tordoya.subo.gtfs.model.GtfsStop;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class VirtualStopGenerator {
    public List<VirtualStop> generate(UUID routePatternId, LineString shape, double spacingMeters) {
        if (routePatternId == null) {
            throw new IllegalArgumentException("routePatternId cannot be null");
        }
        if (shape == null || shape.isEmpty() || shape.getNumPoints() < 2) {
            throw new IllegalArgumentException("Route pattern shape must contain at least two points");
        }
        if (spacingMeters <= 0) {
            throw new IllegalArgumentException("spacingMeters must be greater than zero");
        }
        Coordinate[] coordinates = shape.getCoordinates();
        double[] cumulativeDistances = calculateCumulativeDistances(coordinates);
        double totalLength = cumulativeDistances[cumulativeDistances.length - 1];

        List<VirtualStop> stops = new ArrayList<>();

        int sequence = 1;

        stops.add(createVirtualStop(
                routePatternId,
                sequence++,
                coordinates[0],
                0.0
        ));

        double targetDistance = spacingMeters;
        while (targetDistance < totalLength) {
            Coordinate point = interpolateAtDistance(
                    coordinates,
                    cumulativeDistances,
                    targetDistance
            );

            stops.add(createVirtualStop(
                    routePatternId,
                    sequence++,
                    point,
                    targetDistance
            ));

            targetDistance += spacingMeters;
        }

        Coordinate end = coordinates[coordinates.length - 1];
        stops.add(createVirtualStop(
                routePatternId,
                sequence,
                end,
                totalLength
        ));

        return List.copyOf(stops);
    }

    private VirtualStop createVirtualStop(UUID routePatternId, int sequence, Coordinate coordinate, double distanceFromStartMeters) {
        String stopId = "VSTOP_" + routePatternId + "_" + sequence;
        GtfsStop gtfsStop = new GtfsStop(
                stopId,
                "Virtual Stop " + sequence,
                coordinate.getY(),
                coordinate.getX()
        );
        return new VirtualStop(
                gtfsStop,
                sequence,
                distanceFromStartMeters
        );
    }

    private double[] calculateCumulativeDistances(Coordinate[] coordinates) {
        double[] distances = new double[coordinates.length];
        distances[0] = 0.0;
        for (int i = 1; i < coordinates.length; i++) {
            double distance = GeoUtils.haversineDistance(coordinates[i - 1], coordinates[i]);
            distances[i] = distances[i - 1] + distance;
        }

        return distances;
    }

    private Coordinate interpolateAtDistance(Coordinate[] coordinates, double[] cumulativeDistances, double targetDistance) {
        for (int i = 1; i < coordinates.length; i++) {
            if (targetDistance <= cumulativeDistances[i]) {
                double segmentStartDistance = cumulativeDistances[i - 1];
                double segmentLength = cumulativeDistances[i] - segmentStartDistance;
                if (segmentLength == 0.0) {
                    return new Coordinate(coordinates[i]);
                }
                double fraction = (targetDistance - segmentStartDistance) / segmentLength;

                Coordinate start = coordinates[i - 1];
                Coordinate end = coordinates[i];

                double longitude = start.getX() + fraction * (end.getX() - start.getX());
                double latitude = start.getY() + fraction * (end.getY() - start.getY());

                return new Coordinate(longitude, latitude);
            }
        }
        return new Coordinate(coordinates[coordinates.length - 1]);
    }
}