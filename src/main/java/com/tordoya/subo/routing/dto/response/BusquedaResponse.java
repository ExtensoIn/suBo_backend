package com.tordoya.subo.routing.dto.response;

import com.tordoya.subo.routing.model.ModoTransporte;

import java.util.List;

public record BusquedaResponse(
        List<OpcionRutaResponse> opciones,
        List<Object> alertas,
        List<ModoTransporte> sugerencias
) {
}