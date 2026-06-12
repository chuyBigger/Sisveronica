package com.laveronica.siscontrol.repositories;

import com.laveronica.siscontrol.domain.notacancelacion.NotaCancelacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface NotaCancelacionRepository extends JpaRepository<NotaCancelacion, String> {
    List<NotaCancelacion> findByOrdenCompraIdAndActivoTrue(String ordenCompraId);
    Optional<NotaCancelacion> findByIdAndActivoTrue(String id);
    List<NotaCancelacion> findByOrdenCompraIdAndDiaAndValidadoPorIsNotNullAndActivoTrue(String ordenCompraId, String dia);

    @Query("SELECT c.ordenCompra.id, COUNT(c), SUM(CASE WHEN c.validadoPor IS NOT NULL THEN 1 ELSE 0 END) FROM NotaCancelacion c WHERE c.ordenCompra.id IN :ids AND c.activo = true GROUP BY c.ordenCompra.id")
    List<Object[]> findCancelacionCountsByOrdenCompraIds(@Param("ids") Set<String> ids);
}
