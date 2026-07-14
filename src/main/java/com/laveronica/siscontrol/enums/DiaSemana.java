package com.laveronica.siscontrol.enums;

import com.laveronica.siscontrol.domain.ordencompradetalle.OrdenCompraDetalle;

import java.time.LocalDate;

public enum DiaSemana {

    LUNES(7),
    MARTES(1),
    MIERCOLES(2),
    JUEVES(3),
    VIERNES(4),
    SABADO(5),
    DOMINGO(6);

    private final int offset;

    DiaSemana(int offset) {
        this.offset = offset;
    }

    public LocalDate calcularFecha(LocalDate fechaInicioSemana) {
        return fechaInicioSemana.plusDays(offset);
    }

    public Double getCantidad(OrdenCompraDetalle detalle) {
        return switch (this) {
            case LUNES -> detalle.getLunes();
            case MARTES -> detalle.getMartes();
            case MIERCOLES -> detalle.getMiercoles();
            case JUEVES -> detalle.getJueves();
            case VIERNES -> detalle.getViernes();
            case SABADO -> detalle.getSabado();
            case DOMINGO -> detalle.getDomingo();
        };
    }

    public static DiaSemana fromString(String dia) {
        if (dia == null || dia.isBlank()) {
            throw new IllegalArgumentException("Día no puede estar vacío");
        }
        return switch (dia.trim().toLowerCase()) {
            case "lunes" -> LUNES;
            case "martes" -> MARTES;
            case "miercoles" -> MIERCOLES;
            case "jueves" -> JUEVES;
            case "viernes" -> VIERNES;
            case "sabado" -> SABADO;
            case "domingo" -> DOMINGO;
            default -> throw new IllegalArgumentException("Día inválido: " + dia);
        };
    }
}
