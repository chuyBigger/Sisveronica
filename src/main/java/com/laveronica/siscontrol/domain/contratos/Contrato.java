package com.laveronica.siscontrol.domain.contratos;

import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.domain.contratos.dto.DatosRegistroContrato;
import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

@Table(name = "contratos")
@Entity(name = "contrato")

public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String contrato;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_termino", nullable = false)
    private LocalDate fechaTermino;



    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal presupuesto;

    @Column(nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotaVenta> notas = new ArrayList<>();

    public Contrato(@Valid DatosRegistroContrato datos, Cliente cliente) {

        this.contrato = datos.contrato();
        this.cliente = cliente;
        this.fechaInicio = datos.fechaInicio();
        this.fechaTermino = datos.fechaTermino();
        this.presupuesto = datos.presupuesto();
        this.activo= true;



    }
}
