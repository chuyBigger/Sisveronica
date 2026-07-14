package com.laveronica.siscontrol.services;

import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import com.laveronica.siscontrol.domain.notaventadetalle.NotaVentaDetalle;
import com.laveronica.siscontrol.domain.notaventadetalle.dto.NotaVentaDetalleRegistro;
import com.laveronica.siscontrol.domain.productos.Producto;
import com.laveronica.siscontrol.repositories.NotaVentaDetalleRepository;
import com.laveronica.siscontrol.utils.helpers.ProductoValidacionesHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotaVentaDetalleService {

    private final ProductoValidacionesHelper productoValidacionesHelper;
    private final NotaVentaDetalleRepository notaVentaDetalleRepository;


    public List<NotaVentaDetalle> registrarNuevaListaNotaVentasDetalles(List<NotaVentaDetalleRegistro> datos, NotaVenta notaVenta){

        List<NotaVentaDetalle> detalles = datos.stream()
                .map(dn -> {
                    Producto producto = productoValidacionesHelper.encontrarProductoId(dn.productoId());
                    Integer cantidad = dn.cantidad();
                    return new NotaVentaDetalle(cantidad, producto, notaVenta);

                })
                .collect(Collectors.toList());
        return detalles;
    }

    @Transactional
    public NotaVentaDetalle agregarUnDetalleNuevo(NotaVentaDetalle datos) {
        return notaVentaDetalleRepository.save(datos);
    }

    public static BigDecimal calcularSubTotal(Integer cantidad, BigDecimal precioVenta) {
        return precioVenta.multiply(new BigDecimal(cantidad));
    }

    public static BigDecimal calcularTotalGeneral(List<NotaVentaDetalle> detalles ){
        return detalles.stream()
                .map(NotaVentaDetalle::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add );
    }

}
