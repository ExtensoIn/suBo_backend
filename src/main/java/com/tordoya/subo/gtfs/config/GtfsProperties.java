package com.tordoya.subo.gtfs.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "subo.gtfs")
public record GtfsProperties(
        @NotBlank
        String agencyId,
        @NotBlank
        String agencyName,
        @NotBlank
        String agencyUrl,
        @NotBlank
        String agencyTimezone,
        @Positive
        double virtualStopSpacingMeters,
        @Min(1)
        int serviceHorizonDays,
        @Valid
        TemporalDefault temporalDefault,
        @Valid
        ExportSettings export
) {
    public record TemporalDefault(
            @Min(1)
            int minibusTravelMinutes,
            @Min(1)
            int trufiTravelMinutes,
            @Min(1)
            int pumakatariTravelMinutes
    ) {
    }

    public record ExportSettings(
            @NotBlank
            String workingDirectory,
            @NotBlank
            String zipPath
    ) {
    }
}