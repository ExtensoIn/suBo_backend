package com.tordoya.subo.routing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ModoTransporte {
    @JsonProperty("caminar")
    CAMINAR,

    @JsonProperty("teleferico")
    TELEFERICO,

    @JsonProperty("pumakatari")
    PUMAKATARI,

    @JsonProperty("minibus")
    MINIBUS,

    @JsonProperty("trufi")
    TRUFI
}