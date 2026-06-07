package com.laveronica.siscontrol.domain.factura.dto;

import com.laveronica.siscontrol.domain.facturadetalle.FacturaDetalle;
import java.math.BigDecimal;

public record DatosFacturaDetalle(
    String id,
    String productoNombre,
    Double cantidadTotal,
    BigDecimal precioVenta,
    BigDecimal subtotal
) {
    public DatosFacturaDetalle(FacturaDetalle d) {
        this(d.getId(), d.getProductoNombre(), d.getCantidadTotal(), d.getPrecioVenta(), d.getSubtotal());
    }
}
