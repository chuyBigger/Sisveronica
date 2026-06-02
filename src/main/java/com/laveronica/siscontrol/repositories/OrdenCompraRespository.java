package com.laveronica.siscontrol.repositories;

import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.enums.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface OrdenCompraRespository extends JpaRepository<OrdenCompra, String> {

    boolean existsByCliente_IdAndPartidaAndFechaInicioSemanaAndActivoTrue(String clienteId, Partida partida, LocalDate fecha);

    Page<OrdenCompra> findByAndActivoTrue(Pageable paginacion);

    Optional<OrdenCompra> findByIdAndActivoTrue(String id);
}
