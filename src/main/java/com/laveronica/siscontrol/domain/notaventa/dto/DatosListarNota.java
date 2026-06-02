package com.laveronica.siscontrol.domain.notaventa.dto;

import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import com.laveronica.siscontrol.domain.notaventadetalle.dto.NotaVentaListarDetalle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record DatosListarNota(
        String id,
        Integer folio,
        LocalDateTime fecha,
        String cliente,
        String partida,
        List<NotaVentaListarDetalle> detalles,
        BigDecimal totalGeneral,
        String dia

) {
    public DatosListarNota(NotaVenta datos) {
        this(
                datos.getId(),
                datos.getFolio(),
                datos.getFecha(),
                datos.getCliente().getNombre(),
                datos.getPartida().name(),
                datos.getDetalles().stream()
                        .map(
                        detalle -> new NotaVentaListarDetalle(detalle))
                        .collect(Collectors.toList()),
                datos.getTotalGeneral(),
                datos.getDia()
        );
    }

}
