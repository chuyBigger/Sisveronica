package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.ordencompra.dto.DatosActulizarOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosDetalleOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosListarOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosRegistroOrdenCompra;
import com.laveronica.siscontrol.services.OrdenCompraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/orden_compra")
public class OrdenCompraController {

    @Autowired
    private OrdenCompraService ordenCompraService;

    @PostMapping
    public ResponseEntity<DatosDetalleOrdenCompra> registrarOrdenCompra(@Valid @RequestBody DatosRegistroOrdenCompra datos, UriComponentsBuilder uri) {
        var ordenCompra = ordenCompraService.registrarOrdenCompra(datos);
        var url = uri.path("/ordencompras/${id}").buildAndExpand(ordenCompra.id()).toUri();
        return ResponseEntity.created(url).body(ordenCompra);
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<Page<DatosListarOrdenCompra>> listarOrdenCompra(@PageableDefault(size = 9, sort = "fechaInicioSemana") Pageable paginacion) {
        var paguina = ordenCompraService.listarOrdenesCompra(paginacion);
        return ResponseEntity.ok().body(paguina);

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DatosDetalleOrdenCompra> buscarOrdenCompraId(@PathVariable Long id ){
        var ordenCompra = ordenCompraService.buscarOrdenCompraId(id);
        return ResponseEntity.ok().body(ordenCompra);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<DatosDetalleOrdenCompra> actilizarOdrdenCompra(@PathVariable Long id, @Valid @RequestBody DatosActulizarOrdenCompra datos) {
        var ordenCompraActualizada = ordenCompraService.actulizarOrdenCompraId(id, datos);
        return ResponseEntity.ok().body(ordenCompraActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrdenCompra(@PathVariable Long id){
        ordenCompraService.eliminarOrdenCompra(id);
        return ResponseEntity.noContent().build();
    }

}
