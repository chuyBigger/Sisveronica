package com.laveronica.siscontrol.utils.helpers;

import com.laveronica.siscontrol.domain.notaventadetalle.NotaVentaDetalle;
import com.laveronica.siscontrol.repositories.NotaVentaDetalleRepository;
import com.laveronica.siscontrol.repositories.NotaVentaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotaVentaDetalleValidacionesHelper {

    @Autowired
    private NotaVentaDetalleRepository notaVentaDetalleRepository;

    @Autowired
    private NotaVentaRepository notaVentaRepository;

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
