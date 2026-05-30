package com.laveronica.siscontrol.domain.productos.dto;

import com.laveronica.siscontrol.domain.productos.Producto;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public record DatosDetalleProducto(

        @NotNull
        String id,

        @NotNull
        String nombre,

        @NotNull
        String partida,

        @NotNull
        String categoriaId,

        @NotNull
        String unidadMedida,

        BigDecimal precioCompra,

        @NotNull
        BigDecimal precioVenta,

        String codigo

) {

    public DatosDetalleProducto(Producto producto){
        this(
                producto.getId(),
                producto.getNombre(),
                producto.getPartida().name(),
                producto.getCategoria().getId(),
                producto.getUnidadMedida().name(),
                producto.getPrecioCompra(),
                producto.getPrecioVenta(),
                producto.getCodigo()
        );
    }


}
