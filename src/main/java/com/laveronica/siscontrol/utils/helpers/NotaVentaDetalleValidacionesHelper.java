package com.laveronica.siscontrol.utils.helpers;

import com.laveronica.siscontrol.domain.notaventadetalle.NotaVentaDetalle;
import com.laveronica.siscontrol.repositories.NotaVentaDetalleRepository;
import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotaVentaDetalleValidacionesHelper {

    private final NotaVentaDetalleRepository notaVentaDetalleRepository;

    @Transactional
    public NotaVentaDetalle detalleProductoExiste(String id, String producto){
        if (producto == null || producto.isBlank()){
            return null;
        }
        String productoNormalizado = producto.toUpperCase().trim();

        return notaVentaDetalleRepository.findByNotaVenta_IdAndProducto_NombreIgnoreCase(id, productoNormalizado)
                .orElse(null);
    }



}
