package com.tordoya.subo.transport.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "service_window")
public class ServiceWindow {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "route_pattern_id", nullable = false)
    private RoutePattern routePattern;

    @NotNull
    @Column(name = "day_of_week", nullable = false)
    private Short dayOfWeek;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "headway_seconds")
    private Integer headwaySeconds;

    @Size(max = 20)
    @Column(name = "schedule_type", length = 20, nullable = false)
    private ScheduleType scheduleType;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active = true;


}