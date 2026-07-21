package com.tordoya.subo.gtfs.usecase;

import com.tordoya.subo.gtfs.builder.GtfsFeedBuilder;
import com.tordoya.subo.gtfs.config.GtfsProperties;
import com.tordoya.subo.gtfs.model.GtfsFeed;
import com.tordoya.subo.gtfs.service.GtfsZipService;
import com.tordoya.subo.gtfs.writer.GtfsFeedWriter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@AllArgsConstructor
public class ExportGtfsUseCase {
    private final GtfsFeedBuilder feedBuilder;
    private final GtfsFeedWriter feedWriter;
    private final GtfsZipService zipService;
    private final GtfsProperties properties;

    public GtfsExportResult execute() {
        GtfsFeed feed = feedBuilder.build();
        validateFeed(feed);

        Path workingDirectory = Path.of(properties.export().workingDirectory()).toAbsolutePath().normalize();
        Path zipPath = Path.of(properties.export().zipPath()).toAbsolutePath().normalize();

        feedWriter.write(feed, workingDirectory);

        Path generatedZip = zipService.createZip(workingDirectory, zipPath);

        return new GtfsExportResult(
                generatedZip,
                feed.routes().size(),
                feed.trips().size(),
                feed.stops().size()
        );
    }

    private void validateFeed(
            GtfsFeed feed
    ) {
        if (feed.routes().isEmpty()) {
            throw new IllegalStateException("Cannot export GTFS: no active routes were generated");
        }

        if (feed.trips().isEmpty()) {
            throw new IllegalStateException("Cannot export GTFS: no trips were generated");
        }

        if (feed.stops().size() < 2) {
            throw new IllegalStateException("Cannot export GTFS: at least two stops are required");
        }

        if (feed.stopTimes().isEmpty()) {
            throw new IllegalStateException("Cannot export GTFS: no stop times were generated");
        }
    }
}