package com.laveronica.siscontrol.domain.factura.dto;

import com.laveronica.siscontrol.domain.factura.Factura;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record DatosListarFactura(
    String id,
    Integer folio,
    String ordenCompraId,
    String cliente,
    String contrato,
    String partida,
    LocalDateTime fechaCreacion,
    BigDecimal totalGeneral,
    List<DatosFacturaDetalle> detalles
) {
    public DatosListarFactura(Factura f) {
        this(
            f.getId(),
            f.getFolio(),
            f.getOrdenCompra().getId(),
            f.getCliente(),
            f.getContrato(),
            f.getPartida(),
            f.getFechaCreacion(),
            f.getTotalGeneral(),
            f.getDetalles().stream().map(DatosFacturaDetalle::new).toList()
        );
    }
}
