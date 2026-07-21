package com.tordoya.subo.routing.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record OpcionRutaResponse(
        String id,
        List<String> summary,
        int totalDurationMinutes,
        BigDecimal precioTotalBob,
        int transfers,
        List<PasoRutaResponse> steps,
        List<String> tags
) {
}