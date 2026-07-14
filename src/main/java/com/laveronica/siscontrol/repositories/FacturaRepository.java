package com.laveronica.siscontrol.repositories;

import com.laveronica.siscontrol.domain.factura.Factura;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FacturaRepository extends JpaRepository<Factura, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT COALESCE(MAX(f.folio), 0) FROM Factura f")
    Integer obtenerMaxFolio();

    Optional<Factura> findByOrdenCompraIdAndActivoTrue(String ordenCompraId);

    Optional<Factura> findByOrdenCompraIdAndActivoTrueAndEsExtrasFalse(String ordenCompraId);

    Optional<Factura> findByOrdenCompraIdAndActivoTrueAndEsExtrasTrue(String ordenCompraId);

    boolean existsByOrdenCompraIdAndActivoTrue(String ordenCompraId);

    boolean existsByOrdenCompraIdAndActivoTrueAndEsExtrasFalse(String ordenCompraId);

    boolean existsByOrdenCompraIdAndActivoTrueAndEsExtrasTrue(String ordenCompraId);

    List<Factura> findByActivoTrueOrderByFechaCreacionDesc();

    List<Factura> findByActivoTrueAndOrdenCompraIdOrderByFechaCreacionDesc(String ordenCompraId);

    @Query("SELECT f.ordenCompra.id FROM Factura f WHERE f.ordenCompra.id IN :ids AND f.activo = true")
    Set<String> findOrdenCompraIdsWithFactura(@Param("ids") Set<String> ids);
}
