package com.tordoya.subo.trip.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "subo.trip")
public record TripProperties(
        String shareBaseUrl
) {
}