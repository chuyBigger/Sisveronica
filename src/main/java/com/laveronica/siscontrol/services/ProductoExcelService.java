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
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoExcelService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductoExcelService.class);

    private final ProductosRepository productosRepository;
    private final PartidaValidacionesHelper partidaValidacionesHelper;
    private final CategoriaValidacionesHelper categoriaValidacionesHelper;

    public DatosReporteCargaProductos cargarProductosDesdeExcel(MultipartFile archivo, String partida) {
        validarArchivo(archivo);

        List<String> duplicados = new ArrayList<>();
        List<String> sinPrecio = new ArrayList<>();
        int exitosos = 0;
        int totalProcesados = 0;

        File tempFile = null;
        try {
            tempFile = Files.createTempFile("upload_", ".xlsx").toFile();
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(archivo.getBytes());
            }
            try (OPCPackage pkg = OPCPackage.open(tempFile);
                 Workbook workbook = new XSSFWorkbook(pkg)) {
                return procesarWorkbook(workbook, duplicados, sinPrecio, exitosos, totalProcesados, partida);
            }
        } catch (IOException | InvalidFormatException e) {
            LOG.error("Error al leer el archivo Excel", e);
            throw new RuntimeException("Error al leer el archivo Excel", e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    private DatosReporteCargaProductos procesarWorkbook(Workbook workbook, List<String> duplicados, List<String> sinPrecio, int exitosos, int totalProcesados, String partida) {
        Sheet sheet = workbook.getSheetAt(0);

        boolean esFormatoNuevo = detectarFormatoNuevo(sheet);
        int startRow = esFormatoNuevo ? 3 : 1;

        LOG.info("Excel carga: formatoNuevo={}, startRow={}, lastRowNum={}, partida={}",
                esFormatoNuevo, startRow, sheet.getLastRowNum(), partida);

        for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            totalProcesados++;

            String nombre, partidaStr, categoriaNombre, codigo, unidadMedidaStr;
            String precioCompraStr, precioVentaStr;
            String claveProductoServicio, claveUnidadMedida, impuestoStr, descuentosStr;
            String ieps1Str, ieps2Str, retencion1Tipo, retencion1Str;
            String retencion2Tipo, retencion2Str, retencion3Tipo, retencion3Str;
            String idExternoStr;

            if (esFormatoNuevo) {
                // New format: Codigo, Descripcion, Estatus, ClaveProductoServicio, ClaveUnidadMedida,
                // UnidadDeMedida, Precio, Impuesto, Descuentos, IEPS1, IEPS2, Retencion1_Tipo,
                // Retencion1, Retencion2_Tipo, Retencion2, Retencion3_Tipo, Retencion3, ID
                codigo = getCellValue(row, 0);
                nombre = getCellValue(row, 1);
                String estatus = getCellValue(row, 2);
                claveProductoServicio = getCellValue(row, 3);
                claveUnidadMedida = getCellValue(row, 4);
                unidadMedidaStr = getCellValue(row, 5);
                precioVentaStr = getCellValue(row, 6);
                impuestoStr = getCellValue(row, 7);
                descuentosStr = getCellValue(row, 8);
                ieps1Str = getCellValue(row, 9);
                ieps2Str = getCellValue(row, 10);
                retencion1Tipo = getCellValue(row, 11);
                retencion1Str = getCellValue(row, 12);
                retencion2Tipo = getCellValue(row, 13);
                retencion2Str = getCellValue(row, 14);
                retencion3Tipo = getCellValue(row, 15);
                retencion3Str = getCellValue(row, 16);
                idExternoStr = getCellValue(row, 17);
                partidaStr = null;
                categoriaNombre = null;
                precioCompraStr = null;
            } else {
                nombre = getCellValue(row, 0);
                partidaStr = getCellValue(row, 1);
                categoriaNombre = getCellValue(row, 2);
                codigo = getCellValue(row, 3);
                unidadMedidaStr = getCellValue(row, 4);
                precioCompraStr = getCellValue(row, 5);
                precioVentaStr = getCellValue(row, 6);
                claveProductoServicio = null;
                claveUnidadMedida = null;
                impuestoStr = null;
                descuentosStr = null;
                ieps1Str = null;
                ieps2Str = null;
                retencion1Tipo = null;
                retencion1Str = null;
                retencion2Tipo = null;
                retencion2Str = null;
                retencion3Tipo = null;
                retencion3Str = null;
                idExternoStr = null;
            }

            if (nombre == null || nombre.isBlank()) {
                LOG.info("Fila {}: nombre nulo/vacío, codigo={}", i + 1, codigo);
                totalProcesados--;
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
                Producto producto = new Producto();
                producto.setNombre(nombre.trim().toLowerCase());

                if (esFormatoNuevo) {
                    String partidaAsignada = (partida != null && !partida.isBlank()) ? partida : "GENERAL";
                    Partida partidaEnum = partidaValidacionesHelper.validaPartidaExistaString(partidaAsignada);
                    producto.setPartida(partidaEnum);
                    producto.setCodigo(codigo != null && !codigo.isBlank() ? codigo.trim().toUpperCase() : "PROD-" + System.currentTimeMillis());
                } else {
                    Partida partidaEnumOld = partidaValidacionesHelper.validaPartidaExistaString(partidaStr);
                    producto.setPartida(partidaEnumOld);

                    Categoria categoria = null;
                    if (categoriaNombre != null && !categoriaNombre.isBlank()) {
                        try {
                            categoria = categoriaValidacionesHelper.validarCategoriaActiva(categoriaNombre);
                        } catch (Exception e) {
                            categoria = null;
                        }
                    }
                    producto.setCategoria(categoria);
                    producto.setCodigo(codigo != null && !codigo.isBlank() ? codigo.trim().toUpperCase() : "PROD-" + System.currentTimeMillis());
                }

                UnidadMedida unidadMedida = UnidadMedida.KILO;
                if (unidadMedidaStr != null && !unidadMedidaStr.isBlank()) {
                    String umNormalized = unidadMedidaStr.trim().toUpperCase();
                    if ("KILOGRAMO".equals(umNormalized)) umNormalized = "KILO";
                    try {
                        unidadMedida = UnidadMedida.valueOf(umNormalized);
                    } catch (IllegalArgumentException e) {
                        unidadMedida = UnidadMedida.PIEZA;
                    }
                }
                producto.setUnidadMedida(unidadMedida);

                BigDecimal precioCompra = parseBigDecimal(precioCompraStr);
                producto.setPrecioCompra(precioCompra);
                producto.setPrecioVenta(precioVenta);
                producto.setClaveProductoServicio(claveProductoServicio);
                producto.setClaveUnidadMedida(claveUnidadMedida);
                producto.setImpuesto(parseBigDecimal(impuestoStr));
                producto.setDescuentos(parseBigDecimal(descuentosStr));
                producto.setIeps1(parseBigDecimal(ieps1Str));
                producto.setIeps2(parseBigDecimal(ieps2Str));
                producto.setRetencion1Tipo(retencion1Tipo);
                producto.setRetencion1(parseBigDecimal(retencion1Str));
                producto.setRetencion2Tipo(retencion2Tipo);
                producto.setRetencion2(parseBigDecimal(retencion2Str));
                producto.setRetencion3Tipo(retencion3Tipo);
                producto.setRetencion3(parseBigDecimal(retencion3Str));
                if (idExternoStr != null && !idExternoStr.isBlank()) {
                    try {
                        producto.setIdExterno(Long.parseLong(idExternoStr));
                    } catch (NumberFormatException ignored) {}
                }
                producto.setActivo(true);

                productosRepository.save(producto);
                exitosos++;

            } catch (Exception e) {
                duplicados.add("Fila " + (i + 1) + ": " + nombre + " (error: " + e.getMessage() + ")");
            }
        }

        LOG.info("Excel carga completada: totalProcesados={}, exitosos={}, duplicados={}, sinPrecio={}",
                totalProcesados, exitosos, duplicados.size(), sinPrecio.size());

        return new DatosReporteCargaProductos(
                totalProcesados,
                exitosos,
                duplicados.size(),
                sinPrecio.size(),
                duplicados,
                sinPrecio
        );
    }

    private boolean detectarFormatoNuevo(Sheet sheet) {
        if (sheet.getLastRowNum() < 2) return false;
        Row row2 = sheet.getRow(2);
        if (row2 == null) return false;
        String col0 = getCellValue(row2, 0);
        boolean result = "Codigo".equalsIgnoreCase(col0);
        LOG.info("detectarFormatoNuevo: row2col0='{}' -> {}", col0, result);
        return result;
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
            String[] headers = {"Nombre", "Partida", "Categoría", "Código", "Unidad Medida", "Precio Compra", "Precio Venta",
                    "ClaveProdServ", "ClaveUnidad", "Impuesto", "Descuentos", "IEPS1", "IEPS2",
                    "Ret1_Tipo", "Ret1", "Ret2_Tipo", "Ret2", "Ret3_Tipo", "Ret3", "ID_Externo"};
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

            String[] headers = {"Nombre", "Partida", "Categoría", "Código", "Unidad Medida", "Precio Compra", "Precio Venta",
                    "ClaveProdServ", "ClaveUnidad", "Impuesto", "Descuentos", "IEPS1", "IEPS2",
                    "Ret1_Tipo", "Ret1", "Ret2_Tipo", "Ret2", "Ret3_Tipo", "Ret3", "ID_Externo"};
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
                row.createCell(7).setCellValue(p.getClaveProductoServicio() != null ? p.getClaveProductoServicio() : "");
                row.createCell(8).setCellValue(p.getClaveUnidadMedida() != null ? p.getClaveUnidadMedida() : "");
                row.createCell(9).setCellValue(p.getImpuesto() != null ? p.getImpuesto().doubleValue() : 0);
                row.createCell(10).setCellValue(p.getDescuentos() != null ? p.getDescuentos().doubleValue() : 0);
                row.createCell(11).setCellValue(p.getIeps1() != null ? p.getIeps1().doubleValue() : 0);
                row.createCell(12).setCellValue(p.getIeps2() != null ? p.getIeps2().doubleValue() : 0);
                row.createCell(13).setCellValue(p.getRetencion1Tipo() != null ? p.getRetencion1Tipo() : "");
                row.createCell(14).setCellValue(p.getRetencion1() != null ? p.getRetencion1().doubleValue() : 0);
                row.createCell(15).setCellValue(p.getRetencion2Tipo() != null ? p.getRetencion2Tipo() : "");
                row.createCell(16).setCellValue(p.getRetencion2() != null ? p.getRetencion2().doubleValue() : 0);
                row.createCell(17).setCellValue(p.getRetencion3Tipo() != null ? p.getRetencion3Tipo() : "");
                row.createCell(18).setCellValue(p.getRetencion3() != null ? p.getRetencion3().doubleValue() : 0);
                row.createCell(19).setCellValue(p.getIdExterno() != null ? p.getIdExterno() : 0);
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

        CellType cellType = cell.getCellType();
        if (cellType == CellType.FORMULA) {
            cellType = cell.getCachedFormulaResultType();
        }

        return switch (cellType) {
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
