package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.categoria.*;
import com.laveronica.siscontrol.domain.categoria.dto.DatosActualizarCategoria;
import com.laveronica.siscontrol.domain.categoria.dto.DatosDetalleCategoria;
import com.laveronica.siscontrol.domain.categoria.dto.DatosRegistroCategoria;
import com.laveronica.siscontrol.services.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroCategoria datos, UriComponentsBuilder uriComponentsBuilder) {
        Categoria nuevaCategoria = categoriaService.registrarCategoria(datos);
        var uri = uriComponentsBuilder.path("/categorias/{id}").buildAndExpand(nuevaCategoria.getId()).toUri();
        return ResponseEntity.created(uri).body(nuevaCategoria);
    }

    @GetMapping(value = {"", "/"})
    public List<Categoria> listarCategorias(){
        List listaCategorias = categoriaService.listaCategorias();
        return listaCategorias;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleCategoria> buscarCategoriaId(@PathVariable String id){
        Categoria categoriaId = categoriaService.buscarCategoriaId(id);
        return ResponseEntity.ok(new DatosDetalleCategoria(categoriaId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DatosDetalleCategoria> actualizaCategoriaid( @PathVariable String id, @RequestBody @Valid DatosActualizarCategoria datos){
        Categoria categoriaActualizada = categoriaService.actualizarCategoria(id, datos);
        return ResponseEntity.ok(new DatosDetalleCategoria(categoriaActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable String id){
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }



}
