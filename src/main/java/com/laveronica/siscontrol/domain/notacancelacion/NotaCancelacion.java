package com.laveronica.siscontrol.domain.notacancelacion;

import com.laveronica.siscontrol.domain.notacancelaciondetalle.NotaCancelacionDetalle;
import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "nota_cancelaciones")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class NotaCancelacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_compra_id", nullable = false)
    private OrdenCompra ordenCompra;

    @Column(nullable = false, length = 10)
    private String dia;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "creado_por")
    private String creadoPor;

    @Column(name = "validado_por")
    private String validadoPor;

    @Column(name = "fecha_validacion")
    private LocalDateTime fechaValidacion;

    @Builder.Default
    @OneToMany(mappedBy = "notaCancelacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotaCancelacionDetalle> detalles = new ArrayList<>();

    @Column(nullable = false)
    private Boolean activo;

    public void agregarDetalle(NotaCancelacionDetalle detalle) {
        detalles.add(detalle);
        detalle.setNotaCancelacion(this);
    }
}
