package com.laveronica.siscontrol.repositories;

import com.laveronica.siscontrol.domain.notacancelacion.NotaCancelacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotaCancelacionRepository extends JpaRepository<NotaCancelacion, String> {
    List<NotaCancelacion> findByOrdenCompraIdAndActivoTrue(String ordenCompraId);
    Optional<NotaCancelacion> findByIdAndActivoTrue(String id);
    List<NotaCancelacion> findByOrdenCompraIdAndDiaAndValidadoPorIsNotNullAndActivoTrue(String ordenCompraId, String dia);
}
