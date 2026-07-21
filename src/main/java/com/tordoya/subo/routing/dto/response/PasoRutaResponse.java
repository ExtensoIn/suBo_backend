package com.tordoya.subo.routing.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tordoya.subo.routing.model.LineaTeleferico;
import com.tordoya.subo.routing.model.ModoTransporte;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PasoRutaResponse(
        String id,
        ModoTransporte mode,
        String instruction,
        LineaTeleferico telefericoLine,
        String fromLabel,
        String toLabel,
        int durationMinutes,
        Integer distanceMeters,
        BigDecimal fareBob,
        List<LatLngResponse> path
) {
}