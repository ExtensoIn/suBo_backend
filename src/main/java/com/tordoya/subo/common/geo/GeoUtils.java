package com.tordoya.subo.common.geo;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;

public final class GeoUtils {
    private static final double EARTH_RADIUS_METERS = 6_371_000.0;

    private GeoUtils() {}

    public static double haversineDistance(Point first, Point second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException(
                    "Points cannot be null"
            );
        }
        return haversineDistance(
                first.getY(),
                first.getX(),
                second.getY(),
                second.getX()
        );
    }

    public static double haversineDistance(Coordinate first, Coordinate second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException(
                    "Coordinates cannot be null"
            );
        }
        return haversineDistance(
                first.getY(),
                first.getX(),
                second.getY(),
                second.getX()
        );
    }

    public static double haversineDistance(
            double firstLatitude,
            double firstLongitude,
            double secondLatitude,
            double secondLongitude
    ) {
        double lat1 = Math.toRadians(firstLatitude);
        double lat2 = Math.toRadians(secondLatitude);
        double deltaLat = Math.toRadians(secondLatitude - firstLatitude);
        double deltaLon = Math.toRadians(secondLongitude - firstLongitude);
        double sinLat = Math.sin(deltaLat / 2.0);
        double sinLon = Math.sin(deltaLon / 2.0);
        double a = Math.pow(sinLat, 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(sinLon, 2);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        return EARTH_RADIUS_METERS * c;
    }
}