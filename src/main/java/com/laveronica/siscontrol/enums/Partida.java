package com.laveronica.siscontrol.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Partida {
    ABARROTES,
    CARNES,
    LACTEOS,
    FRUTASYVERDURAS,
    VARIOS,
    GENERAL;

    @JsonCreator
    public static Partida fromString(String value) {
        return Partida.valueOf(value.toUpperCase());
    }
}

