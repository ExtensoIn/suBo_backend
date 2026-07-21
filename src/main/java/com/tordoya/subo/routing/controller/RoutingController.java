package com.tordoya.subo.routing.controller;

import com.tordoya.subo.routing.dto.request.BusquedaRequest;
import com.tordoya.subo.routing.dto.response.BusquedaResponse;
import com.tordoya.subo.routing.usecase.SearchRoutesUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/busquedas")
public class RoutingController {

    private final SearchRoutesUseCase searchRoutesUseCase;

    public RoutingController(SearchRoutesUseCase searchRoutesUseCase) {
        this.searchRoutesUseCase = searchRoutesUseCase;
    }

    @PostMapping
    public ResponseEntity<BusquedaResponse> search(
            @RequestHeader("X-Dispositivo-Id") String deviceId,
            @Valid @RequestBody BusquedaRequest request
    ) {
        return ResponseEntity.ok(searchRoutesUseCase.execute(request));
    }
}