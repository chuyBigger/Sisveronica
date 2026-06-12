package com.laveronica.siscontrol.domain.usuario;

import com.laveronica.siscontrol.enums.Accion;
import com.laveronica.siscontrol.enums.Modulo;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "usuario_permisos", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usuario_id", "modulo", "accion"})
})
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class UsuarioPermiso {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Modulo modulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Accion accion;
}
