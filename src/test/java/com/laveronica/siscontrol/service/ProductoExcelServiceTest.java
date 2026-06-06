package com.laveronica.siscontrol.service;

import com.laveronica.siscontrol.domain.productos.dto.DatosReporteCargaProductos;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.repositories.ProductosRepository;
import com.laveronica.siscontrol.services.ProductoExcelService;
import com.laveronica.siscontrol.utils.helpers.CategoriaValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.PartidaValidacionesHelper;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductoExcelServiceTest {

    @Mock
    private ProductosRepository productosRepository;

    @Mock
    private PartidaValidacionesHelper partidaValidacionesHelper;

    @Mock
    private CategoriaValidacionesHelper categoriaValidacionesHelper;

    @InjectMocks
    private ProductoExcelService productoExcelService;

    @Test
    void generarPlantillaExcelReturnsBytes() throws IOException {
        byte[] result = productoExcelService.generarPlantillaExcel();

        assertThat(result).isNotNull();
        assertThat(result.length).isPositive();
    }

    @Test
    void cargarProductosDesdeExcelWithValidDataAndNullCategoria() throws IOException {
        byte[] excelContent = createExcelWithRow("Papa blanca", "FRUTASYVERDURAS", "Verduras", "PAP001", "KILO", "15.50", "22.00");

        MultipartFile archivo = createMockMultipartFile("test.xlsx", excelContent);

        given(productosRepository.existsByNombre("papa blanca")).willReturn(false);
        given(partidaValidacionesHelper.validaPartidaExistaString("FRUTASYVERDURAS")).willReturn(Partida.FRUTASYVERDURAS);
        given(categoriaValidacionesHelper.validarCategoriaActiva("Verduras"))
                .willThrow(new RuntimeException("Categoría no encontrada"));

        DatosReporteCargaProductos result = productoExcelService.cargarProductosDesdeExcel(archivo);

        assertThat(result).isNotNull();
        assertThat(result.totalProcesados()).isEqualTo(1);
    }

    @Test
    void cargarProductosDesdeExcelSinPrecio() throws IOException {
        byte[] excelContent = createExcelWithRow("Producto sin precio", "ABARROTES", "Comida", "COD001", "PIEZA", "10.00", "0");

        MultipartFile archivo = createMockMultipartFile("test.xlsx", excelContent);

        DatosReporteCargaProductos result = productoExcelService.cargarProductosDesdeExcel(archivo);

        assertThat(result).isNotNull();
        assertThat(result.sinPrecio()).isEqualTo(1);
        assertThat(result.mensajesSinPrecio()).isNotEmpty();
    }

    @Test
    void cargarProductosDesdeExcelDuplicado() throws IOException {
        byte[] excelContent = createExcelWithRow("Producto Duplicado", "ABARROTES", "Comida", "COD001", "PIEZA", "10.00", "25.00");

        MultipartFile archivo = createMockMultipartFile("test.xlsx", excelContent);

        given(productosRepository.existsByNombre("producto duplicado")).willReturn(true);

        DatosReporteCargaProductos result = productoExcelService.cargarProductosDesdeExcel(archivo);

        assertThat(result).isNotNull();
        assertThat(result.duplicados()).isEqualTo(1);
        assertThat(result.mensajesDuplicados()).isNotEmpty();
    }

    private byte[] createExcelWithRow(String nombre, String partida, String categoria, String codigo,
                                       String unidadMedida, String precioCompra, String precioVenta) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            var sheet = workbook.createSheet("Productos");
            var headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Nombre");
            headerRow.createCell(1).setCellValue("Partida");
            headerRow.createCell(2).setCellValue("Categoría");
            headerRow.createCell(3).setCellValue("Código");
            headerRow.createCell(4).setCellValue("Unidad Medida");
            headerRow.createCell(5).setCellValue("Precio Compra");
            headerRow.createCell(6).setCellValue("Precio Venta");

            var dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(nombre);
            dataRow.createCell(1).setCellValue(partida);
            dataRow.createCell(2).setCellValue(categoria);
            dataRow.createCell(3).setCellValue(codigo);
            dataRow.createCell(4).setCellValue(unidadMedida);
            dataRow.createCell(5).setCellValue(precioCompra);
            dataRow.createCell(6).setCellValue(precioVenta);

            var outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private MultipartFile createMockMultipartFile(String name, byte[] content) {
        return new MultipartFile() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getOriginalFilename() {
                return name;
            }

            @Override
            public String getContentType() {
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            }

            @Override
            public boolean isEmpty() {
                return content.length == 0;
            }

            @Override
            public long getSize() {
                return content.length;
            }

            @Override
            public byte[] getBytes() {
                return content;
            }

            @Override
            public ByteArrayInputStream getInputStream() {
                return new ByteArrayInputStream(content);
            }

            @Override
            public void transferTo(java.io.File dest) throws IOException {
                java.nio.file.Files.write(dest.toPath(), content);
            }
        };
    }
}
