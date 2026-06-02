package com.laveronica.siscontrol.repositories;

import com.laveronica.siscontrol.domain.usuario.UsuarioPermiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuarioPermisoRepository extends JpaRepository<UsuarioPermiso, String> {

    List<UsuarioPermiso> findByUsuarioId(String usuarioId);

    @Modifying
    @Query("DELETE FROM UsuarioPermiso up WHERE up.usuario.id = ?1")
    void deleteByUsuarioId(String usuarioId);
}
