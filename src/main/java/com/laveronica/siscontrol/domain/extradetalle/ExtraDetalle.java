package com.laveronica.siscontrol.domain.extradetalle;

import com.laveronica.siscontrol.domain.extra.Extra;
import com.laveronica.siscontrol.domain.productos.Producto;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "extra_detalles")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExtraDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extra_id", nullable = false)
    private Extra extra;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Double cantidad;

    @Column(nullable = false)
    private boolean activo;
}
