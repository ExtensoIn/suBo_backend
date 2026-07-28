package com.tordoya.subo.trip.model;

import com.tordoya.subo.transport.model.RoutePattern;
import com.tordoya.subo.trip.model.converter.TripModeConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.locationtech.jts.geom.LineString;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "itinerary_step")
public class ItineraryStep {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "itinerary_id", nullable = false)
    private Itinerary itinerary;

    @NotNull
    @Column(name = "step_sequence", nullable = false)
    private Integer stepSequence;

    @Convert(converter = TripModeConverter.class)
    @Column(name = "mode", nullable = false, length = 30, columnDefinition = "varchar(30)")
    private TripMode mode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_pattern_id")
    private RoutePattern routePattern;

    @Size(max = 300)
    @Column(name = "instruction", length = 300)
    private String instruction;

    @Size(max = 200)
    @NotNull
    @Column(name = "from_label", nullable = false, length = 200)
    private String fromLabel;

    @Size(max = 200)
    @NotNull
    @Column(name = "to_label", nullable = false, length = 200)
    private String toLabel;

    @NotNull
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "distance_meters")
    private Integer distanceMeters;

    @Column(name = "fare_bob", precision = 8, scale = 2)
    private BigDecimal fareBob;

    @Column(name = "path", columnDefinition = "geometry not null")
    private LineString path;

    public ItineraryStep(
            int stepSequence,
            TripMode mode,
            String instruction,
            String fromLabel,
            String toLabel,
            int durationMinutes,
            Integer distanceMeters,
            BigDecimal fareBob,
            LineString path
    ) {
        this.stepSequence = stepSequence;
        this.mode = mode;
        this.instruction = instruction;
        this.fromLabel = fromLabel;
        this.toLabel = toLabel;
        this.durationMinutes = durationMinutes;
        this.distanceMeters = distanceMeters;
        this.fareBob = fareBob;
        this.path = path;
    }

    protected ItineraryStep() {
    }

    void attachTo(Itinerary itinerary) {
        this.itinerary = itinerary;
    }
}