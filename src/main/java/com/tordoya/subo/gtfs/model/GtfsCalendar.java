package com.tordoya.subo.gtfs.model;

import java.time.LocalDate;

public record GtfsCalendar(
        String serviceId,
        boolean monday,
        boolean tuesday,
        boolean wednesday,
        boolean thursday,
        boolean friday,
        boolean saturday,
        boolean sunday,
        LocalDate startDate,
        LocalDate endDate
) {
}