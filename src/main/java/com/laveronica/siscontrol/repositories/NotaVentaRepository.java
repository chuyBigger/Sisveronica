package com.laveronica.siscontrol.repositories;

import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotaVentaRepository extends JpaRepository<NotaVenta, String> {
    Page<NotaVenta> findAllByActivoTrue(Pageable paginacion);

    Optional<NotaVenta> findByIdAndActivoTrue(String id);
}
