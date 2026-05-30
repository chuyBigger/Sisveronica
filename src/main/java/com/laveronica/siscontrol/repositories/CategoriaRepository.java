package com.laveronica.siscontrol.repositories;

import com.laveronica.siscontrol.domain.categoria.Categoria;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, String> {

    boolean existsByNombre(@NotNull String nombre);

    Optional<Categoria> findByIdAndActivoTrue(String id);

}
