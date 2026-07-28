package com.tordoya.subo.trip.dto.request;

import com.tordoya.subo.routing.dto.response.OpcionRutaResponse;
import com.tordoya.subo.trip.model.ExpirationMode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record StartTripRequest(
        @NotNull @Valid OpcionRutaResponse route,
        @NotNull ExpirationMode expirationMode,
        OffsetDateTime expiresAt
) {
}