package com.tordoya.subo.gtfs.writer;

import com.tordoya.subo.gtfs.model.*;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

@Component
public class GtfsFeedWriter {
    private static final DateTimeFormatter GTFS_DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;

    public Path write(GtfsFeed feed, Path outputDirectory) {
        try {
            Files.createDirectories(outputDirectory);

            writeAgencies(feed, outputDirectory);
            writeStops(feed, outputDirectory);
            writeRoutes(feed, outputDirectory);
            writeTrips(feed, outputDirectory);
            writeStopTimes(feed, outputDirectory);
            writeCalendars(feed, outputDirectory);
            writeShapes(feed, outputDirectory);
            writeFrequencies(feed, outputDirectory);

            return outputDirectory;
        } catch (IOException exception) {
            throw new UncheckedIOException(
                    "Failed to write GTFS feed",
                    exception
            );
        }
    }

    private void writeAgencies(GtfsFeed feed, Path directory) throws IOException {
        writeFile(
                directory.resolve("agency.txt"),
                "agency_id,agency_name,agency_url,agency_timezone",
                feed.agencies(),
                agency -> csvRow(
                        agency.agencyId(),
                        agency.agencyName(),
                        agency.agencyUrl(),
                        agency.agencyTimezone()
                )
        );
    }

    private void writeStops(GtfsFeed feed, Path directory) throws IOException {
        writeFile(
                directory.resolve("stops.txt"),
                "stop_id,stop_name,stop_lat,stop_lon",
                feed.stops(),
                stop -> csvRow(
                        stop.stopId(),
                        stop.stopName(),
                        stop.latitude(),
                        stop.longitude()
                )
        );
    }

    private void writeRoutes(GtfsFeed feed, Path directory) throws IOException {
        writeFile(
                directory.resolve("routes.txt"),
                "route_id,agency_id,route_short_name,route_long_name,route_type,continuous_pickup,continuous_drop_off",
                feed.routes(),
                route -> csvRow(
                        route.routeId(),
                        route.agencyId(),
                        route.shortName(),
                        route.longName(),
                        route.routeType(),
                        route.continuousPickup(),
                        route.continuousDropOff()
                )
        );
    }

    private void writeTrips(GtfsFeed feed, Path directory) throws IOException {
        writeFile(
                directory.resolve("trips.txt"),
                "route_id,service_id,trip_id,trip_headsign,direction_id,shape_id",
                feed.trips(),
                trip -> csvRow(
                        trip.routeId(),
                        trip.serviceId(),
                        trip.tripId(),
                        trip.headsign(),
                        trip.directionId(),
                        trip.shapeId()
                )
        );
    }

    private void writeStopTimes(GtfsFeed feed, Path directory) throws IOException {
        writeFile(
                directory.resolve("stop_times.txt"),
                "trip_id,arrival_time,departure_time,stop_id,stop_sequence,timepoint",
                feed.stopTimes(),
                stopTime -> csvRow(
                        stopTime.tripId(),
                        stopTime.arrivalTime(),
                        stopTime.departureTime(),
                        stopTime.stopId(),
                        stopTime.stopSequence(),
                        stopTime.timepoint()
                )
        );
    }

    private void writeCalendars(GtfsFeed feed, Path directory) throws IOException {
        writeFile(
                directory.resolve("calendar.txt"),
                "service_id,monday,tuesday,wednesday,thursday,friday,saturday,sunday,start_date,end_date",
                feed.calendars(),
                calendar -> csvRow(
                        calendar.serviceId(),
                        booleanToGtfs(calendar.monday()),
                        booleanToGtfs(calendar.tuesday()),
                        booleanToGtfs(calendar.wednesday()),
                        booleanToGtfs(calendar.thursday()),
                        booleanToGtfs(calendar.friday()),
                        booleanToGtfs(calendar.saturday()),
                        booleanToGtfs(calendar.sunday()),
                        calendar.startDate().format(GTFS_DATE_FORMAT),
                        calendar.endDate().format(GTFS_DATE_FORMAT)
                )
        );
    }

    private void writeShapes(GtfsFeed feed, Path directory) throws IOException {
        writeFile(
                directory.resolve("shapes.txt"),
                "shape_id,shape_pt_lat,shape_pt_lon,shape_pt_sequence",
                feed.shapePoints(),
                point -> csvRow(
                        point.shapeId(),
                        point.latitude(),
                        point.longitude(),
                        point.sequence()
                )
        );
    }

    private void writeFrequencies(GtfsFeed feed, Path directory) throws IOException {
        Path frequencyFile = directory.resolve("frequencies.txt");
        if (feed.frequencies().isEmpty()) {
            Files.deleteIfExists(frequencyFile);
            return;
        }
        writeFile(
                frequencyFile,
                "trip_id,start_time,end_time,headway_secs,exact_times",
                feed.frequencies(),
                frequency -> csvRow(
                        frequency.tripId(),
                        frequency.startTime(),
                        frequency.endTime(),
                        frequency.headwaySeconds(),
                        frequency.exactTimes()
                )
        );
    }

    private <T> void writeFile(Path file, String header, Iterable<T> rows, Function<T, String> rowMapper) throws IOException {
        try (
                BufferedWriter writer =
                        Files.newBufferedWriter(
                                file,
                                StandardCharsets.UTF_8,
                                StandardOpenOption.CREATE,
                                StandardOpenOption.TRUNCATE_EXISTING,
                                StandardOpenOption.WRITE
                        )
        ) {
            writer.write(header);
            writer.newLine();
            for (T row : rows) {
                writer.write(rowMapper.apply(row));
                writer.newLine();
            }
        }
    }

    private String csvRow(Object... values) {
        StringBuilder builder = new StringBuilder();
        builder.append(values[0]);
        for (int i = 1; i < values.length; i++) {
            Object value = values[i];
            if (value != null) {
                builder.append(',');
                builder.append(escapeCsv(value.toString()));
            }
        }
        return builder.toString();
    }

    private String escapeCsv(String value) {
        if (value.contains("\n") || value.contains("\r") || value.contains("\t")) {
            throw new IllegalArgumentException("GTFS field values cannot contain newlines, carriage returns, or tabs");
        }
        boolean requiresQuotes = value.contains(",") || value.contains("\"");
        if (!requiresQuotes) {
            return value;
        }
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    private int booleanToGtfs(boolean value) {
        return value ? 1 : 0;
    }
}