package com.tordoya.subo.trip.repository;

import com.tordoya.subo.routing.dto.response.LatLngResponse;
import com.tordoya.subo.routing.dto.response.OpcionRutaResponse;
import com.tordoya.subo.routing.dto.response.PasoRutaResponse;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class TripRepository {
    private final JdbcTemplate jdbcTemplate;

    public void ensureDeviceExists(String deviceId) {
        jdbcTemplate.update("""
                INSERT INTO anonymous_device (device_id)
                VALUES (?)
                ON CONFLICT (device_id) DO NOTHING
                """, deviceId);
    }

    public void insertItinerary(UUID itineraryId, OpcionRutaResponse route) {
        String sql = """
                INSERT INTO itinerary (
                    id,
                    summary,
                    total_duration_minutes,
                    total_fare_bob,
                    transfers,
                    tags
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setObject(1, itineraryId);
                statement.setArray(2, connection.createArrayOf(
                        "text",
                        route.summary().toArray(String[]::new)
                ));
                statement.setInt(3, route.totalDurationMinutes());
                statement.setBigDecimal(4, route.precioTotalBob());
                statement.setInt(5, route.transfers());
                statement.setArray(6, connection.createArrayOf(
                        "text",
                        route.tags().toArray(String[]::new)
                ));
                statement.executeUpdate();
            }

            return null;
        });
    }

    public void insertStep(UUID itineraryId, int sequence, PasoRutaResponse step) {
        jdbcTemplate.update("""
                INSERT INTO itinerary_step (
                    id,
                    itinerary_id,
                    step_sequence,
                    mode,
                    route_pattern_id,
                    instruction,
                    from_label,
                    to_label,
                    duration_minutes,
                    distance_meters,
                    fare_bob,
                    path
                )
                VALUES (?, ?, ?, ?, NULL, ?, ?, ?, ?, ?, ?, ST_GeomFromText(?, 4326))
                """,
                UUID.randomUUID(),
                itineraryId,
                sequence,
                step.mode().name().toLowerCase(Locale.ROOT),
                step.instruction(),
                step.fromLabel(),
                step.toLabel(),
                step.durationMinutes(),
                step.distanceMeters(),
                step.fareBob(),
                toLineStringWkt(step.path())
        );
    }

    public void insertSharedTrip(
            UUID tripId,
            String deviceId,
            UUID itineraryId,
            String shareTokenHash,
            String writeTokenHash,
            String expirationMode,
            OffsetDateTime estimatedArrivalAt,
            OffsetDateTime expiresAt
    ) {
        jdbcTemplate.update("""
                INSERT INTO shared_trip (
                    id,
                    owner_device_id,
                    itinerary_id,
                    share_token_hash,
                    write_token_hash,
                    expiration_mode,
                    status,
                    current_step,
                    estimated_arrival_at,
                    expires_at
                )
                VALUES (?, ?, ?, ?, ?, ?, 'en_curso', 0, ?, ?)
                """,
                tripId,
                deviceId,
                itineraryId,
                shareTokenHash,
                writeTokenHash,
                expirationMode,
                estimatedArrivalAt,
                expiresAt
        );
    }

    private String toLineStringWkt(List<LatLngResponse> path) {
        if (path == null || path.size() < 2) {
            throw new IllegalArgumentException(
                    "A trip step path must contain at least two points"
            );
        }

        return "LINESTRING (" + path.stream()
                .map(point -> point.longitude() + " " + point.latitude())
                .collect(Collectors.joining(", ")) + ")";
    }
}