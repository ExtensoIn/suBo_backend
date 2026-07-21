package com.tordoya.subo.otp.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "subo.otp")
public record OtpProperties(
        @NotBlank
        String baseUrl
) {
}