package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.productos.*;
import com.laveronica.siscontrol.domain.productos.dto.DatosActualizarProducto;
import com.laveronica.siscontrol.domain.productos.dto.DatosDetalleProducto;
import com.laveronica.siscontrol.domain.productos.dto.DatosListarProductos;
import com.laveronica.siscontrol.domain.productos.dto.DatosRegistroProducto;
import com.laveronica.siscontrol.services.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
@Tag(name = "Productos")
@SecurityRequirement(name = "bearerAuth")
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    @Operation(summary = "Registrar producto")
    public ResponseEntity<DatosDetalleProducto> registrar(@RequestBody @Valid DatosRegistroProducto datos, UriComponentsBuilder uriComponentsBuilder){
        var producto = productoService.registrarProducto(datos);
        var uri = uriComponentsBuilder.path("/productos/{id}").buildAndExpand(producto).toUri();
        return ResponseEntity.created(uri).body(producto);
    }

    @GetMapping(path = {"","/"})
    @Operation(summary = "Listar productos")
    public ResponseEntity<Page<DatosListarProductos>> listarPodructo(@PageableDefault(size = 9, sort = {"nombre"}) Pageable paguinas) {
        var lista = productoService.listaProductos(paguinas);
        return ResponseEntity.ok(lista);
    }

    @GetMapping(path = "/partidas/{partida}")
    @Operation(summary = "Listar productos por partida")
    public ResponseEntity<Page<DatosListarProductos>> listarProductosPartida(@PathVariable String partida, @PageableDefault(size = 10, sort = {"nombre"}) Pageable paguinas){
        var listaProductosPartida = productoService.listaProductosPartida(paguinas, partida);
        return ResponseEntity.ok(listaProductosPartida);
    }

    @GetMapping(path = "/categorias/{id}")
    @Operation(summary = "Listar productos por categoría")
    public ResponseEntity<Page<DatosListarProductos>> listarProductoCategoria(@PathVariable String id,@PageableDefault(size = 10, sort = {"nombre"}) Pageable paguinas){
        var listaProdustosPorIdCategoria = productoService.listaProdictosCategoriaId(id, paguinas);
        return ResponseEntity.ok(listaProdustosPorIdCategoria);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Buscar producto por ID")
    public ResponseEntity<DatosDetalleProducto> buscarProductoId(@PathVariable String id){
        var producto = productoService.buscarProductoId(id);
        return ResponseEntity.ok(producto);
    }

    @GetMapping(path = "/buscar/{nombre}")
    @Operation(summary = "Buscar producto por nombre")
    public ResponseEntity<DatosDetalleProducto> buscarProductoNombre(@PathVariable String nombre){
        var producto = productoService.buscarProductoNombre(nombre);
        return ResponseEntity.ok(producto);
    }

    @GetMapping("/buscar_palabras")
    @Operation(summary = "Buscar productos por palabra clave")
    public ResponseEntity<Page<DatosDetalleProducto>> buscarProductosPorPalabra(@RequestParam (name = "q") String palabraBuscar, @PageableDefault(size = 10, sort = {"nombre"}) Pageable paguinas){
        var productos = productoService.buscarProductosPorPalabra(palabraBuscar, paguinas);
        return ResponseEntity.ok(productos);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar producto")
    public ResponseEntity<DatosDetalleProducto> actualizarProductoId(@PathVariable String id, @RequestBody DatosActualizarProducto datos){
        var nuevoProducto = productoService.actualizarProductoId(id, datos);
        return ResponseEntity.ok(nuevoProducto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto")
    public ResponseEntity eliminarProducto(@PathVariable String id ){
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
