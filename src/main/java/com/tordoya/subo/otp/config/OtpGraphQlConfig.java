package com.tordoya.subo.otp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class OtpGraphQlConfig {

    @Bean
    public HttpSyncGraphQlClient otpGraphQlClient(OtpProperties properties) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        RestClient restClient = RestClient.builder()
                .baseUrl(properties.baseUrl() + "/otp/gtfs/v1")
                .requestFactory(new BufferingClientHttpRequestFactory(requestFactory))
                .build();

        return HttpSyncGraphQlClient.create(restClient);
    }
}