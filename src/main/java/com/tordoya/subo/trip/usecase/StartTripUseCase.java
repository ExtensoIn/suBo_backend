package com.tordoya.subo.trip.usecase;

import com.tordoya.subo.trip.dto.request.StartTripRequest;
import com.tordoya.subo.trip.dto.response.StartTripResponse;
import com.tordoya.subo.trip.service.TripService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StartTripUseCase {
    private final TripService tripService;

    public StartTripResponse execute(String deviceId, StartTripRequest request) {
        return tripService.startTrip(deviceId, request);
    }
}