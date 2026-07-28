package com.tordoya.subo.trip.service;

import com.tordoya.subo.device.model.AnonymousDevice;
import com.tordoya.subo.device.repository.AnonymousDeviceRepository;
import com.tordoya.subo.routing.dto.response.OpcionRutaResponse;
import com.tordoya.subo.routing.dto.response.PasoRutaResponse;
import com.tordoya.subo.trip.config.TripProperties;
import com.tordoya.subo.trip.dto.request.StartTripRequest;
import com.tordoya.subo.trip.dto.response.StartTripResponse;
import com.tordoya.subo.trip.model.*;
import com.tordoya.subo.trip.repository.ItineraryRepository;
import com.tordoya.subo.trip.repository.SharedTripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class TripService {

    private final AnonymousDeviceRepository deviceRepository;
    private final ItineraryRepository itineraryRepository;
    private final SharedTripRepository sharedTripRepository;
    private final TripTokenService tokenService;
    private final TripGeometryMapper geometryMapper;
    private final TripProperties properties;

    public TripService(
            AnonymousDeviceRepository deviceRepository,
            ItineraryRepository itineraryRepository,
            SharedTripRepository sharedTripRepository,
            TripTokenService tokenService,
            TripGeometryMapper geometryMapper,
            TripProperties properties
    ) {
        this.deviceRepository = deviceRepository;
        this.itineraryRepository = itineraryRepository;
        this.sharedTripRepository = sharedTripRepository;
        this.tokenService = tokenService;
        this.geometryMapper = geometryMapper;
        this.properties = properties;
    }

    @Transactional
    public StartTripResponse startTrip(String deviceId, StartTripRequest request) {
        validateDeviceId(deviceId);
        validateRoute(request.route());

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime expiresAt = resolveExpiresAt(request, now);
        AnonymousDevice anonymousDevice = new AnonymousDevice();
        anonymousDevice.setDeviceId(deviceId);

        AnonymousDevice device = deviceRepository.findById(deviceId)
                .orElseGet(() -> deviceRepository.save(anonymousDevice));

        OpcionRutaResponse route = request.route();

        Itinerary itinerary = new Itinerary(
                route.summary() == null ? List.of() : route.summary(),
                route.totalDurationMinutes(),
                route.precioTotalBob() == null ? BigDecimal.ZERO : route.precioTotalBob(),
                route.transfers(),
                route.tags() == null ? List.of() : route.tags()
        );

        for (int index = 0; index < route.steps().size(); index++) {
            PasoRutaResponse routeStep = route.steps().get(index);

            ItineraryStep step = new ItineraryStep(
                    index,
                    TripMode.fromRoutingMode(routeStep.mode()),
                    routeStep.instruction(),
                    routeStep.fromLabel(),
                    routeStep.toLabel(),
                    routeStep.durationMinutes(),
                    routeStep.distanceMeters(),
                    routeStep.fareBob(),
                    geometryMapper.toLineString(routeStep.path())
            );

            itinerary.addStep(step);
        }

        itinerary = itineraryRepository.save(itinerary);

        String shareToken = tokenService.generateToken();
        String writeToken = tokenService.generateToken();
        OffsetDateTime estimatedArrivalAt =
                now.plusMinutes(Math.max(0, route.totalDurationMinutes()));

        SharedTrip sharedTrip = new SharedTrip(
                device,
                itinerary,
                tokenService.hash(shareToken),
                tokenService.hash(writeToken),
                request.expirationMode(),
                estimatedArrivalAt,
                expiresAt,
                now
        );

        sharedTrip = sharedTripRepository.save(sharedTrip);

        return new StartTripResponse(
                sharedTrip.getId(),
                shareToken,
                buildShareUrl(shareToken),
                writeToken
        );
    }

    private OffsetDateTime resolveExpiresAt(
            StartTripRequest request,
            OffsetDateTime now
    ) {
        if (request.expirationMode() == ExpirationMode.UNTIL_ARRIVAL) {
            if (request.expiresAt() != null) {
                throw new IllegalArgumentException(
                        "expiresAt must be null when expirationMode is until_arrival"
                );
            }

            return null;
        }

        if (request.expiresAt() == null) {
            throw new IllegalArgumentException(
                    "expiresAt is required when expirationMode is fixed_time"
            );
        }

        if (!request.expiresAt().isAfter(now)) {
            throw new IllegalArgumentException("expiresAt must be in the future");
        }

        return request.expiresAt();
    }

    private void validateRoute(OpcionRutaResponse route) {
        if (route.steps() == null || route.steps().isEmpty()) {
            throw new IllegalArgumentException(
                    "The selected route must contain at least one step"
            );
        }
    }

    private void validateDeviceId(String deviceId) {
        if (deviceId == null || deviceId.isBlank()) {
            throw new IllegalArgumentException("X-Dispositivo-Id cannot be blank");
        }
    }

    private String buildShareUrl(String shareToken) {
        String baseUrl = properties.shareBaseUrl();

        while (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        return baseUrl + "/" + shareToken;
    }
}