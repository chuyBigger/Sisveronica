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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoExcelService {

    private final ProductosRepository productosRepository;
    private final PartidaValidacionesHelper partidaValidacionesHelper;
    private final CategoriaValidacionesHelper categoriaValidacionesHelper;

    public DatosReporteCargaProductos cargarProductosDesdeExcel(MultipartFile archivo) throws IOException {
        List<String> duplicados = new ArrayList<>();
        List<String> sinPrecio = new ArrayList<>();
        int exitosos = 0;
        int totalProcesados = 0;

        try (Workbook workbook = new XSSFWorkbook(archivo.getInputStream())) {
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

                // Validar precio de venta
                BigDecimal precioVenta = parseBigDecimal(precioVentaStr);
                if (precioVenta == null || precioVenta.compareTo(BigDecimal.ZERO) <= 0) {
                    sinPrecio.add("Fila " + (i + 1) + ": " + nombre + " (sin precio de venta)");
                    continue;
                }

                // Verificar duplicado por nombre
                if (productosRepository.existsByNombre(nombre.trim().toLowerCase())) {
                    duplicados.add("Fila " + (i + 1) + ": " + nombre + " (ya existe)");
                    continue;
                }

                try {
                    // Validar partida
                    Partida partida = partidaValidacionesHelper.validaPartidaExistaString(partidaStr);

                    // Buscar o usar categoría por defecto
                    Categoria categoria = null;
                    if (categoriaNombre != null && !categoriaNombre.isBlank()) {
                        try {
                            categoria = categoriaValidacionesHelper.validarCategoriaActiva(categoriaNombre);
                        } catch (Exception e) {
                            // Si no encuentra la categoría, usar la primera disponible
                            categoria = null;
                        }
                    }

                    // Parsear unidad de medida
                    UnidadMedida unidadMedida = UnidadMedida.KILO;
                    if (unidadMedidaStr != null && !unidadMedidaStr.isBlank()) {
                        try {
                            unidadMedida = UnidadMedida.valueOf(unidadMedidaStr.trim().toUpperCase());
                        } catch (IllegalArgumentException e) {
                            unidadMedida = UnidadMedida.KILO;
                        }
                    }

                    // Parsear precio de compra
                    BigDecimal precioCompra = parseBigDecimal(precioCompraStr);

                    // Crear producto
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

    public byte[] generarPlantillaExcel() throws IOException {
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
