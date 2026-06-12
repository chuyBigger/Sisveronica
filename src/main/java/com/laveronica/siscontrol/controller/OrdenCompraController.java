package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.notaventa.dto.DatosDetalleNota;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosListarNota;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosActulizarOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosDetalleOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosListarOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosRegistroOrdenCompra;
import com.laveronica.siscontrol.services.OrdenCompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/orden_compra")
@Tag(name = "Órdenes de Compra")
@SecurityRequirement(name = "bearerAuth")
public class OrdenCompraController {

    @Autowired
    private OrdenCompraService ordenCompraService;

    @PostMapping
    @Operation(summary = "Registrar orden de compra")
    public ResponseEntity<DatosDetalleOrdenCompra> registrarOrdenCompra(@Valid @RequestBody DatosRegistroOrdenCompra datos, UriComponentsBuilder uri) {
        var ordenCompra = ordenCompraService.registrarOrdenCompra(datos);
        var url = uri.path("/ordencompras/${id}").buildAndExpand(ordenCompra.id()).toUri();
        return ResponseEntity.created(url).body(ordenCompra);
    }

    @GetMapping(path = {"", "/"})
    @Operation(summary = "Listar órdenes de compra")
    public ResponseEntity<Page<DatosListarOrdenCompra>> listarOrdenCompra(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @PageableDefault(size = 9, sort = "fechaInicioSemana") Pageable paginacion) {
        Page<DatosListarOrdenCompra> pagina;
        if (fecha != null) {
            pagina = ordenCompraService.listarOrdenesCompraPorFecha(fecha, paginacion);
        } else {
            pagina = ordenCompraService.listarOrdenesCompra(paginacion);
        }
        return ResponseEntity.ok(pagina);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Buscar orden de compra por ID")
    public ResponseEntity<DatosDetalleOrdenCompra> buscarOrdenCompraId(@PathVariable String id ){
        var ordenCompra = ordenCompraService.buscarOrdenCompraId(id);
        return ResponseEntity.ok().body(ordenCompra);
    }

    @PatchMapping(path = "/{id}")
    @Operation(summary = "Actualizar orden de compra")
    public ResponseEntity<DatosDetalleOrdenCompra> actilizarOdrdenCompra(@PathVariable String id, @Valid @RequestBody DatosActulizarOrdenCompra datos) {
        var ordenCompraActualizada = ordenCompraService.actulizarOrdenCompraId(id, datos);
        return ResponseEntity.ok().body(ordenCompraActualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar orden de compra")
    public ResponseEntity<Void> eliminarOrdenCompra(@PathVariable String id){
        ordenCompraService.eliminarOrdenCompra(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar orden de compra")
    public ResponseEntity<DatosDetalleOrdenCompra> confirmarOrdenCompra(@PathVariable String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var ordenCompra = ordenCompraService.confirmarOrdenCompra(id, username);
        return ResponseEntity.ok(ordenCompra);
    }

    @GetMapping("/{id}/notas")
    @Operation(summary = "Listar notas por orden de compra")
    public ResponseEntity<List<DatosListarNota>> listarNotasPorOrden(@PathVariable String id) {
        var notas = ordenCompraService.listarNotasPorOrden(id);
        return ResponseEntity.ok(notas);
    }

    @PostMapping("/{id}/generar-notas")
    @Operation(summary = "Generar todas las notas de orden")
    public ResponseEntity<List<DatosDetalleNota>> generarTodasNotas(@PathVariable String id) {
        var notas = ordenCompraService.generarTodasNotas(id);
        return ResponseEntity.ok(notas);
    }

}
