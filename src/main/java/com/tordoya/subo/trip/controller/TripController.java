package com.tordoya.subo.trip.controller;

import com.tordoya.subo.trip.dto.request.StartTripRequest;
import com.tordoya.subo.trip.dto.response.StartTripResponse;
import com.tordoya.subo.trip.usecase.StartTripUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/viajes")
public class TripController {

    private final StartTripUseCase startTripUseCase;

    public TripController(StartTripUseCase startTripUseCase) {
        this.startTripUseCase = startTripUseCase;
    }

    @PostMapping
    public ResponseEntity<StartTripResponse> startTrip(
            @RequestHeader("X-Dispositivo-Id") String deviceId,
            @Valid @RequestBody StartTripRequest request
    ) {
        StartTripResponse response = startTripUseCase.execute(deviceId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}