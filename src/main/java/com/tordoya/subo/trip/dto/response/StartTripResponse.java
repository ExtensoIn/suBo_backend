package com.tordoya.subo.trip.dto.response;

import java.util.UUID;

public record StartTripResponse(
        UUID tripId,
        String shareToken,
        String shareUrl,
        String writeToken
) {
}