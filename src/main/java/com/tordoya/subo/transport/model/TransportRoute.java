package com.tordoya.subo.transport.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "transport_route")
public class TransportRoute {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(max = 80)
    @NotNull
    @Column(name = "code", nullable = false, length = 80)
    private String code;

    @Size(max = 200)
    @NotNull
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Size(max = 200)
    @Column(name = "operator_name", length = 200)
    private String operatorName;

    @Size(max = 30)
    @NotNull
    @Column(name = "mode", nullable = false, length = 30)
    private TransportMode  mode;

    @Size(max = 80)
    @NotNull
    @Column(name = "short_name", nullable = false, length = 80)
    private String shortName;

    @Size(max = 7)
    @Column(name = "color", length = 7)
    private String color;

    @Column(name = "fare_bob", precision = 8, scale = 2)
    private BigDecimal fareBob;

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