package com.laveronica.siscontrol.repositories;

import com.laveronica.siscontrol.domain.contratos.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContratoRepository extends JpaRepository<Contrato, String> {

    boolean existsByContrato(String contrato);

    Optional<Contrato> findByIdAndActivoTrue(String id);

    Optional<Contrato> findByContratoAndActivoTrue(String contrato);

    List<Contrato> findAllByActivoTrue();


}
