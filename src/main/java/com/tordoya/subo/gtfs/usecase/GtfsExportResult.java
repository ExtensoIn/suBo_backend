package com.tordoya.subo.gtfs.usecase;

import java.nio.file.Path;

public record GtfsExportResult(
        Path zipPath,
        int routes,
        int trips,
        int stops
) {
}