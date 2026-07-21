package com.tordoya.subo.routing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PreferenciaRuta {
    @JsonProperty("mas_rapida")
    MAS_RAPIDA,

    @JsonProperty("mas_barata")
    MAS_BARATA
}