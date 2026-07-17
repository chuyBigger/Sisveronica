package com.laveronica.siscontrol.repositories;

import com.laveronica.siscontrol.domain.extra.Extra;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExtraRepository extends JpaRepository<Extra, String> {
    List<Extra> findByOrdenCompraIdAndActivoTrue(String ordenCompraId);
    Optional<Extra> findByIdAndActivoTrue(String id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT COALESCE(MAX(e.folio), 0) FROM Extra e")
    Integer findMaxFolio();
}
