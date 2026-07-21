package com.tordoya.subo.routing.dto.request;

import com.tordoya.subo.routing.model.ModoTransporte;
import com.tordoya.subo.routing.model.PreferenciaRuta;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record BusquedaRequest(
        @NotNull @Valid Punto origen,
        @NotNull @Valid Punto destino,
        @NotEmpty Set<ModoTransporte> modos,
        @NotNull PreferenciaRuta preferencia
) {
    public record Punto(
            @NotNull @Valid LatLng coordinate,
            String placeId
    ) {
    }

    public record LatLng(
            double latitude,
            double longitude
    ) {
    }
}