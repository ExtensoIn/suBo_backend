package com.tordoya.subo.trip.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AccessDuration {
    @JsonProperty("hasta_llegar")
    HASTA_LLEGAR,

    @JsonProperty("una_hora")
    UNA_HORA,

    @JsonProperty("veinticuatro_horas")
    VEINTICUATRO_HORAS
}