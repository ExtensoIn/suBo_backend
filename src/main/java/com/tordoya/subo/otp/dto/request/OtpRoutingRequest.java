package com.tordoya.subo.otp.dto.request;

import com.tordoya.subo.otp.model.OtpTransitMode;

import java.time.OffsetDateTime;
import java.util.Set;

public record OtpRoutingRequest(
        double originLatitude,
        double originLongitude,
        double destinationLatitude,
        double destinationLongitude,
        OffsetDateTime earliestDeparture,
        Set<OtpTransitMode> transitModes,
        boolean transitOnly,
        double walkReluctance,
        int maxResults
) {

    public OtpRoutingRequest {
        if (transitModes == null) {
            throw new IllegalArgumentException("transitModes cannot be null");
        }
        transitModes = Set.copyOf(transitModes);
    }
}