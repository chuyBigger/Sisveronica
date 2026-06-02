package com.laveronica.siscontrol.repositories;

import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotaVentaRepository extends JpaRepository<NotaVenta, String> {
    Page<NotaVenta> findAllByActivoTrue(Pageable paginacion);

    Optional<NotaVenta> findByIdAndActivoTrue(String id);

    @Query("SELECT COALESCE(MAX(n.folio), 0) FROM nota_venta n")
    Integer findMaxFolio();

    List<NotaVenta> findByOrdenCompraIdAndActivoTrue(String ordenCompraId);

    Optional<NotaVenta> findByOrdenCompraIdAndDiaAndActivoTrue(String ordenCompraId, String dia);
}
