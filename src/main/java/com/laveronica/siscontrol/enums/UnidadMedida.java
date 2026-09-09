package com.laveronica.siscontrol.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UnidadMedida {
    KILO,
    LITRO,
    PIEZA,
    PAQUETE,
    LATA,
    KILOGRAMO,
    FRASCO;

    @JsonCreator
    public static UnidadMedida fromString(String value){
        return UnidadMedida.valueOf(value.toUpperCase());
    }
}
