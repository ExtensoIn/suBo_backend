package com.tordoya.subo.gtfs.dto.response;

public record GtfsExportResponse(
        String zipPath,
        int routes,
        int trips,
        int stops
) {
}