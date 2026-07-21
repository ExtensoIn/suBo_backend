package com.tordoya.subo.transport.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "route_pattern_stop")
public class RoutePatternStop {
    @EmbeddedId
    private RoutePatternStopId id;

    @MapsId("routePatternId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "route_pattern_id", nullable = false)
    private RoutePattern routePattern;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stop_id", nullable = false)
    private TransportStop stop;

    @Column(name = "estimated_seconds_from_previous")
    private Integer estimatedSecondsFromPrevious;

    @NotNull
    @ColumnDefault("true")
    @Column(name = "pick_up_allowed", nullable = false)
    private Boolean pickUpAllowed;

    @NotNull
    @ColumnDefault("true")
    @Column(name = "drop_off_allowed", nullable = false)
    private Boolean dropOffAllowed;

    public Integer getStopSequence() {
        return id.getStopSequence();
    }
}