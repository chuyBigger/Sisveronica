package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.productos.*;
import com.laveronica.siscontrol.domain.productos.dto.DatosActualizarProducto;
import com.laveronica.siscontrol.domain.productos.dto.DatosDetalleProducto;
import com.laveronica.siscontrol.domain.productos.dto.DatosListarProductos;
import com.laveronica.siscontrol.domain.productos.dto.DatosRegistroProducto;
import com.laveronica.siscontrol.services.ProductoService;
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
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<DatosDetalleProducto> registrar(@RequestBody @Valid DatosRegistroProducto datos, UriComponentsBuilder uriComponentsBuilder){
        var producto = productoService.registrarProducto(datos);
        var uri = uriComponentsBuilder.path("/productos/{id}").buildAndExpand(producto).toUri();
        return ResponseEntity.created(uri).body(producto);
    }

    @GetMapping(path = {"","/"})
    public ResponseEntity<Page<DatosListarProductos>> listarPodructo(@PageableDefault(size = 9, sort = {"nombre"}) Pageable paguinas) {
        var lista = productoService.listaProductos(paguinas);
        return ResponseEntity.ok(lista);
    }

    @GetMapping(path = "/partidas/{partida}")
    public ResponseEntity<Page<DatosListarProductos>> listarProductosPartida(@PathVariable String partida, @PageableDefault(size = 10, sort = {"nombre"}) Pageable paguinas){
        var listaProductosPartida = productoService.listaProductosPartida(paguinas, partida);
        return ResponseEntity.ok(listaProductosPartida);
    }

    @GetMapping(path = "/categorias/{id}")
    public ResponseEntity<Page<DatosListarProductos>> listarProductoCategoria(@PathVariable Long id,@PageableDefault(size = 10, sort = {"nombre"}) Pageable paguinas){
        var listaProdustosPorIdCategoria = productoService.listaProdictosCategoriaId(id, paguinas);
        return ResponseEntity.ok(listaProdustosPorIdCategoria);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DatosDetalleProducto> buscarProductoId(@PathVariable Long id){
        var producto = productoService.buscarProductoId(id);
        return ResponseEntity.ok(producto);
    }

    @GetMapping(path = "/buscar/{nombre}")
    public ResponseEntity<DatosDetalleProducto> buscarProductoNombre(@PathVariable String nombre){
        var producto = productoService.buscarProductoNombre(nombre);
        return ResponseEntity.ok(producto);
    }

    @GetMapping("/buscar_palabras")
    public ResponseEntity<Page<DatosDetalleProducto>> buscarProductosPorPalabra(@RequestParam (name = "q") String palabraBuscar, @PageableDefault(size = 10, sort = {"nombre"}) Pageable paguinas){
        var productos = productoService.buscarProductosPorPalabra(palabraBuscar, paguinas);
        return ResponseEntity.ok(productos);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DatosDetalleProducto> actualizarProductoId(@PathVariable Long id, @RequestBody DatosActualizarProducto datos){
        var nuevoProducto = productoService.actualizarProductoId(id, datos);
        return ResponseEntity.ok(nuevoProducto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminarProducto(@PathVariable Long id ){
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
