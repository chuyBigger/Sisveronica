package com.laveronica.siscontrol.domain.categoria;

import com.laveronica.siscontrol.domain.categoria.dto.DatosRegistroCategoria;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.domain.productos.Producto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "categorias")
@Entity(name = "categoria")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Partida partida;

    @Column(nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "categoria")
    private List<Producto> productos = new ArrayList<>();

    public Categoria(DatosRegistroCategoria categoria){
        this.id=null;
        this.nombre = categoria.nombre();
        this.partida = categoria.partida();
        this.activo = true;
    }


}
