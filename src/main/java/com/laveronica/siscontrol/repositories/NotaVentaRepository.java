package com.laveronica.siscontrol.repositories;

import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface NotaVentaRepository extends JpaRepository<NotaVenta, String> {
    Page<NotaVenta> findAllByActivoTrue(Pageable paginacion);

    Optional<NotaVenta> findByIdAndActivoTrue(String id);

    @Query("SELECT COALESCE(MAX(n.folio), 0) FROM nota_venta n")
    Integer findMaxFolio();

    Optional<NotaVenta> findByFolio(Integer folio);

    List<NotaVenta> findByOrdenCompraIdAndActivoTrue(String ordenCompraId);

    Optional<NotaVenta> findByOrdenCompraIdAndDiaAndActivoTrue(String ordenCompraId, String dia);

    @Query("SELECT n.ordenCompra.id, COUNT(n), SUM(CASE WHEN n.firmada = true THEN 1 ELSE 0 END) FROM nota_venta n WHERE n.ordenCompra.id IN :ids AND n.activo = true GROUP BY n.ordenCompra.id")
    List<Object[]> findNotaCountsByOrdenCompraIds(@Param("ids") Set<String> ids);
}
