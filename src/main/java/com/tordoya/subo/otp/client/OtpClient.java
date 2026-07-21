package com.tordoya.subo.otp.client;

import com.tordoya.subo.otp.dto.request.OtpRoutingRequest;
import com.tordoya.subo.otp.dto.response.OtpPlanResponse;
import com.tordoya.subo.otp.exception.OtpRoutingException;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;
import java.util.Map;

@Component
public class OtpClient {
    private final HttpSyncGraphQlClient graphQlClient;

    public OtpClient(HttpSyncGraphQlClient graphQlClient) {
        this.graphQlClient = graphQlClient;
    }

    public OtpPlanResponse.PlanConnection plan(OtpRoutingRequest request) {
        try {
            OtpPlanResponse.PlanConnection response = graphQlClient
                    .documentName("planConnection")
                    .variables(buildVariables(request))
                    .retrieveSync("planConnection")
                    .toEntity(OtpPlanResponse.PlanConnection.class);

            if (response == null) {
                throw new OtpRoutingException("OTP returned an empty planConnection");
            }

            return response;
        } catch (HttpStatusCodeException exception) {
            throw new OtpRoutingException(
                    "OTP returned HTTP " + exception.getStatusCode()
                            + ": " + exception.getResponseBodyAsString(),
                    exception
            );
        } catch (OtpRoutingException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new OtpRoutingException("Failed to execute OTP routing request", exception);
        }
    }

    private Map<String, Object> buildVariables(OtpRoutingRequest request) {
        return Map.of(
                "origin", buildLocation(request.originLatitude(), request.originLongitude()),
                "destination", buildLocation(request.destinationLatitude(), request.destinationLongitude()),
                "dateTime", Map.of(
                        "earliestDeparture", request.earliestDeparture().toString()
                ),
                "modes", buildModes(request),
                "preferences", Map.of(
                        "street", Map.of(
                                "walk", Map.of(
                                        "reluctance", request.walkReluctance()
                                )
                        )
                ),
                "first", request.maxResults()
        );
    }

    private Map<String, Object> buildLocation(double latitude, double longitude) {
        return Map.of(
                "location", Map.of(
                        "coordinate", Map.of(
                                "latitude", latitude,
                                "longitude", longitude
                        )
                )
        );
    }

    private Map<String, Object> buildModes(OtpRoutingRequest request) {
        if (request.transitModes().isEmpty()) {
            return Map.of("transitOnly", false);
        }
        List<Map<String, String>> transitModes = request.transitModes()
                .stream()
                .map(mode -> Map.of("mode", mode.name()))
                .toList();
        return Map.of(
                "transitOnly", request.transitOnly(),
                "transit", Map.of("transit", transitModes)
        );
    }
}