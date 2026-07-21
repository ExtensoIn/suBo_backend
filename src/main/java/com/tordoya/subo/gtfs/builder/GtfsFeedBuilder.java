package com.tordoya.subo.gtfs.builder;

import com.tordoya.subo.common.geo.GeoUtils;
import com.tordoya.subo.gtfs.config.GtfsProperties;
import com.tordoya.subo.gtfs.generator.VirtualStop;
import com.tordoya.subo.gtfs.generator.VirtualStopGenerator;
import com.tordoya.subo.gtfs.model.GtfsAgency;
import com.tordoya.subo.gtfs.model.GtfsCalendar;
import com.tordoya.subo.gtfs.model.GtfsFeed;
import com.tordoya.subo.gtfs.model.GtfsFrequency;
import com.tordoya.subo.gtfs.model.GtfsRoute;
import com.tordoya.subo.gtfs.model.GtfsShapePoint;
import com.tordoya.subo.gtfs.model.GtfsStop;
import com.tordoya.subo.gtfs.model.GtfsStopTime;
import com.tordoya.subo.gtfs.model.GtfsTrip;
import com.tordoya.subo.gtfs.service.GtfsTravelTimeService;
import com.tordoya.subo.transport.model.RouteDirection;
import com.tordoya.subo.transport.model.RoutePattern;
import com.tordoya.subo.transport.model.RoutePatternStop;
import com.tordoya.subo.transport.model.ScheduleType;
import com.tordoya.subo.transport.model.ServiceWindow;
import com.tordoya.subo.transport.model.TransportMode;
import com.tordoya.subo.transport.model.TransportRoute;
import com.tordoya.subo.transport.model.TransportStop;
import com.tordoya.subo.transport.repository.RoutePatternRepository;
import com.tordoya.subo.transport.repository.RoutePatternStopRepository;
import com.tordoya.subo.transport.repository.ServiceWindowRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GtfsFeedBuilder {
    private static final DateTimeFormatter GTFS_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final RoutePatternRepository routePatternRepository;
    private final RoutePatternStopRepository routePatternStopRepository;
    private final ServiceWindowRepository serviceWindowRepository;
    private final VirtualStopGenerator virtualStopGenerator;
    private final GtfsTravelTimeService travelTimeService;
    private final GtfsProperties properties;

    @Transactional(readOnly = true)
    public GtfsFeed build() {
        Map<String, GtfsRoute> routes = new LinkedHashMap<>();
        Map<String, GtfsStop> stops = new LinkedHashMap<>();
        List<GtfsTrip> trips = new ArrayList<>();
        List<GtfsStopTime> stopTimes = new ArrayList<>();
        List<GtfsShapePoint> shapePoints = new ArrayList<>();
        List<GtfsCalendar> calendars = new ArrayList<>();
        List<GtfsFrequency> frequencies = new ArrayList<>();
        LocalDate serviceStartDate = LocalDate.now(ZoneId.of(properties.agencyTimezone()));
        LocalDate serviceEndDate = serviceStartDate.plusDays(properties.serviceHorizonDays());
        List<RoutePattern> patterns = routePatternRepository.findAllActiveWithRoute();

        for (RoutePattern pattern : patterns) {
            List<ServiceWindow> serviceWindows =
                    serviceWindowRepository
                            .findAllByRoutePatternIdAndActiveTrueOrderByDayOfWeekAscStartTimeAsc(
                                    pattern.getId()
                            );
            if (serviceWindows.isEmpty()) {
                continue;
            }
            TransportRoute transportRoute = pattern.getRoute();
            String routeId = buildRouteId(transportRoute);
            routes.putIfAbsent(routeId, buildGtfsRoute(transportRoute));
            String shapeId = buildShapeId(pattern);
            addShapePoints(pattern, shapeId, shapePoints);
            StopPlan stopPlan = buildStopPlan(pattern);

            for (StopPosition position : stopPlan.positions()) {
                stops.putIfAbsent(position.stop().stopId(), position.stop());
            }

            int totalTravelSeconds = resolveTravelSeconds(pattern, stopPlan);
            for (ServiceWindow window : serviceWindows) {
                validateServiceWindow(pattern, window);
                String tripId = buildTripId(pattern, window);
                String serviceId = buildServiceId(window);
                trips.add(
                        new GtfsTrip(
                                routeId,
                                serviceId,
                                tripId,
                                pattern.getName(),
                                toDirectionId(pattern.getDirection()),
                                shapeId
                        )
                );

                calendars.add(
                        buildCalendar(
                                serviceId,
                                window.getDayOfWeek(),
                                serviceStartDate,
                                serviceEndDate
                        )
                );

                addStopTimes(
                        pattern,
                        tripId,
                        window,
                        stopPlan,
                        totalTravelSeconds,
                        stopTimes
                );

                frequencies.add(
                        new GtfsFrequency(
                                tripId,
                                formatLocalTime(window.getStartTime()),
                                formatLocalTime(window.getEndTime()),
                                window.getHeadwaySeconds(),
                                toExactTimes(window.getScheduleType())
                        )
                );
            }
        }

        GtfsAgency agency =
                new GtfsAgency(
                        properties.agencyId(),
                        properties.agencyName(),
                        properties.agencyUrl(),
                        properties.agencyTimezone()
                );

        return new GtfsFeed(
                List.of(agency),
                new ArrayList<>(stops.values()),
                new ArrayList<>(routes.values()),
                trips,
                stopTimes,
                shapePoints,
                calendars,
                frequencies
        );
    }

    private StopPlan buildStopPlan(RoutePattern pattern) {
        TransportMode mode = pattern.getRoute().getMode();
        return switch (mode) {
            case MINIBUS, TRUFI -> buildVirtualStopPlan(pattern);
            case PUMAKATARI -> buildPumakatariStopPlan(pattern);
            case TELEFERICO -> buildTelefericoStopPlan(pattern);
        };
    }

    private StopPlan buildVirtualStopPlan(RoutePattern pattern) {
        List<VirtualStop> generatedStops =
                virtualStopGenerator.generate(
                        pattern.getId(),
                        pattern.getShape(),
                        properties.virtualStopSpacingMeters()
                );

        if (generatedStops.size() < 2) {
            throw new IllegalStateException("Route pattern must generate at least two virtual stops: " + pattern.getId());
        }
        List<StopPosition> positions = getStopPositionsFromVirtualStops(pattern, generatedStops);
        return new StopPlan(List.copyOf(positions), null);
    }

    private static List<StopPosition> getStopPositionsFromVirtualStops(RoutePattern pattern, List<VirtualStop> generatedStops) {
        double totalDistance = generatedStops.getLast().distanceFromStartMeters();

        if (totalDistance <= 0.0) {
            throw new IllegalStateException("Route pattern has zero length: " + pattern.getId());
        }

        List<StopPosition> positions = new ArrayList<>();
        for (VirtualStop virtualStop : generatedStops) {
            double progress = virtualStop.distanceFromStartMeters() / totalDistance;
            positions.add(
                    new StopPosition(
                            virtualStop.stop(),
                            virtualStop.sequence(),
                            progress
                    )
            );
        }
        return positions;
    }

    private StopPlan buildTelefericoStopPlan(
            RoutePattern pattern
    ) {
        List<RoutePatternStop> patternStops = routePatternStopRepository.findOrderedStopsByRoutePatternId(pattern.getId());

        if (patternStops.size() < 2) {
            throw new IllegalStateException("Teleferico pattern must contain at least two stations: " + pattern.getId());
        }

        List<Integer> cumulativeTimes = new ArrayList<>();
        int totalSeconds = 0;
        for (int i = 0; i < patternStops.size(); i++) {
            RoutePatternStop patternStop = patternStops.get(i);
            if (i > 0) {
                Integer secondsFromPrevious = getSecondsFromPrevious(pattern, patternStop);
                totalSeconds += secondsFromPrevious;
            }
            cumulativeTimes.add(totalSeconds);
        }

        if (totalSeconds <= 0) {
            throw new IllegalStateException("Teleferico pattern must have a positive total travel time: " + pattern.getId());
        }

        int completeTravelSeconds = totalSeconds;
        List<StopPosition> positions = new ArrayList<>();

        for (int i = 0; i < patternStops.size(); i++) {
            RoutePatternStop patternStop = patternStops.get(i);
            GtfsStop gtfsStop = toGtfsStop(patternStop.getStop());
            double progress = (double) cumulativeTimes.get(i) / completeTravelSeconds;
            positions.add(
                    new StopPosition(
                            gtfsStop,
                            patternStop.getStopSequence(),
                            progress
                    )
            );
        }

        return new StopPlan(List.copyOf(positions), completeTravelSeconds);
    }

    private static Integer getSecondsFromPrevious(RoutePattern pattern, RoutePatternStop patternStop) {
        Integer secondsFromPrevious = patternStop.getEstimatedSecondsFromPrevious();

        if (secondsFromPrevious == null) {
            throw new IllegalStateException(
                    "Missing travel time for teleferico pattern "
                            + pattern.getId()
                            + " at station sequence "
                            + patternStop.getStopSequence()
            );
        }

        if (secondsFromPrevious < 0) {
            throw new IllegalStateException(
                    "Invalid negative travel time for "
                            + "teleferico pattern "
                            + pattern.getId()
            );
        }
        return secondsFromPrevious;
    }

    private StopPlan buildPumakatariStopPlan(RoutePattern pattern) {
        List<RoutePatternStop> patternStops = routePatternStopRepository.findOrderedStopsByRoutePatternId(pattern.getId());

        if (patternStops.size() < 2) {
            throw new IllegalStateException("PumaKatari pattern must contain at least two stops: " + pattern.getId());
        }
        List<Double> cumulativeDistances = new ArrayList<>();
        double totalDistance = 0.0;
        cumulativeDistances.add(0.0);

        for (int i = 1; i < patternStops.size(); i++) {
            Point previous = patternStops
                    .get(i - 1)
                    .getStop()
                    .getLocation();

            Point current = patternStops
                    .get(i)
                    .getStop()
                    .getLocation();

            totalDistance += GeoUtils.haversineDistance(previous, current);
            cumulativeDistances.add(totalDistance);
        }

        if (totalDistance <= 0.0) {
            throw new IllegalStateException("PumaKatari pattern stops have zero total geographic distance: " + pattern.getId());
        }

        double completeDistance = totalDistance;
        List<StopPosition> positions = new ArrayList<>();

        for (int i = 0; i < patternStops.size(); i++) {
            RoutePatternStop patternStop = patternStops.get(i);
            double progress = cumulativeDistances.get(i) / completeDistance;
            positions.add(
                    new StopPosition(
                            toGtfsStop(
                                    patternStop.getStop()
                            ),
                            patternStop.getStopSequence(),
                            progress
                    )
            );
        }

        return new StopPlan(List.copyOf(positions), null);
    }

    private int resolveTravelSeconds(RoutePattern pattern, StopPlan stopPlan) {
        if (stopPlan.actualTravelSeconds() != null) {
            return stopPlan.actualTravelSeconds();
        }

        long estimatedSeconds = travelTimeService.estimateTotalTravelTime(pattern).toSeconds();
        if (estimatedSeconds <= 0) {
            throw new IllegalStateException("Estimated travel time must be positive for pattern " + pattern.getId());
        }

        return Math.toIntExact(estimatedSeconds);
    }

    private void addStopTimes(
            RoutePattern pattern,
            String tripId,
            ServiceWindow window,
            StopPlan stopPlan,
            int totalTravelSeconds,
            List<GtfsStopTime> target
    ) {
        int timepoint = toTimepoint(pattern.getRoute().getMode(), window.getScheduleType());

        for (StopPosition position : stopPlan.positions()) {
            int offsetSeconds = (int) Math.round(position.progress() * totalTravelSeconds);
            String gtfsTime = formatGtfsDuration(offsetSeconds);
            target.add(
                    new GtfsStopTime(
                            tripId,
                            gtfsTime,
                            gtfsTime,
                            position.stop().stopId(),
                            position.sequence(),
                            timepoint
                    )
            );
        }
    }

    private void addShapePoints(RoutePattern pattern, String shapeId, List<GtfsShapePoint> target) {
        if (pattern.getShape() == null || pattern.getShape().isEmpty()) {
            throw new IllegalStateException("Route pattern does not have a valid shape: " + pattern.getId());
        }

        Coordinate[] coordinates = pattern.getShape().getCoordinates();
        for (int i = 0; i < coordinates.length; i++) {
            Coordinate coordinate = coordinates[i];
            target.add(
                    new GtfsShapePoint(
                            shapeId,
                            coordinate.getY(),
                            coordinate.getX(),
                            i + 1
                    )
            );
        }
    }

    private GtfsRoute buildGtfsRoute(TransportRoute route) {
        boolean continuous = route.getMode() == TransportMode.MINIBUS || route.getMode() == TransportMode.TRUFI;

        return new GtfsRoute(
                buildRouteId(route),
                properties.agencyId(),
                route.getShortName(),
                route.getName(),
                toGtfsRouteType(route.getMode()),
                continuous ? 0 : null,
                continuous ? 0 : null
        );
    }

    private GtfsCalendar buildCalendar(String serviceId, int dayOfWeek, LocalDate startDate, LocalDate endDate) {
        if (dayOfWeek < 1 || dayOfWeek > 7) {
            throw new IllegalArgumentException("dayOfWeek must be between 1 and 7");
        }

        return new GtfsCalendar(
                serviceId,
                dayOfWeek == 1,
                dayOfWeek == 2,
                dayOfWeek == 3,
                dayOfWeek == 4,
                dayOfWeek == 5,
                dayOfWeek == 6,
                dayOfWeek == 7,
                startDate,
                endDate
        );
    }

    private void validateServiceWindow(RoutePattern pattern, ServiceWindow window) {
        if (window.getHeadwaySeconds() == null || window.getHeadwaySeconds() <= 0) {
            throw new IllegalStateException(
                    "Service window requires a positive "
                            + "headway_seconds. Pattern: "
                            + pattern.getId()
            );
        }

        if (window.getStartTime() == null || window.getEndTime() == null) {
            throw new IllegalStateException(
                    "Service window requires start and end "
                            + "times. Pattern: "
                            + pattern.getId()
            );
        }
        if (!window.getEndTime().isAfter(window.getStartTime())) {
            throw new IllegalStateException(
                    "Overnight or invalid service window is "
                            + "not supported yet. Pattern: "
                            + pattern.getId()
            );
        }

        if (window.getScheduleType() == null) {
            throw new IllegalStateException(
                    "Service window requires a schedule_type. "
                            + "Pattern: "
                            + pattern.getId()
            );
        }
    }

    private GtfsStop toGtfsStop(TransportStop transportStop) {
        Point location = transportStop.getLocation();
        if (location == null || location.isEmpty()) {
            throw new IllegalStateException(
                    "Transport stop does not have a valid "
                            + "location: "
                            + transportStop.getId()
            );
        }

        return new GtfsStop(
                buildRealStopId(transportStop),
                transportStop.getName(),
                location.getY(),
                location.getX()
        );
    }

    private int toGtfsRouteType(TransportMode mode) {
        return switch (mode) {
            case TELEFERICO -> 6;
            case PUMAKATARI,
                 MINIBUS,
                 TRUFI -> 3;
        };
    }

    private Integer toDirectionId(RouteDirection direction) {
        return switch (direction) {
            case OUTBOUND -> 0;
            case INBOUND -> 1;
            case CIRCULAR -> null;
        };
    }

    private int toExactTimes(ScheduleType scheduleType) {
        return switch (scheduleType) {
            case FIXED -> 1;
            case FREQUENCY_BASED,
                 ESTIMATED_FREQUENCY -> 0;
        };
    }

    private int toTimepoint(TransportMode mode, ScheduleType scheduleType) {
        if (mode == TransportMode.TELEFERICO) {
            return 1;
        }
        if (scheduleType == ScheduleType.ESTIMATED_FREQUENCY) {
            return 0;
        }
        return 1;
    }

    private String buildRouteId(TransportRoute route) {
        return "ROUTE_" + route.getCode();
    }

    private String buildShapeId(RoutePattern pattern) {
        return "SHAPE_" + pattern.getId();
    }

    private String buildTripId(RoutePattern pattern, ServiceWindow window) {
        return "TRIP_" + pattern.getId() + "_" + window.getId();
    }

    private String buildServiceId(ServiceWindow window) {
        return "SERVICE_" + window.getId();
    }

    private String buildRealStopId(TransportStop stop) {
        if (stop.getCode() != null && !stop.getCode().isBlank()) {
            return "STOP_" + stop.getCode();
        }
        return "STOP_" + stop.getId();
    }

    private String formatLocalTime(LocalTime time) {
        return time.format(GTFS_TIME_FORMAT);
    }

    private String formatGtfsDuration(int totalSeconds) {
        if (totalSeconds < 0) {
            throw new IllegalArgumentException("totalSeconds cannot be negative");
        }

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format(
                Locale.ROOT,
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds
        );
    }

    private record StopPosition(
            GtfsStop stop,
            int sequence,
            double progress
    ) {
    }

    private record StopPlan(
            List<StopPosition> positions,
            Integer actualTravelSeconds
    ) {
    }
}