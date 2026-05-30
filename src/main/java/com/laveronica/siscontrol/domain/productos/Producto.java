package com.laveronica.siscontrol.domain.productos;

import com.laveronica.siscontrol.domain.categoria.Categoria;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.enums.UnidadMedida;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Data

@Table(name = "productos")
@Entity

public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Partida partida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "unidad_medida", nullable = false)
    private UnidadMedida unidadMedida;

    @Column(name = "precio_compra")
    private BigDecimal precioCompra;

    @Column(name = "precio_venta")
    private BigDecimal precioVenta;

    @Column(nullable = false)
    private Boolean activo;
}


