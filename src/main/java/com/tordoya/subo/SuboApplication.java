package com.tordoya.subo;

import com.tordoya.subo.gtfs.config.GtfsProperties;
import com.tordoya.subo.otp.config.OtpProperties;
import com.tordoya.subo.trip.config.TripProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({GtfsProperties.class, OtpProperties.class, TripProperties.class})
public class SuboApplication {
    public static void main(String[] args) {
        SpringApplication.run(SuboApplication.class, args);
    }
}
