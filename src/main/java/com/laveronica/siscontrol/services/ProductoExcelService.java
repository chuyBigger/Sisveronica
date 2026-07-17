package com.laveronica.siscontrol.services;

import com.laveronica.siscontrol.domain.categoria.Categoria;
import com.laveronica.siscontrol.domain.productos.Producto;
import com.laveronica.siscontrol.domain.productos.dto.DatosReporteCargaProductos;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.enums.UnidadMedida;
import com.laveronica.siscontrol.repositories.ProductosRepository;
import com.laveronica.siscontrol.utils.helpers.CategoriaValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.PartidaValidacionesHelper;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoExcelService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductoExcelService.class);

    private final ProductosRepository productosRepository;
    private final PartidaValidacionesHelper partidaValidacionesHelper;
    private final CategoriaValidacionesHelper categoriaValidacionesHelper;

    public DatosReporteCargaProductos cargarProductosDesdeExcel(MultipartFile archivo) {
        validarArchivo(archivo);

        List<String> duplicados = new ArrayList<>();
        List<String> sinPrecio = new ArrayList<>();
        int exitosos = 0;
        int totalProcesados = 0;

        try (Workbook workbook = new XSSFWorkbook(archivo.getInputStream())) {
            return procesarWorkbook(workbook, duplicados, sinPrecio, exitosos, totalProcesados);
        } catch (IOException e) {
            LOG.error("Error al leer el archivo Excel", e);
            throw new RuntimeException("Error al leer el archivo Excel", e);
        }
    }

    private DatosReporteCargaProductos procesarWorkbook(Workbook workbook, List<String> duplicados, List<String> sinPrecio, int exitosos, int totalProcesados) {
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            totalProcesados++;

            String nombre = getCellValue(row, 0);
            String partidaStr = getCellValue(row, 1);
            String categoriaNombre = getCellValue(row, 2);
            String codigo = getCellValue(row, 3);
            String unidadMedidaStr = getCellValue(row, 4);
            String precioCompraStr = getCellValue(row, 5);
            String precioVentaStr = getCellValue(row, 6);

            if (nombre == null || nombre.isBlank()) {
                continue;
            }

            BigDecimal precioVenta = parseBigDecimal(precioVentaStr);
            if (precioVenta == null || precioVenta.compareTo(BigDecimal.ZERO) <= 0) {
                sinPrecio.add("Fila " + (i + 1) + ": " + nombre + " (sin precio de venta)");
                continue;
            }

            if (productosRepository.existsByNombre(nombre.trim().toLowerCase())) {
                duplicados.add("Fila " + (i + 1) + ": " + nombre + " (ya existe)");
                continue;
            }

            try {
                Partida partida = partidaValidacionesHelper.validaPartidaExistaString(partidaStr);

                Categoria categoria = null;
                if (categoriaNombre != null && !categoriaNombre.isBlank()) {
                    try {
                        categoria = categoriaValidacionesHelper.validarCategoriaActiva(categoriaNombre);
                    } catch (Exception e) {
                        categoria = null;
                    }
                }

                UnidadMedida unidadMedida = UnidadMedida.KILO;
                if (unidadMedidaStr != null && !unidadMedidaStr.isBlank()) {
                    try {
                        unidadMedida = UnidadMedida.valueOf(unidadMedidaStr.trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        unidadMedida = UnidadMedida.KILO;
                    }
                }

                BigDecimal precioCompra = parseBigDecimal(precioCompraStr);

                Producto producto = new Producto();
                producto.setNombre(nombre.trim().toLowerCase());
                producto.setPartida(partida);
                producto.setCategoria(categoria);
                producto.setCodigo(codigo != null && !codigo.isBlank() ? codigo.trim().toUpperCase() : "PROD-" + System.currentTimeMillis());
                producto.setUnidadMedida(unidadMedida);
                producto.setPrecioCompra(precioCompra);
                producto.setPrecioVenta(precioVenta);
                producto.setActivo(true);

                productosRepository.save(producto);
                exitosos++;

            } catch (Exception e) {
                duplicados.add("Fila " + (i + 1) + ": " + nombre + " (error: " + e.getMessage() + ")");
            }
        }

        return new DatosReporteCargaProductos(
                totalProcesados,
                exitosos,
                duplicados.size(),
                sinPrecio.size(),
                duplicados,
                sinPrecio
        );
    }

    public byte[] generarPlantillaExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Productos");

            // Estilo para encabezados
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Encabezados
            String[] headers = {"Nombre", "Partida", "Categoría", "Código", "Unidad Medida", "Precio Compra", "Precio Venta"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Ejemplo de datos
            Row exampleRow = sheet.createRow(1);
            exampleRow.createCell(0).setCellValue("Papa blanca");
            exampleRow.createCell(1).setCellValue("FRUTASYVERDURAS");
            exampleRow.createCell(2).setCellValue("Verduras");
            exampleRow.createCell(3).setCellValue("PAP001");
            exampleRow.createCell(4).setCellValue("KILO");
            exampleRow.createCell(5).setCellValue(15.50);
            exampleRow.createCell(6).setCellValue(22.00);

            // Ajustar ancho de columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            LOG.error("Error al generar plantilla Excel", e);
            throw new RuntimeException("Error al generar la plantilla Excel", e);
        }
    }

    public byte[] exportarProductosPorPartida(String partidaStr) {
        Partida partida = partidaValidacionesHelper.validaPartidaExistaString(partidaStr);
        List<Producto> productos = productosRepository.findAllByPartidaAndActivoTrue(partida, Pageable.unpaged()).getContent();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Productos");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            String[] headers = {"Nombre", "Partida", "Categoría", "Código", "Unidad Medida", "Precio Compra", "Precio Venta"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (Producto p : productos) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(p.getNombre());
                row.createCell(1).setCellValue(p.getPartida().name());
                row.createCell(2).setCellValue(p.getCategoria() != null ? p.getCategoria().getNombre() : "");
                row.createCell(3).setCellValue(p.getCodigo() != null ? p.getCodigo() : "");
                row.createCell(4).setCellValue(p.getUnidadMedida().name());
                row.createCell(5).setCellValue(p.getPrecioCompra() != null ? p.getPrecioCompra().doubleValue() : 0);
                row.createCell(6).setCellValue(p.getPrecioVenta() != null ? p.getPrecioVenta().doubleValue() : 0);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            LOG.error("Error al exportar productos", e);
            throw new RuntimeException("Error al exportar productos", e);
        }
    }

    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }
        String filename = archivo.getOriginalFilename();
        if (filename == null || !filename.endsWith(".xlsx")) {
            throw new IllegalArgumentException("Formato inválido. Solo se permiten archivos .xlsx");
        }
    }

    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue().toString();
                }
                double val = cell.getNumericCellValue();
                yield val == (long) val ? String.valueOf((long) val) : String.valueOf(val);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> null;
        };
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            String cleaned = value.replace("$", "").replace(",", "").trim();
            return new BigDecimal(cleaned);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
