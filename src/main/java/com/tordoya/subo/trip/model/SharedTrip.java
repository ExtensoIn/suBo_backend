package com.tordoya.subo.trip.model;

import com.tordoya.subo.device.model.AnonymousDevice;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "shared_trip")
public class SharedTrip {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_device_id", nullable = false)
    private AnonymousDevice ownerDevice;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "itinerary_id", nullable = false)
    private Itinerary itinerary;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(
            name = "share_token_hash",
            nullable = false,
            unique = true,
            length = 64,
            columnDefinition = "char(64)"
    )
    private String shareTokenHash;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(
            name = "write_token_hash",
            nullable = false,
            unique = true,
            length = 64,
            columnDefinition = "char(64)"
    )
    private String writeTokenHash;

    @Size(max = 20)
    @NotNull
    @Convert(converter = ExpirationMode.JpaConverter.class)
    @Column(name = "expiration_mode", nullable = false, length = 20)
    private ExpirationMode expirationMode;

    @Size(max = 20)
    @NotNull
    @Convert(converter = SharedTripStatus.JpaConverter.class)
    @Column(name = "status", nullable = false, length = 20)
    private SharedTripStatus status;

    @NotNull
    @Column(name = "current_step", nullable = false)
    private Integer currentStep;

    @Column(name = "current_position", columnDefinition = "geometry")
    private Point currentPosition;

    @Column(name = "estimated_arrival_at")
    private OffsetDateTime estimatedArrivalAt;

    @NotNull
    @Column(name = "started_at", nullable = false)
    private OffsetDateTime startedAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    @Column(name = "ended_at")
    private OffsetDateTime endedAt;

    public SharedTrip(
            AnonymousDevice ownerDevice,
            Itinerary itinerary,
            String shareTokenHash,
            String writeTokenHash,
            ExpirationMode expirationMode,
            OffsetDateTime estimatedArrivalAt,
            OffsetDateTime expiresAt,
            OffsetDateTime now
    ) {
        this.ownerDevice = ownerDevice;
        this.itinerary = itinerary;
        this.shareTokenHash = shareTokenHash;
        this.writeTokenHash = writeTokenHash;
        this.expirationMode = expirationMode;
        this.status = SharedTripStatus.IN_PROGRESS;
        this.currentStep = 0;
        this.estimatedArrivalAt = estimatedArrivalAt;
        this.expiresAt = expiresAt;
        this.startedAt = now;
        this.updatedAt = now;
    }

    protected SharedTrip() {}

    @PreUpdate
    void updateTimestamp() {
        updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}