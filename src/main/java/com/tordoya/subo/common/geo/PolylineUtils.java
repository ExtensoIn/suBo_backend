package com.tordoya.subo.common.geo;

import java.util.ArrayList;
import java.util.List;

public final class PolylineUtils {

    private PolylineUtils() {
    }

    public static List<GeoPoint> decode(String encoded) {
        if (encoded == null || encoded.isBlank()) {
            return List.of();
        }

        List<GeoPoint> points = new ArrayList<>();
        int index = 0;
        int latitude = 0;
        int longitude = 0;

        while (index < encoded.length()) {
            DecodeResult latResult = decodeValue(encoded, index);
            index = latResult.nextIndex();
            latitude += latResult.value();

            DecodeResult lonResult = decodeValue(encoded, index);
            index = lonResult.nextIndex();
            longitude += lonResult.value();

            points.add(new GeoPoint(latitude / 1e5, longitude / 1e5));
        }

        return List.copyOf(points);
    }

    private static DecodeResult decodeValue(String encoded, int startIndex) {
        int result = 0;
        int shift = 0;
        int index = startIndex;
        int value;

        do {
            value = encoded.charAt(index++) - 63;
            result |= (value & 0x1F) << shift;
            shift += 5;
        } while (value >= 0x20);

        int decoded = (result & 1) != 0 ? ~(result >> 1) : result >> 1;
        return new DecodeResult(decoded, index);
    }

    public record GeoPoint(
            double latitude,
            double longitude
    ) {
    }

    private record DecodeResult(
            int value,
            int nextIndex
    ) {
    }
}