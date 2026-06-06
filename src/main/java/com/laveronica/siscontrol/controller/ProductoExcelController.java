package com.laveronica.siscontrol.controller;

import com.laveronica.siscontrol.domain.productos.dto.DatosReporteCargaProductos;
import com.laveronica.siscontrol.services.ProductoExcelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/productos/excel")
@RequiredArgsConstructor
@Tag(name = "Productos")
@SecurityRequirement(name = "bearerAuth")
public class ProductoExcelController {

    private final ProductoExcelService productoExcelService;

    @PostMapping("/cargar")
    @Operation(summary = "Cargar productos desde Excel")
    @ApiResponse(responseCode = "400", description = "Archivo vacío o formato inválido", content = @Content)
    @ApiResponse(responseCode = "500", description = "Error interno al procesar el archivo", content = @Content)
    public ResponseEntity<DatosReporteCargaProductos> cargarProductos(@RequestParam("archivo") MultipartFile archivo) {
        DatosReporteCargaProductos reporte = productoExcelService.cargarProductosDesdeExcel(archivo);
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/plantilla")
    @Operation(summary = "Descargar plantilla Excel")
    @ApiResponse(responseCode = "500", description = "Error interno al generar la plantilla", content = @Content)
    public ResponseEntity<byte[]> descargarPlantilla() {
        byte[] plantilla = productoExcelService.generarPlantillaExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "plantilla_productos.xlsx");
        headers.setContentLength(plantilla.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(plantilla);
    }
}
