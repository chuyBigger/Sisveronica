package com.laveronica.siscontrol.repositories;

import com.laveronica.siscontrol.domain.categoria.Categoria;
import com.laveronica.siscontrol.domain.productos.Producto;
import com.laveronica.siscontrol.enums.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProductosRepository extends JpaRepository<Producto, Long> {

    boolean existsByNombre(String nombre);

    Page<Producto> findAllByActivoTrue(Pageable paguinas);

    Page<Producto> findAllByPartidaAndActivoTrue(Partida partida, Pageable paguinas);

    Page<Producto> findAllByCategoriaAndActivoTrue(Categoria categoria, Pageable paguinas);

    Optional<Producto> findByIdAndActivoTrue(Long id);

    Optional<Producto> findByNombreAndActivoTrue(String nombre);

    Page<Producto> findAllByNombreContainingAndActivoTrue(String palabraBuscar, Pageable paguinas);
}
