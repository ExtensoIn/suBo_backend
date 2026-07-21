package com.tordoya.subo.otp.dto.response;

import java.util.List;

public final class OtpPlanResponse {
    private OtpPlanResponse() {
    }

    public record PlanConnection(
            List<RoutingError> routingErrors,
            List<Edge> edges
    ) {
    }

    public record RoutingError(
            String code,
            String description
    ) {
    }

    public record Edge(
            Node node
    ) {
    }

    public record Node(
            String start,
            String end,
            List<Leg> legs
    ) {
    }

    public record Leg(
            String mode,
            Double duration,
            Double distance,
            Place from,
            Place to,
            Route route,
            Geometry legGeometry
    ) {
    }

    public record Place(
            String name,
            Double lat,
            Double lon

    ) {
    }

    public record Route(
            String gtfsId,
            String shortName,
            String longName,
            String mode

    ) {
    }

    public record Geometry(
            String points
    ) {
    }
}