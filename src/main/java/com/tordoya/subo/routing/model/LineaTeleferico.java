package com.tordoya.subo.routing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum LineaTeleferico {
    @JsonProperty("roja")
    ROJA,

    @JsonProperty("amarilla")
    AMARILLA,

    @JsonProperty("verde")
    VERDE,

    @JsonProperty("azul")
    AZUL,

    @JsonProperty("naranja")
    NARANJA,

    @JsonProperty("blanca")
    BLANCA,

    @JsonProperty("celeste")
    CELESTE,

    @JsonProperty("morada")
    MORADA,

    @JsonProperty("cafe")
    CAFE,

    @JsonProperty("plateada")
    PLATEADA
}