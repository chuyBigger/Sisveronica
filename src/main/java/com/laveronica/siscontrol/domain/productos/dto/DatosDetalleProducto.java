package com.laveronica.siscontrol.domain.productos.dto;

import com.laveronica.siscontrol.domain.productos.Producto;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public record DatosDetalleProducto(

        @NotNull
        Integer id,

        @NotNull
        String nombre,

        @NotNull
        String partida,

        @NotNull
        Integer categoriaId,

        @NotNull
        String unidadMedida,

        BigDecimal precioCompra,

        @NotNull
        BigDecimal precioVenta



) {

    public DatosDetalleProducto(Producto producto){
        this(
                producto.getId().intValue(),
                producto.getNombre(),
                producto.getPartida().name(),
                producto.getCategoria().getId().intValue(),
                producto.getUnidadMedida().name(),
                producto.getPrecioCompra(),
                producto.getPrecioVenta()
        );
    }


}
