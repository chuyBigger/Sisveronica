package com.laveronica.siscontrol.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Partida {
    ABARROTES,
    CARNES,
    LACTEOS,
    FRUTASYVERDURAS,
    VARIOS;

    @JsonCreator
    public static Partida fromString(String value) {
        return Partida.valueOf(value.toUpperCase());
    }
}

