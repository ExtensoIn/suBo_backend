package com.tordoya.subo.gtfs.model;

public record GtfsAgency(
        String agencyId,
        String agencyName,
        String agencyUrl,
        String agencyTimezone
) {
}