package com.laveronica.siscontrol.repositories;

import com.laveronica.siscontrol.domain.notaventadetalle.NotaVentaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotaVentaDetalleRepository extends JpaRepository<NotaVentaDetalle, String> {
    Optional<NotaVentaDetalle> findByNotaVenta_IdAndProducto_NombreIgnoreCase(String id, String producto);
}
