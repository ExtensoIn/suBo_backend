package com.tordoya.subo.transport.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "transport_stop")
public class TransportStop {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(max = 80)
    @Column(name = "code", length = 80)
    private String code;

    @Size(max = 200)
    @NotNull
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Size(max = 30)
    @NotNull
    @Column(name = "stop_type", nullable = false, length = 30)
    private String stopType;

    @Column(name = "location", columnDefinition = "geometry not null")
    private Point location;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false, insertable = false)
    private OffsetDateTime updatedAt;
}