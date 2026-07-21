package com.tordoya.subo.routing.usecase;

import com.tordoya.subo.routing.dto.request.BusquedaRequest;
import com.tordoya.subo.routing.dto.response.BusquedaResponse;
import com.tordoya.subo.routing.service.RoutingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SearchRoutesUseCase {
    private final RoutingService routingService;

    public BusquedaResponse execute(BusquedaRequest request) {
        return routingService.search(request);
    }
}