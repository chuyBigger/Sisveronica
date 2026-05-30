package com.laveronica.siscontrol.domain.productos.dto;


import com.laveronica.siscontrol.domain.productos.Producto;

import java.math.BigDecimal;

public record DatosListarProductos(
        String id,
        String nombre,
        String partida,
        String categoria,
        String codigo,
        BigDecimal precioVenta

) {

    public DatosListarProductos(Producto producto){

        this(
                producto.getId(),
                producto.getNombre(),
                producto.getPartida().name(),
                producto.getCategoria().getNombre(),
                producto.getCodigo(),
                producto.getPrecioVenta()
        );

    }
}
