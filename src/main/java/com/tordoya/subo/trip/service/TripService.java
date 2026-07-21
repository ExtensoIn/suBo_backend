package com.tordoya.subo.trip.service;

import com.tordoya.subo.trip.config.TripProperties;
import com.tordoya.subo.trip.dto.request.StartTripRequest;
import com.tordoya.subo.trip.dto.response.StartTripResponse;
import com.tordoya.subo.trip.model.ExpirationMode;
import com.tordoya.subo.trip.repository.TripRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final TripTokenService tokenService;
    private final TripProperties properties;

    @Transactional
    public StartTripResponse startTrip(String deviceId, StartTripRequest request) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime expiresAt = resolveExpiresAt(request, now);

        tripRepository.ensureDeviceExists(deviceId);

        UUID itineraryId = UUID.randomUUID();
        UUID tripId = UUID.randomUUID();

        tripRepository.insertItinerary(itineraryId, request.route());

        for (int i = 0; i < request.route().steps().size(); i++) {
            tripRepository.insertStep(
                    itineraryId,
                    i,
                    request.route().steps().get(i)
            );
        }

        String shareToken = tokenService.generateToken();
        String writeToken = tokenService.generateToken();

        OffsetDateTime estimatedArrivalAt = now.plusMinutes(
                request.route().totalDurationMinutes()
        );

        tripRepository.insertSharedTrip(
                tripId,
                deviceId,
                itineraryId,
                tokenService.hash(shareToken),
                tokenService.hash(writeToken),
                request.expirationMode().value(),
                estimatedArrivalAt,
                expiresAt
        );

        return new StartTripResponse(
                tripId.toString(),
                shareToken,
                properties.shareBaseUrl() + "/" + shareToken,
                writeToken
        );
    }

    private OffsetDateTime resolveExpiresAt(StartTripRequest request, OffsetDateTime now) {
        if (request.expirationMode() == ExpirationMode.UNTIL_ARRIVAL) {
            return null;
        }

        if (request.expiresAt() == null) {
            throw new IllegalArgumentException(
                    "expiresAt is required when expirationMode is fixed_time"
            );
        }

        if (!request.expiresAt().isAfter(now)) {
            throw new IllegalArgumentException(
                    "expiresAt must be in the future"
            );
        }

        return request.expiresAt();
    }
}