package com.laveronica.siscontrol.domain.ordencompradetalle.dto;

import java.time.LocalDate;

public record DatosRegistroOrdenCompraDetalle(

        LocalDate fecha,
        String producto,
        Double lunes,
        Double martes,
        Double miercoles,
        Double jueves,
        Double viernes,
        Double sabado,
        Double domingo
) {
}
