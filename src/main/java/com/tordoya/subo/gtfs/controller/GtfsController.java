package com.tordoya.subo.gtfs.controller;

import com.tordoya.subo.gtfs.dto.response.GtfsExportResponse;
import com.tordoya.subo.gtfs.usecase.ExportGtfsUseCase;
import com.tordoya.subo.gtfs.usecase.GtfsExportResult;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/gtfs")
@AllArgsConstructor
public class GtfsController {
    private final ExportGtfsUseCase exportGtfsUseCase;

    @PostMapping("/export")
    public ResponseEntity<GtfsExportResponse> export() {
        GtfsExportResult result = exportGtfsUseCase.execute();
        GtfsExportResponse response =
                new GtfsExportResponse(
                        result.zipPath().toString(),
                        result.routes(),
                        result.trips(),
                        result.stops()
                );
        return ResponseEntity.ok(response);
    }
}