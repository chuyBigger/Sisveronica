package com.laveronica.siscontrol.repositories;

import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.enums.Partida;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrdenCompraRespository extends JpaRepository<OrdenCompra, String> {

    boolean existsByCliente_IdAndPartidaAndFechaInicioSemanaAndActivoTrue(String clienteId, Partida partida, LocalDate fecha);

    Page<OrdenCompra> findByAndActivoTrue(Pageable paginacion);

    Optional<OrdenCompra> findByIdAndActivoTrue(String id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM OrdenCompra o WHERE o.id = :id AND o.activo = true")
    Optional<OrdenCompra> findByIdAndActivoTrueWithLock(@Param("id") String id);

    @Query("SELECT o FROM OrdenCompra o WHERE o.activo = true AND o.fechaInicioSemana <= :fecha AND o.fechaFinSemana >= :fecha")
    Page<OrdenCompra> findByFechaInPeriodo(@Param("fecha") LocalDate fecha, Pageable paginacion);

    List<OrdenCompra> findByActivoTrueAndFechaInicioSemanaAndPartida(LocalDate fechaInicioSemana, Partida partida);
}
