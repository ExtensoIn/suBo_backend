package com.tordoya.subo.trip.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "itinerary")
public class Itinerary {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "summary", nullable = false)
    private List<String> summary;

    @NotNull
    @Column(name = "total_duration_minutes", nullable = false)
    private Integer totalDurationMinutes;

    @NotNull
    @Column(name = "total_fare_bob", nullable = false, precision = 8, scale = 2)
    private BigDecimal totalFareBob;

    @NotNull
    @Column(name = "transfers", nullable = false)
    private Integer transfers;

    @NotNull
    @Column(name = "tags", nullable = false)
    private List<String> tags;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("stepSequence ASC")
    private List<ItineraryStep> steps = new ArrayList<>();

    public Itinerary(
            List<String> summary,
            int totalDurationMinutes,
            BigDecimal totalFareBob,
            int transfers,
            List<String> tags
    ) {
        this.summary = summary;
        this.totalDurationMinutes = totalDurationMinutes;
        this.totalFareBob = totalFareBob;
        this.transfers = transfers;
        this.tags = tags;
    }

    protected Itinerary() {
    }

    public void addStep(ItineraryStep step) {
        steps.add(step);
        step.attachTo(this);
    }
}