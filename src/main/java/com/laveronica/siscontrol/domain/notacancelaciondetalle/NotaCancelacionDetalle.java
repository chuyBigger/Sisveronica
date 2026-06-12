package com.laveronica.siscontrol.domain.notacancelaciondetalle;

import com.laveronica.siscontrol.domain.notacancelacion.NotaCancelacion;
import com.laveronica.siscontrol.domain.productos.Producto;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "nota_cancelacion_detalles")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotaCancelacionDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nota_cancelacion_id", nullable = false)
    private NotaCancelacion notaCancelacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "cantidad_cancelada", nullable = false)
    private Double cantidadCancelada;

    @Column(nullable = false)
    private boolean activo;
}
