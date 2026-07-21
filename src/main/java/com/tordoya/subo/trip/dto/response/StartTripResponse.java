package com.tordoya.subo.trip.dto.response;

public record StartTripResponse(
        String tripId,
        String shareToken,
        String shareUrl,
        String writeToken
) {
}