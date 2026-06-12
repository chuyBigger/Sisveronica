package com.laveronica.siscontrol.domain.extra;

import com.laveronica.siscontrol.domain.extradetalle.ExtraDetalle;
import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "extras")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Extra {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_compra_id", nullable = false)
    private OrdenCompra ordenCompra;

    @Column(nullable = false, length = 10)
    private String dia;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private Integer folio;

    @Column(nullable = false)
    private Boolean firmada;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "creado_por")
    private String creadoPor;

    @Column(nullable = false)
    private Boolean activo;

    @Builder.Default
    @OneToMany(mappedBy = "extra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExtraDetalle> detalles = new ArrayList<>();

    public void agregarDetalle(ExtraDetalle detalle) {
        detalles.add(detalle);
        detalle.setExtra(this);
    }
}
