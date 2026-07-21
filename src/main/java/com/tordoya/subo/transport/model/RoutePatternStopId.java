package com.tordoya.subo.transport.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class RoutePatternStopId implements Serializable {
    @NotNull
    @Column(name = "route_pattern_id", nullable = false)
    private UUID routePatternId;

    @NotNull
    @Column(name = "stop_sequence", nullable = false)
    private Integer stopSequence;
}