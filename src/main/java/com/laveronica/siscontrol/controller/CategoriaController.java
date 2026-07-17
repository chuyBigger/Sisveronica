package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.categoria.*;
import com.laveronica.siscontrol.domain.categoria.dto.DatosActualizarCategoria;
import com.laveronica.siscontrol.domain.categoria.dto.DatosDetalleCategoria;
import com.laveronica.siscontrol.domain.categoria.dto.DatosRegistroCategoria;
import com.laveronica.siscontrol.services.CategoriaService;
import com.laveronica.siscontrol.services.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías")
@SecurityRequirement(name = "bearerAuth")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    @Operation(summary = "Registrar categoría")
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroCategoria datos, UriComponentsBuilder uriComponentsBuilder) {
        Categoria nuevaCategoria = categoriaService.registrarCategoria(datos);
        var uri = uriComponentsBuilder.path("/categorias/{id}").buildAndExpand(nuevaCategoria.getId()).toUri();
        return ResponseEntity.created(uri).body(nuevaCategoria);
    }

    @GetMapping(value = {"", "/"})
    @Operation(summary = "Listar categorías")
    public List<Categoria> listarCategorias(){
        List listaCategorias = categoriaService.listaCategorias();
        return listaCategorias;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoría por ID")
    public ResponseEntity<DatosDetalleCategoria> buscarCategoriaId(@PathVariable String id){
        Categoria categoriaId = categoriaService.buscarCategoriaId(id);
        return ResponseEntity.ok(new DatosDetalleCategoria(categoriaId));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar categoría")
    public ResponseEntity<DatosDetalleCategoria> actualizaCategoriaid( @PathVariable String id, @RequestBody @Valid DatosActualizarCategoria datos){
        Categoria categoriaActualizada = categoriaService.actualizarCategoria(id, datos);
        return ResponseEntity.ok(new DatosDetalleCategoria(categoriaActualizada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable String id){
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }



}
