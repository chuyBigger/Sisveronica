package com.laveronica.siscontrol.domain.productos.dto;

import java.math.BigDecimal;

public record DatosActualizarProducto(


        String nombre,

        String partida,

        String categoriaId,

        String unidadMedida,

        BigDecimal precioCompra,

        BigDecimal precioVenta,

        String codigo

) {
}
