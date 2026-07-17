# Reporte de Funciones — SisVeronica

> Generado el 28/06/2026. Backend: Java 21 + Spring Boot 3.5.4. Frontend: Angular 21 + Material. Total: ~436 funciones.

---

## Índice

- [1. Backend (Java)](#1-backend-java)
  - [1.1 Controllers](#11-controllers)
    - [1.1.1 AuthController](#111-authcontroller)
    - [1.1.2 CategoriaController](#112-categoriacontroller)
    - [1.1.3 ClienteController](#113-clientecontroller)
    - [1.1.4 ContratosController](#114-contratoscontroller)
    - [1.1.5 EnumsController](#115-enumscontroller)
    - [1.1.6 ExtraController](#116-extracontroller)
    - [1.1.7 FacturaController](#117-facturacontroller)
    - [1.1.8 NotaCancelacionController](#118-notacancelacioncontroller)
    - [1.1.9 NotaVentaController](#119-notaventacontroller)
    - [1.1.10 OrdenCompraController](#1110-ordencompracontroller)
    - [1.1.11 ProductoController](#1111-productocontroller)
    - [1.1.12 ProductoExcelController](#1112-productoexcelcontroller)
    - [1.1.13 ReporteProduccionController](#1113-reporteproduccioncontroller)
    - [1.1.14 SuperAdminController](#1114-superadmincontroller)
    - [1.1.15 UsuarioAdminController](#1115-usuarioadmincontroller)
  - [1.2 Services](#12-services)
    - [1.2.1 AuthService](#121-authservice)
    - [1.2.2 CategoriaService](#122-categoriaservice)
    - [1.2.3 ClienteService](#123-clienteservice)
    - [1.2.4 ContratoService](#124-contratoservice)
    - [1.2.5 ExtraService](#125-extraservice)
    - [1.2.6 FacturaService](#126-facturaservice)
    - [1.2.7 NotaCancelacionService](#127-notacancelacionservice)
    - [1.2.8 NotaVentaDetalleService](#128-notaventadetalleservice)
    - [1.2.9 NotaVentaService](#129-notaventaservice)
    - [1.2.10 OrdenCompraDetalleService](#1210-ordencompradetalleservice)
    - [1.2.11 OrdenCompraService](#1211-ordencompraservice)
    - [1.2.12 ProductoExcelService](#1212-productoexcleservice)
    - [1.2.13 ProductoService](#1213-productoservice)
    - [1.2.14 ReporteProduccionService](#1214-reporteproduccionservice)
    - [1.2.15 UsuarioAdminService](#1215-usuarioadminservice)
  - [1.3 Helpers / Validaciones](#13-helpers--validaciones)
  - [1.4 Infrastructure / Security](#14-infrastructure--security)
  - [1.5 Domain — Entities](#15-domain--entities)
  - [1.6 Domain — Validators & Enums](#16-domain--validators--enums)
  - [1.7 Repositories](#17-repositories)
- [2. Frontend (Angular / TypeScript)](#2-frontend-angular--typescript)
  - [2.1 App & Layout](#21-app--layout)
  - [2.2 Auth / Guards](#22-auth--guards)
  - [2.3 Config / Super Admin](#23-config--super-admin)
  - [2.4 Categorías](#24-categorías)
  - [2.5 Productos](#25-productos)
  - [2.6 Clientes](#26-clientes)
  - [2.7 Contratos](#27-contratos)
  - [2.8 Notas de Venta](#28-notas-de-venta)
  - [2.9 Órdenes de Compra](#29-órdenes-de-compra)
  - [2.10 Reportes](#210-reportes)
  - [2.11 Servicios Frontend](#211-servicios-frontend)
- [3. Totales](#3-totales)

---

# 1. Backend (Java)

## 1.1 Controllers

| # | Clase | Método | Endpoint |
|---|-------|--------|----------|
| 1 | AuthController | `login(DatosLogin)` | `POST /auth/login` |
| 2 | AuthController | `register(DatosRegistroUsuario)` | `POST /auth/register` |
| 3 | CategoriaController | `registrar(DatosRegistroCategoria, UriComponentsBuilder)` | `POST /categorias` |
| 4 | CategoriaController | `listarCategorias()` | `GET /categorias` |
| 5 | CategoriaController | `buscarCategoriaId(String id)` | `GET /categorias/{id}` |
| 6 | CategoriaController | `actualizaCategoriaid(String id, DatosActualizarCategoria)` | `PATCH /categorias/{id}` |
| 7 | CategoriaController | `eliminarCategoria(String id)` | `DELETE /categorias/{id}` |
| 8 | ClienteController | `registrar(DatosRegistroCliente, UriComponentsBuilder)` | `POST /clientes` |
| 9 | ClienteController | `listarClietes()` | `GET /clientes` |
| 10 | ClienteController | `mostrarClienteId(String id)` | `GET /clientes/{id}` |
| 11 | ClienteController | `actulizarCliente(String id, DatosActualizarCliente)` | `PATCH /clientes/{id}` |
| 12 | ClienteController | `eliminarCliente(String id)` | `DELETE /clientes/{id}` |
| 13 | ContratosController | `registrar(DatosRegistroContrato, UriComponentsBuilder)` | `POST /contratos` |
| 14 | ContratosController | `contratosLista()` | `GET /contratos` |
| 15 | ContratosController | `contratoBuscarId(String id)` | `GET /contratos/{id}` |
| 16 | ContratosController | `actualizarContrato(String id, DatosActualizarContrato)` | `PATCH /contratos/{id}` |
| 17 | ContratosController | `eliminarContrato(String id)` | `DELETE /contratos/{id}` |
| 18 | EnumsController | `listarPartidas()` | `GET /enums/partidas` |
| 19 | EnumsController | `listarUnidadesMedida()` | `GET /enums/unidades_medida` |
| 20 | EnumsController | `listarModulos()` | `GET /enums/modulos` |
| 21 | EnumsController | `listarAcciones()` | `GET /enums/acciones` |
| 22 | ExtraController | `crear(DatosRegistroExtra, UserDetails)` | `POST /extras` |
| 23 | ExtraController | `listarPorOrden(String ordenCompraId)` | `GET /extras/orden/{ordenCompraId}` |
| 24 | ExtraController | `firmar(String id, UserDetails)` | `POST /extras/{id}/firmar` |
| 25 | ExtraController | `eliminar(String id)` | `DELETE /extras/{id}` |
| 26 | FacturaController | `generarFactura(DatosRegistroFactura)` | `POST /facturas` |
| 27 | FacturaController | `listarFacturas()` | `GET /facturas` |
| 28 | FacturaController | `obtenerPorId(String id)` | `GET /facturas/{id}` |
| 29 | FacturaController | `obtenerPorOrdenCompraId(String ordenCompraId)` | `GET /facturas/orden/{ordenCompraId}` |
| 30 | FacturaController | `generarFacturaExtras(DatosRegistroFactura)` | `POST /facturas/extras` |
| 31 | FacturaController | `obtenerFacturaExtrasPorOrdenCompraId(String ordenCompraId)` | `GET /facturas/extras/orden/{ordenCompraId}` |
| 32 | NotaCancelacionController | `crear(DatosRegistroCancelacion, UserDetails)` | `POST /cancelaciones` |
| 33 | NotaCancelacionController | `listarPorOrden(String ordenCompraId)` | `GET /cancelaciones/orden/{ordenCompraId}` |
| 34 | NotaCancelacionController | `validar(String id, UserDetails)` | `POST /cancelaciones/{id}/validar` |
| 35 | NotaCancelacionController | `eliminar(String id)` | `DELETE /cancelaciones/{id}` |
| 36 | NotaCancelacionController | `reconstruir(String ordenCompraId)` | `POST /cancelaciones/{ordenCompraId}/reconstruir` |
| 37 | NotaVentaController | `registrarNota(DatosRegistroNota, UriComponentsBuilder)` | `POST /notaventas` |
| 38 | NotaVentaController | `generarNotaDesdeOrden(DatosGenerarNotaDesdeOrden)` | `POST /notaventas/generar-desde-orden` |
| 39 | NotaVentaController | `listaNotas(Pageable)` | `GET /notaventas` |
| 40 | NotaVentaController | `buscarNotaId(String id)` | `GET /notaventas/{id}` |
| 41 | NotaVentaController | `actualizarNota(String id, DatosActualizarNota)` | `PATCH /notaventas/{id}` |
| 42 | NotaVentaController | `eliminarNota(String id)` | `DELETE /notaventas/{id}` |
| 43 | NotaVentaController | `firmarNota(String id)` | `POST /notaventas/{id}/firmar` |
| 44 | NotaVentaController | `actualizarDetalle(String id, Map)` | `PATCH /notaventas/{id}/detalle` |
| 45 | OrdenCompraController | `registrarOrdenCompra(DatosRegistroOrdenCompra, UriComponentsBuilder)` | `POST /orden_compra` |
| 46 | OrdenCompraController | `listarOrdenCompra(LocalDate, Pageable)` | `GET /orden_compra` |
| 47 | OrdenCompraController | `buscarOrdenCompraId(String id)` | `GET /orden_compra/{id}` |
| 48 | OrdenCompraController | `actilizarOdrdenCompra(String id, DatosActulizarOrdenCompra)` | `PATCH /orden_compra/{id}` |
| 49 | OrdenCompraController | `eliminarOrdenCompra(String id)` | `DELETE /orden_compra/{id}` |
| 50 | OrdenCompraController | `confirmarOrdenCompra(String id)` | `POST /orden_compra/{id}/confirmar` |
| 51 | OrdenCompraController | `listarNotasPorOrden(String id)` | `GET /orden_compra/{id}/notas` |
| 52 | OrdenCompraController | `generarTodasNotas(String id)` | `POST /orden_compra/{id}/generar-notas` |
| 53 | ProductoController | `registrar(DatosRegistroProducto, UriComponentsBuilder)` | `POST /productos` |
| 54 | ProductoController | `listarPodructo(Pageable)` | `GET /productos` |
| 55 | ProductoController | `listarProductosPartida(String partida, Pageable)` | `GET /productos/partidas/{partida}` |
| 56 | ProductoController | `listarProductoCategoria(String id, Pageable)` | `GET /productos/categorias/{id}` |
| 57 | ProductoController | `buscarProductoId(String id)` | `GET /productos/{id}` |
| 58 | ProductoController | `buscarProductoNombre(String nombre)` | `GET /productos/buscar/{nombre}` |
| 59 | ProductoController | `buscarProductosPorPalabra(String, Pageable)` | `GET /productos/buscar_palabras` |
| 60 | ProductoController | `actualizarProductoId(String id, DatosActualizarProducto)` | `PATCH /productos/{id}` |
| 61 | ProductoController | `eliminarProducto(String id)` | `DELETE /productos/{id}` |
| 62 | ProductoExcelController | `cargarProductos(MultipartFile)` | `POST /productos/excel/cargar` |
| 63 | ProductoExcelController | `descargarPlantilla()` | `GET /productos/excel/plantilla` |
| 64 | ReporteProduccionController | `reporteSemanal(LocalDate)` | `GET /reportes/produccion` |
| 65 | SuperAdminController | `eliminarNotaPorId(String id)` | `DELETE /admin/super/notas/{id}` |
| 66 | SuperAdminController | `eliminarNotaPorFolio(Integer folio)` | `DELETE /admin/super/notas/folio/{folio}` |
| 67 | SuperAdminController | `eliminarOrdenPorId(String id)` | `DELETE /admin/super/ordenes/{id}` |
| 68 | SuperAdminController | `eliminarCancelacionPorId(String id)` | `DELETE /admin/super/cancelaciones/{id}` |
| 69 | UsuarioAdminController | `listar()` | `GET /admin/usuarios` |
| 70 | UsuarioAdminController | `buscar(String id)` | `GET /admin/usuarios/{id}` |
| 71 | UsuarioAdminController | `crear(DatosRegistroUsuario)` | `POST /admin/usuarios` |
| 72 | UsuarioAdminController | `asignarPermisos(String id, DatosPermisoUsuario)` | `PATCH /admin/usuarios/{id}/permisos` |
| 73 | UsuarioAdminController | `toggleUsuario(String id)` | `PATCH /admin/usuarios/{id}/toggle` |
| 74 | UsuarioAdminController | `actualizar(String id, DatosActualizarUsuario)` | `PATCH /admin/usuarios/{id}` |

## 1.2 Services

| # | Clase | Método |
|---|-------|--------|
| 75 | AuthService | `login(DatosLogin): DatosRespuestaAuth` |
| 76 | AuthService | `register(DatosRegistroUsuario): DatosRespuestaAuth` |
| 77 | CategoriaService | `registrarCategoria(DatosRegistroCategoria): Categoria` |
| 78 | CategoriaService | `listaCategorias(): List<DatosDetalleCategoria>` |
| 79 | CategoriaService | `buscarCategoriaId(String id): Categoria` |
| 80 | CategoriaService | `actualizarCategoria(String id, DatosActualizarCategoria): Categoria` |
| 81 | CategoriaService | `eliminarCategoria(String id): void` |
| 82 | ClienteService | `registarCliente(DatosRegistroCliente): Cliente` |
| 83 | ClienteService | `buscarTodos(): List<DatosDetalleCliente>` |
| 84 | ClienteService | `buscarClienteId(String id): DatosDetalleCliente` |
| 85 | ClienteService | `actualizarCliente(String id, DatosActualizarCliente): DatosDetalleCliente` |
| 86 | ClienteService | `eliminarCliente(String id): void` |
| 87 | ContratoService | `registrarContrato(DatosRegistroContrato): Contrato` |
| 88 | ContratoService | `listarContratos(): List<DatosDetalleContrato>` |
| 89 | ContratoService | `buscarContratoId(String id): DatosDetalleContrato` |
| 90 | ContratoService | `actualizarContratoId(String id, DatosActualizarContrato): DatosDetalleContrato` |
| 91 | ContratoService | `eliminarContrato(String id): void` |
| 92 | ExtraService | `crearExtra(DatosRegistroExtra, String): DatosListarExtra` |
| 93 | ExtraService | `listarPorOrden(String): List<DatosListarExtra>` |
| 94 | ExtraService | `firmarExtra(String, String): DatosListarExtra` |
| 95 | ExtraService | `eliminarExtra(String): void` |
| 96 | FacturaService | `generarFactura(DatosRegistroFactura): DatosListarFactura` |
| 97 | FacturaService | `generarFacturaExtras(DatosRegistroFactura): DatosListarFactura` |
| 98 | FacturaService | `listarFacturas(): List<DatosListarFactura>` |
| 99 | FacturaService | `obtenerPorId(String): Optional<DatosListarFactura>` |
| 100 | FacturaService | `obtenerPorOrdenCompraId(String): Optional<DatosListarFactura>` |
| 101 | FacturaService | `obtenerFacturaExtrasPorOrdenCompraId(String): Optional<DatosListarFactura>` |
| 102 | NotaCancelacionService | `crearCancelacion(DatosRegistroCancelacion, String): DatosListarCancelacion` |
| 103 | NotaCancelacionService | `listarPorOrden(String): List<DatosListarCancelacion>` |
| 104 | NotaCancelacionService | `validarCancelacion(String, String): DatosListarCancelacion` |
| 105 | NotaCancelacionService | `eliminarCancelacion(String): void` |
| 106 | NotaCancelacionService | `reconstruirNotas(String): List<DatosDetalleNota>` |
| 107 | NotaVentaDetalleService | `registrarNuevaListaNotaVentasDetalles(List, NotaVenta): List<NotaVentaDetalle>` |
| 108 | NotaVentaDetalleService | `agregarUnDetalleNuevo(NotaVentaDetalle): NotaVentaDetalle` |
| 109 | NotaVentaDetalleService | `calcularSubTotal(Integer, BigDecimal): BigDecimal` (static) |
| 110 | NotaVentaDetalleService | `calcularTotalGeneral(List<NotaVentaDetalle>): BigDecimal` (static) |
| 111 | NotaVentaService | `registrarNota(DatosRegistroNota): DatosDetalleNota` |
| 112 | NotaVentaService | `generarNotaDesdeOrden(DatosGenerarNotaDesdeOrden): DatosDetalleNota` |
| 113 | NotaVentaService | `listarNotas(Pageable): Page<DatosListarNota>` |
| 114 | NotaVentaService | `buscarNotaId(String): DatosDetalleNota` |
| 115 | NotaVentaService | `actualizarNota(String, DatosActualizarNota): DatosDetalleNota` |
| 116 | NotaVentaService | `eliminarNota(String): void` |
| 117 | NotaVentaService | `firmarNota(String): DatosDetalleNota` |
| 118 | NotaVentaService | `actualizarDetalle(String, String): DatosDetalleNota` |
| 119 | OrdenCompraDetalleService | `registrarListaDetallesOrdenCompra(List, OrdenCompra): List<OrdenCompraDetalle>` |
| 120 | OrdenCompraDetalleService | `actualizarListaDetallesOrdenCompra(List, OrdenCompra): void` |
| 121 | OrdenCompraService | `registrarOrdenCompra(DatosRegistroOrdenCompra): DatosDetalleOrdenCompra` |
| 122 | OrdenCompraService | `listarOrdenesCompra(Pageable): Page<DatosListarOrdenCompra>` |
| 123 | OrdenCompraService | `listarOrdenesCompraPorFecha(LocalDate, Pageable): Page<DatosListarOrdenCompra>` |
| 124 | OrdenCompraService | `buscarOrdenCompraId(String): DatosDetalleOrdenCompra` |
| 125 | OrdenCompraService | `actulizarOrdenCompraId(String, DatosActulizarOrdenCompra): DatosDetalleOrdenCompra` |
| 126 | OrdenCompraService | `eliminarOrdenCompra(String): void` |
| 127 | OrdenCompraService | `confirmarOrdenCompra(String, String): DatosDetalleOrdenCompra` |
| 128 | OrdenCompraService | `listarNotasPorOrden(String): List<DatosListarNota>` |
| 129 | OrdenCompraService | `generarTodasNotas(String): List<DatosDetalleNota>` |
| 130 | ProductoExcelService | `cargarProductosDesdeExcel(MultipartFile): DatosReporteCargaProductos` |
| 131 | ProductoExcelService | `generarPlantillaExcel(): byte[]` |
| 132 | ProductoService | `registrarProducto(DatosRegistroProducto): DatosDetalleProducto` |
| 133 | ProductoService | `listaProductos(Pageable): Page<DatosListarProductos>` |
| 134 | ProductoService | `listaProductosPartida(Pageable, String): Page<DatosListarProductos>` |
| 135 | ProductoService | `listaProdictosCategoriaId(String, Pageable): Page<DatosListarProductos>` |
| 136 | ProductoService | `buscarProductoId(String): DatosDetalleProducto` |
| 137 | ProductoService | `buscarProductoNombre(String): DatosDetalleProducto` |
| 138 | ProductoService | `buscarProductosPorPalabra(String, Pageable): Page` |
| 139 | ProductoService | `actualizarProductoId(String, DatosActualizarProducto): DatosDetalleProducto` |
| 140 | ProductoService | `eliminarProducto(String): void` |
| 141 | ReporteProduccionService | `generarReporte(LocalDate): DatosReporteProduccionCarne` |
| 142 | UsuarioAdminService | `listarUsuarios(): List<DatosUsuarioAdmin>` |
| 143 | UsuarioAdminService | `buscarUsuario(String): DatosDetalleUsuario` |
| 144 | UsuarioAdminService | `crearUsuario(DatosRegistroUsuario): DatosRespuestaAuth` |
| 145 | UsuarioAdminService | `asignarPermisos(String, List<DatosPermiso>): void` |
| 146 | UsuarioAdminService | `toggleUsuario(String): void` |
| 147 | UsuarioAdminService | `actualizarUsuario(String, DatosActualizarUsuario): DatosUsuarioAdmin` |

## 1.3 Helpers / Validaciones

| # | Clase | Método |
|---|-------|--------|
| 148 | CategoriaValidacionesHelper | `validarCategoriaActiva(String id): Categoria` |
| 149 | ClienteValidacionesHelper | `validaClienteExista(String): Cliente` |
| 150 | ClienteValidacionesHelper | `validaClienteExistaId(String): Cliente` |
| 151 | ContratoValidacionesHelper | `validaContratoExisteId(String): Contrato` |
| 152 | ContratoValidacionesHelper | `validaContratoExiste(String): void` |
| 153 | ContratoValidacionesHelper | `buscarContratoExisteId(String): DatosDetalleContrato` |
| 154 | NotaVentaDetalleValidacionesHelper | `detalleProductoExiste(String, String): NotaVentaDetalle` |
| 155 | NotaVentaValidacionesHelper | `notaVentaExiste(String): NotaVenta` |
| 156 | OrdenCompraValidacionesHelper | `validaOrdenCompraExiste(String, LocalDate, Partida): void` |
| 157 | OrdenCompraValidacionesHelper | `validaOrdenCompraExisteAlActualizar(String, String, LocalDate, Partida): void` |
| 158 | OrdenCompraValidacionesHelper | `buscarOrdenCompraId(String): OrdenCompra` |
| 159 | PartidaValidacionesHelper | `validaPartidaExistaString(String): Partida` |
| 160 | ProductoValidacionesHelper | `encontrarProductoId(String): Producto` |
| 161 | ProductoValidacionesHelper | `encontrarProductoNombre(String): Producto` |
| 162 | ProductoValidacionesHelper | `validarNombreNoExista(DatosRegistroProducto): String` |
| 163 | StringValidacionesHelper | `normalizadorcodigosPersitecia(String): String` |
| 164 | StringValidacionesHelper | `normalizadoTextosPersistecia(String): String` |
| 165 | UnidadMedidaValidacionesHelper | `validar(String): UnidadMedida` |

## 1.4 Infrastructure / Security

| # | Clase | Método |
|---|-------|--------|
| 166 | CustomUserDetailsService | `loadUserByUsername(String): UserDetails` |
| 167 | JwtAuthFilter | `doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain): void` |
| 168 | JwtUtil | `generateToken(UserDetails): String` |
| 169 | JwtUtil | `extractUsername(String): String` |
| 170 | JwtUtil | `validateToken(String, UserDetails): boolean` |
| 171 | SecurityConfig | `securityFilterChain(HttpSecurity): SecurityFilterChain` |
| 172 | SecurityConfig | `authenticationProvider(): DaoAuthenticationProvider` |
| 173 | SecurityConfig | `passwordEncoder(): PasswordEncoder` |
| 174 | SecurityConfig | `authenticationManager(AuthenticationConfiguration): AuthenticationManager` |
| 175 | SecurityConfig | `corsConfigurationSource(): CorsConfigurationSource` |
| 176 | WebConfig | `addCorsMappings(CorsRegistry): void` |
| 177 | ManejoErrores | `handleValidationExceptions(MethodArgumentNotValidException): ResponseEntity` |
| 178 | ManejoErrores | `handleRecursoExistente(RecursoExistenteException): ResponseEntity` |
| 179 | ManejoErrores | `handleTypeMismatch(MethodArgumentTypeMismatchException): ResponseEntity` |
| 180 | ManejoErrores | `handleBadRequest(HttpMessageNotReadableException): ResponseEntity` |
| 181 | ManejoErrores | `handleResourceNotFound(ResourceNotFoundException): ResponseEntity` |
| 182 | ManejoErrores | `handleMethodNotSupported(HttpRequestMethodNotSupportedException): ResponseEntity` |
| 183 | ManejoErrores | `handleNoHandlerFound(NoHandlerFoundException): ResponseEntity` |
| 184 | ManejoErrores | `handleOtherExceptions(Exception): ResponseEntity` |
| 185 | ManejoErrores | `handleIllegalArgument(IllegalArgumentException): ResponseEntity` |
| 186 | ManejoErrores | `RulesValidationException(RuleValidationException): ResponseEntity` |
| 187 | ManejoErrores | `handleUsernameNotFound(UsernameNotFoundException): ResponseEntity` |
| 188 | ManejoErrores | `handleBadCredentials(BadCredentialsException): ResponseEntity` |
| 189 | OpenApiConfig | `customOpenAPI(): OpenAPI` |
| 190 | SisVeronicaApplication | `main(String[]): void` |

## 1.5 Domain — Entities

| # | Clase | Método |
|---|-------|--------|
| 191 | Categoria | `constructor(DatosRegistroCategoria)` — normaliza nombre a mayúsculas |
| 192 | Cliente | `constructor(DatosRegistroCliente)` |
| 193 | Contrato | `constructor(DatosRegistroContrato, Cliente)` |
| 194 | Extra | `agregarDetalle(ExtraDetalle): void` |
| 195 | Factura | `agregarDetalle(FacturaDetalle): void` |
| 196 | NotaCancelacion | `agregarDetalle(NotaCancelacionDetalle): void` |
| 197 | NotaVenta | `agregarDetalles(NotaVentaDetalle): void` |
| 198 | NotaVenta | `constructor(Cliente, Partida)` |
| 199 | NotaVentaDetalle | `constructor(Integer, Producto, NotaVenta)` |
| 200 | OrdenCompra | `agregarDetalles(OrdenCompraDetalle): void` |
| 201 | OrdenCompra | `constructor(DatosRegistroOrdenCompra, Cliente, Contrato, Partida, LocalDate)` |
| 202 | OrdenCompraDetalle | `constructor(DatosRegistroOrdenCompraDetalle, Producto, OrdenCompra)` |
| 203 | Usuario | `constructor(String, String, Role)` |

## 1.6 Domain — Validators & Enums

| # | Clase | Método |
|---|-------|--------|
| 204 | ValidarNombreCategoria | `validar(DatosRegistroCategoria): void` |
| 205 | ValidacionNombreNoExista | `validar(DatosRegistroProducto): void` |
| 206 | RfcValidacion | `isValid(String, ConstraintValidatorContext): boolean` |
| 207 | DiaSemana (enum) | `calcularFecha(LocalDate): LocalDate` |
| 208 | DiaSemana (enum) | `getCantidad(OrdenCompraDetalle): Double` |
| 209 | DiaSemana (enum) | `fromString(String): DiaSemana` |
| 210 | Partida (enum) | `fromString(String): Partida` |
| 211 | UnidadMedida (enum) | `fromString(String): UnidadMedida` |

## 1.7 Repositories

| # | Clase | Método |
|---|-------|--------|
| 212 | CategoriaRepository | `existsByNombre(String): boolean` |
| 213 | CategoriaRepository | `findByIdAndActivoTrue(String): Optional<Categoria>` |
| 214 | ClienteRepository | `existsByNombre(String): boolean`, `findByNombre(String): Cliente` |
| 215 | ContratoRepository | `existsByContrato(String): boolean`, `findByIdAndActivoTrue(String): Optional`, `findByContratoAndActivoTrue(String): Optional`, `findAllByActivoTrue(): List` |
| 216 | ExtraRepository | `findByOrdenCompraIdAndActivoTrue(String): List`, `findByIdAndActivoTrue(String): Optional`, `findMaxFolio(): Integer` |
| 217 | FacturaRepository | `obtenerMaxFolio(): Integer`, (7 métodos de búsqueda por orden/activo/esExtras), `findOrdenCompraIdsWithFactura(Set): Set` |
| 218 | NotaCancelacionRepository | (4 métodos: findByOrdenCompra, findById, findByDia, findCancelacionCounts) |
| 219 | NotaVentaDetalleRepository | `findByNotaVenta_IdAndProducto_NombreIgnoreCase(String, String): Optional` |
| 220 | NotaVentaRepository | (7 métodos: findAllByActivoTrue, findById, findMaxFolio, findByFolio, findByOrdenCompraId, findByDia, findNotaCountsByOrdenCompraIds) |
| 221 | OrdenCompraRespository | (6 métodos: exists, findByAndActivoTrue, findByIdAndActivoTrue, findByIdAndActivoTrueWithLock, findByFechaInPeriodo, findByActivoTrueAndFechaInicioSemanaAndPartida) |
| 222 | ProductosRepository | (7 métodos: existsByNombre, findAllByActivoTrue, findAllByPartida, findAllByCategoria, findByIdAndActivoTrue, findByNombreAndActivoTrue, findAllByNombreContaining) |
| 223 | UsuarioPermisoRepository | `findByUsuarioId(String): List`, `deleteByUsuarioId(String): void` |
| 224 | UsuarioRepository | `findByUsernameAndActivoTrue(String): Optional`, `existsByUsername(String): boolean` |

---

# 2. Frontend (Angular / TypeScript)

## 2.1 App & Layout

| # | Archivo | Clase | Método |
|---|---------|-------|--------|
| 225 | app.ts | App | `ngOnInit(): void` |
| 226 | app.ts | App | `ngOnDestroy(): void` |
| 227 | app.ts | App | `actualizarReloj(): void` |
| 228 | app.ts | App | `irASettings(): void` |
| 229 | app.ts | App | `toggleDarkMode(): void` |
| 230 | app.ts | App | `toggleSidebar(): void` |
| 231 | app.ts | App | `logout(): void` |
| 232 | sidebar.component.ts | SidebarComponent | `logout(): void` |
| 233 | dashboard.component.ts | DashboardComponent | `ngOnInit(): void` |
| 234 | dashboard.component.ts | DashboardComponent | `cargarConteos(): void` |
| 235 | dashboard.component.ts | DashboardComponent | `navigateTo(route: string): void` |

## 2.2 Auth / Guards

| # | Archivo | Clase | Método |
|---|---------|-------|--------|
| 236 | login.component.ts | LoginComponent | `login(): void` |
| 237 | auth.guard.ts | AuthGuard | `canActivate(): boolean` |
| 238 | auth.guard.ts | AdminGuard | `canActivate(): boolean` |
| 239 | jwt.interceptor.ts | *(función)* | `jwtInterceptor(req, next): Observable<HttpEvent>` |

## 2.3 Config / Super Admin

| # | Archivo | Clase | Método |
|---|---------|-------|--------|
| 240 | config.component.ts | ConfigComponent | `ngOnInit(): void` |
| 241 | config.component.ts | ConfigComponent | `iniciarMatriz(): void` |
| 242 | config.component.ts | ConfigComponent | `cargarUsuarios(): void` |
| 243 | config.component.ts | ConfigComponent | `seleccionarUsuario(u): void` |
| 244 | config.component.ts | ConfigComponent | `cargarPermisosEnMatriz(permisos): void` |
| 245 | config.component.ts | ConfigComponent | `getPermisosSeleccionados(): array` |
| 246 | config.component.ts | ConfigComponent | `guardarPermisos(): void` |
| 247 | config.component.ts | ConfigComponent | `guardarPerfil(): void` |
| 248 | config.component.ts | ConfigComponent | `toggleUsuario(u): void` |
| 249 | config.component.ts | ConfigComponent | `crearUsuario(): void` |
| 250 | config.component.ts | ConfigComponent | `limpiarFormulario(): void` |
| 251 | config.component.ts | ConfigComponent | `nuevoUsuario(): void` |
| 252 | config.component.ts | ConfigComponent | `tienePermiso(mod, acc): boolean` |
| 253 | config.component.ts | ConfigComponent | `nombreModulo(mod): string` |
| 254 | config.component.ts | ConfigComponent | `nombreAccion(acc): string` |
| 255 | super-admin.component.ts | SuperAdminComponent | `eliminar(): void` |
| 256 | super-admin.component.ts | SuperAdminComponent | `buildRequest(val): Observable` (private) |

## 2.4 Categorías

| # | Archivo | Clase | Método |
|---|---------|-------|--------|
| 257 | categoria-dialog.component.ts | CategoriaDialogComponent | `ngOnInit(): void` |
| 258 | categoria-dialog.component.ts | CategoriaDialogComponent | `onNombreInput(event): void` |
| 259 | categoria-dialog.component.ts | CategoriaDialogComponent | `onEditNombreInput(event): void` |
| 260 | categoria-dialog.component.ts | CategoriaDialogComponent | `cargarPartidas(): void` |
| 261 | categoria-dialog.component.ts | CategoriaDialogComponent | `cargarCategorias(): void` |
| 262 | categoria-dialog.component.ts | CategoriaDialogComponent | `guardar(): void` |
| 263 | categoria-dialog.component.ts | CategoriaDialogComponent | `editar(cat): void` |
| 264 | categoria-dialog.component.ts | CategoriaDialogComponent | `cancelarEdicion(): void` |
| 265 | categoria-dialog.component.ts | CategoriaDialogComponent | `actualizar(): void` |
| 266 | categoria-dialog.component.ts | CategoriaDialogComponent | `eliminar(cat): void` |

## 2.5 Productos

| # | Archivo | Clase | Método |
|---|---------|-------|--------|
| 267 | producto-lista.component.ts | ProductoListaComponent | `ngAfterViewInit(): void` |
| 268 | producto-lista.component.ts | ProductoListaComponent | `cargarProductos(): void` |
| 269 | producto-lista.component.ts | ProductoListaComponent | `onPageChange(): void` |
| 270 | producto-lista.component.ts | ProductoListaComponent | `filtrarPorPartida(): void` |
| 271 | producto-lista.component.ts | ProductoListaComponent | `buscar(): void` |
| 272 | producto-lista.component.ts | ProductoListaComponent | `limpiarBusqueda(): void` |
| 273 | producto-lista.component.ts | ProductoListaComponent | `abrirPreview(producto): void` |
| 274 | producto-lista.component.ts | ProductoListaComponent | `abrirExcelDialog(): void` |
| 275 | producto-lista.component.ts | ProductoListaComponent | `abrirCategorias(): void` |
| 276 | producto-lista.component.ts | ProductoListaComponent | `confirmarEliminar(id, nombre): void` |
| 277 | producto-form.component.ts | ProductoFormComponent | `ngOnInit(): void` |
| 278 | producto-form.component.ts | ProductoFormComponent | `cargarCatalogos(): void` |
| 279 | producto-form.component.ts | ProductoFormComponent | `cargarProducto(id): void` |
| 280 | producto-form.component.ts | ProductoFormComponent | `filtrarCategorias(): void` |
| 281 | producto-form.component.ts | ProductoFormComponent | `mostrarCategoria(id): string` |
| 282 | producto-form.component.ts | ProductoFormComponent | `seleccionarCategoria(id): void` |
| 283 | producto-form.component.ts | ProductoFormComponent | `get categoriaNoExiste(): boolean` (getter) |
| 284 | producto-form.component.ts | ProductoFormComponent | `agregarCategoriaDesdeInput(): void` |
| 285 | producto-form.component.ts | ProductoFormComponent | `abrirCategoriaDialog(): void` |
| 286 | producto-form.component.ts | ProductoFormComponent | `guardar(): void` |
| 287 | producto-form.component.ts | ProductoFormComponent | `cancelar(): void` |
| 288 | producto-preview-dialog.component.ts | ProductoPreviewDialogComponent | `constructor(@Inject data)` |
| 289 | producto-preview-dialog.component.ts | ProductoPreviewDialogComponent | `ngOnInit(): void` |
| 290 | producto-preview-dialog.component.ts | ProductoPreviewDialogComponent | `editar(): void` |
| 291 | producto-preview-dialog.component.ts | ProductoPreviewDialogComponent | `guardar(): void` |
| 292 | producto-preview-dialog.component.ts | ProductoPreviewDialogComponent | `cancelarEdicion(): void` |
| 293 | producto-preview-dialog.component.ts | ProductoPreviewDialogComponent | `borrar(): void`, `cerrar(): void` |
| 294 | producto-excel-dialog.component.ts | ProductoExcelDialogComponent | `onFileSelected(event): void` |
| 295 | producto-excel-dialog.component.ts | ProductoExcelDialogComponent | `cargar(): void` |
| 296 | producto-excel-dialog.component.ts | ProductoExcelDialogComponent | `descargarPlantilla(): void`, `cerrar(): void` |

## 2.6 Clientes

| # | Archivo | Clase | Método |
|---|---------|-------|--------|
| 297 | cliente-lista.component.ts | ClienteListaComponent | `ngAfterViewInit(): void` |
| 298 | cliente-lista.component.ts | ClienteListaComponent | `cargarClientes(): void` |
| 299 | cliente-lista.component.ts | ClienteListaComponent | `buscar(): void`, `limpiarBusqueda(): void` |
| 300 | cliente-lista.component.ts | ClienteListaComponent | `verCliente(id): void`, `confirmarEliminar(id, nombre): void` |
| 301 | cliente-form.component.ts | ClienteFormComponent | `ngOnInit(): void` |
| 302 | cliente-form.component.ts | ClienteFormComponent | `cargarCliente(id): void` |
| 303 | cliente-form.component.ts | ClienteFormComponent | `guardar(): void`, `cancelar(): void` |

## 2.7 Contratos

| # | Archivo | Clase | Método |
|---|---------|-------|--------|
| 304 | contrato-lista.component.ts | ContratoListaComponent | `ngAfterViewInit(): void` |
| 305 | contrato-lista.component.ts | ContratoListaComponent | `cargarContratos(): void` |
| 306 | contrato-lista.component.ts | ContratoListaComponent | `buscar(): void`, `limpiarBusqueda(): void` |
| 307 | contrato-lista.component.ts | ContratoListaComponent | `verContrato(id): void`, `confirmarEliminar(id, nombre): void` |
| 308 | contrato-form.component.ts | ContratoFormComponent | `ngOnInit(): void` |
| 309 | contrato-form.component.ts | ContratoFormComponent | `cargarContrato(id): void` |
| 310 | contrato-form.component.ts | ContratoFormComponent | `guardar(): void`, `cancelar(): void` |

## 2.8 Notas de Venta

| # | Archivo | Clase | Método |
|---|---------|-------|--------|
| 311 | notaventa-lista.component.ts | NotaVentaListaComponent | `ngAfterViewInit(): void` |
| 312 | notaventa-lista.component.ts | NotaVentaListaComponent | `cargarNotas(): void` |
| 313 | notaventa-lista.component.ts | NotaVentaListaComponent | `onPageChange(): void`, `filtrar(): void` |
| 314 | notaventa-lista.component.ts | NotaVentaListaComponent | `abrirCrear(): void`, `abrirEditar(notaId): void` |
| 315 | notaventa-lista.component.ts | NotaVentaListaComponent | `abrirPreview(row): void`, `confirmarEliminar(id): void` |
| 316 | notaventa-lista.component.ts | NotaVentaListaComponent | `firmarNota(id, event): void`, `editarDetalle(row, event): void` |
| 317 | notaventa-lista.component.ts | NotaVentaListaComponent | `getRowClass(row): string` |
| 318 | notaventa-form.component.ts | NotaVentaFormComponent | `get detalles(): FormArray` |
| 319 | notaventa-form.component.ts | NotaVentaFormComponent | `ngOnInit(): void`, `crearDetalle(productoId, cantidad): FormGroup` |
| 320 | notaventa-form.component.ts | NotaVentaFormComponent | `agregarDetalle(): void`, `eliminarDetalle(index): void` |
| 321 | notaventa-form.component.ts | NotaVentaFormComponent | `calcularTotal(): number` |
| 322 | notaventa-form.component.ts | NotaVentaFormComponent | `cargarNota(id): void`, `guardar(): void`, `cancelar(): void` |
| 323 | notaventa-detalle.component.ts | NotaVentaDetalleComponent | `ngOnInit(): void`, `volver(): void` |
| 324 | notaventa-detalle.component.ts | NotaVentaDetalleComponent | `editar(): void`, `borrar(): void`, `imprimir(): void` |
| 325 | notaventa-detalle.component.ts | NotaVentaDetalleComponent | `firmarNota(): void`, `editarDetalle(): void` |
| 326 | notaventa-preview-dialog.component.ts | NotaVentaPreviewDialogComponent | `get isExtra(): boolean`, `get label(): string`, `get clienteOUsuario(): string`, `get conPrecios(): boolean` (getters) |
| 327 | notaventa-preview-dialog.component.ts | NotaVentaPreviewDialogComponent | `getProductoName(d): string` |
| 328 | notaventa-preview-dialog.component.ts | NotaVentaPreviewDialogComponent | `imprimir(): void`, `editar(): void`, `borrar(): void`, `cerrar(): void`, `firmar(): void`, `editarDetalle(): void` |
| 329 | notaventa-form-dialog.component.ts | NotaVentaFormDialogComponent | `get detalles(): FormArray`, `get clienteNombre(): string`, `get folioDisplay(): string`, `get productos()` (getters) |
| 330 | notaventa-form-dialog.component.ts | NotaVentaFormDialogComponent | `ngOnInit(): void`, `cargarNota(id): void` |
| 331 | notaventa-form-dialog.component.ts | NotaVentaFormDialogComponent | `crearDetalle(productoId, cantidad): FormGroup` |
| 332 | notaventa-form-dialog.component.ts | NotaVentaFormDialogComponent | `agregarDetalle(): void`, `eliminarDetalle(index): void` |
| 333 | notaventa-form-dialog.component.ts | NotaVentaFormDialogComponent | `calcularSubTotal(cant, precio): number` |
| 334 | notaventa-form-dialog.component.ts | NotaVentaFormDialogComponent | `getPrecio(productoId): number`, `getProductoNombre(productoId): string`, `getDetalleSubTotal(index): number`, `calcularTotal(): number` |
| 335 | notaventa-form-dialog.component.ts | NotaVentaFormDialogComponent | `onPartidaChange(): void`, `guardar(): void`, `cancelar(): void` |
| 336 | detalle-dialog.component.ts | DetalleDialogComponent | `constructor(@Inject data)` |

## 2.9 Órdenes de Compra

| # | Archivo | Clase | Método |
|---|---------|-------|--------|
| 337 | orden-lista.component.ts | OrdenListaComponent | `ngAfterViewInit(): void` |
| 338 | orden-lista.component.ts | OrdenListaComponent | `cargarOrdenes(): void`, `onPageChange(): void` |
| 339 | orden-lista.component.ts | OrdenListaComponent | `onFechaChange(): void`, `filtrar(): void`, `limpiarBusqueda(): void` |
| 340 | orden-lista.component.ts | OrdenListaComponent | `verOrden(id): void`, `confirmarEliminar(id): void` |
| 341 | orden-form.component.ts | OrdenFormComponent | `get detalles(): FormArray` |
| 342 | orden-form.component.ts | OrdenFormComponent | `ngOnInit(): void`, `onClienteChange(): void`, `onPartidaChange(): void` |
| 343 | orden-form.component.ts | OrdenFormComponent | `generarDetallesDesdeProductos(): void`, `crearDetalle(productoId, productoNombre): FormGroup` |
| 344 | orden-form.component.ts | OrdenFormComponent | `onCellChange(): void`, `recalcularTotales(): void` |
| 345 | orden-form.component.ts | OrdenFormComponent | `cargarOrden(id): void`, `guardar(): void` |
| 346 | orden-form.component.ts | OrdenFormComponent | `getConfirmaTotalFila(index): number`, `getConfirmaTotalCol(dia): number`, `getConfirmaGranTotal(): number` |
| 347 | orden-form.component.ts | OrdenFormComponent | `nuevaOrden(): void`, `activarModoGeneracion(): void` |
| 348 | orden-form.component.ts | OrdenFormComponent | `generarNota(dia): void`, `generarTodasLasNotas(): void` |
| 349 | orden-form.component.ts | OrdenFormComponent | `tieneProductosDia(dia): boolean`, `getNotaIdCorto(id): string` |
| 350 | orden-form.component.ts | OrdenFormComponent | `cancelar(): void`, `volverLista(): void` |
| 351 | orden-detalle.component.ts | OrdenDetalleComponent | `ngOnInit(): void` |
| 352 | orden-detalle.component.ts | OrdenDetalleComponent | `cargarOrden(id): void`, `cargarNotas(id): void`, `cargarCancelaciones(id): void`, `cargarExtras(id): void`, `cargarFactura(id): void`, `cargarFacturaExtras(id): void` (private) |
| 353 | orden-detalle.component.ts | OrdenDetalleComponent | `volver(): void`, `editar(): void`, `eliminar(): void`, `confirmar(): void` |
| 354 | orden-detalle.component.ts | OrdenDetalleComponent | `generarNotas(): void`, `abrirNota(nota): void` |
| 355 | orden-detalle.component.ts | OrdenDetalleComponent | `abrirCrearCancelacion(): void`, `validarCancelacion(id): void`, `eliminarCancelacion(id): void` |
| 356 | orden-detalle.component.ts | OrdenDetalleComponent | `abrirCrearExtra(): void`, `abrirExtra(extra): void` |
| 357 | orden-detalle.component.ts | OrdenDetalleComponent | `reconstruirNotas(): void` |
| 358 | orden-detalle.component.ts | OrdenDetalleComponent | `get puedeGenerarFactura(): boolean`, `get puedeGenerarFacturaExtras(): boolean` |
| 359 | orden-detalle.component.ts | OrdenDetalleComponent | `generarFactura(): void`, `generarFacturaExtras(): void` |
| 360 | orden-detalle.component.ts | OrdenDetalleComponent | `notaTieneCancelacion(nota): boolean`, `getNotaClase(nota): string` |
| 361 | orden-detalle.component.ts | OrdenDetalleComponent | `getValor(detalle, dia): string`, `getFinSemana(): Date` |
| 362 | orden-detalle.component.ts | OrdenDetalleComponent | `sumarDia(detalles, dia): number`, `totalRow(detalle): number`, `totalGeneral(): number` |
| 363 | cancelacion-form-dialog.component.ts | CancelacionFormDialogComponent | `constructor(@Inject data)` |
| 364 | cancelacion-form-dialog.component.ts | CancelacionFormDialogComponent | `onDiaChange(): void`, `getCantidadOC(detalle): number` |
| 365 | cancelacion-form-dialog.component.ts | CancelacionFormDialogComponent | `validarCantidad(i): void`, `tieneCancelaciones(): boolean` |
| 366 | cancelacion-form-dialog.component.ts | CancelacionFormDialogComponent | `guardar(): void`, `cerrar(): void` |
| 367 | extra-form-dialog.component.ts | ExtraFormDialogComponent | `constructor(@Inject data)` |
| 368 | extra-form-dialog.component.ts | ExtraFormDialogComponent | `ngOnInit(): void`, `tieneExtras(): boolean` |
| 369 | extra-form-dialog.component.ts | ExtraFormDialogComponent | `guardar(): void`, `cerrar(): void` |

## 2.10 Reportes

| # | Archivo | Clase | Método |
|---|---------|-------|--------|
| 370 | reporte-produccion.component.ts | ReporteProduccionComponent | `getMonday(date): Date`, `formatDate(d): string` |
| 371 | reporte-produccion.component.ts | ReporteProduccionComponent | `consultar(): void`, `diaLabel(dia): string`, `imprimir(): void` |

## 2.11 Servicios Frontend

| # | Archivo | Clase | Método |
|---|---------|-------|--------|
| 372 | auth.service.ts | AuthService | `login(datos): Observable`, `register(datos): Observable` |
| 373 | auth.service.ts | AuthService | `logout(): void`, `getToken(): string`, `isAuthenticated(): boolean` |
| 374 | auth.service.ts | AuthService | `get currentUser(): DatosRespuestaAuth`, `hasRole(role): boolean`, `isAdmin(): boolean` |
| 375 | admin.service.ts | AdminService | `eliminarNotaPorId(id): Observable` |
| 376 | admin.service.ts | AdminService | `eliminarNotaPorFolio(folio): Observable` |
| 377 | admin.service.ts | AdminService | `eliminarOrdenPorId(id): Observable` |
| 378 | admin.service.ts | AdminService | `eliminarCancelacionPorId(id): Observable` |
| 379 | usuario-admin.service.ts | UsuarioAdminService | `listar(): Observable`, `buscar(id): Observable` |
| 380 | usuario-admin.service.ts | UsuarioAdminService | `crear(datos): Observable`, `actualizar(id, datos): Observable` |
| 381 | usuario-admin.service.ts | UsuarioAdminService | `asignarPermisos(usuarioId, permisos): Observable`, `toggle(usuarioId): Observable` |
| 382 | categoria.service.ts | CategoriaService | `registrar(datos): Observable`, `listar(): Observable` |
| 383 | categoria.service.ts | CategoriaService | `getFromCache(id): DatosDetalleCategoria`, `buscarPorId(id): Observable` |
| 384 | categoria.service.ts | CategoriaService | `actualizar(id, datos): Observable`, `eliminar(id): Observable` |
| 385 | producto.service.ts | ProductoService | `registrar(datos): Observable`, `listar(page?, size?): Observable` |
| 386 | producto.service.ts | ProductoService | `listarPorPartida(partida, page?, size?): Observable` |
| 387 | producto.service.ts | ProductoService | `listarPorCategoria(id, page?, size?): Observable` |
| 388 | producto.service.ts | ProductoService | `buscarPorId(id): Observable`, `buscarPorNombre(nombre): Observable` |
| 389 | producto.service.ts | ProductoService | `buscarPorPalabra(q, page?, size?): Observable` |
| 390 | producto.service.ts | ProductoService | `actualizar(id, datos): Observable`, `eliminar(id): Observable` |
| 391 | producto-excel.service.ts | ProductoExcelService | `cargarProductos(archivo): Observable`, `descargarPlantilla(): Observable` |
| 392 | cliente.service.ts | ClienteService | `registrar(datos): Observable`, `listar(): Observable` |
| 393 | cliente.service.ts | ClienteService | `getFromCache(id): DatosDetalleCliente`, `buscarPorId(id): Observable` |
| 394 | cliente.service.ts | ClienteService | `actualizar(id, datos): Observable`, `eliminar(id): Observable` |
| 395 | contrato.service.ts | ContratoService | `registrar(datos): Observable`, `listar(): Observable` |
| 396 | contrato.service.ts | ContratoService | `getFromCache(id): DatosDetalleContrato`, `buscarPorId(id): Observable` |
| 397 | contrato.service.ts | ContratoService | `actualizar(id, datos): Observable`, `eliminar(id): Observable` |
| 398 | notaventa.service.ts | NotaVentaService | `registrar(datos): Observable`, `generarDesdeOrden(ordenCompraId, dia): Observable` |
| 399 | notaventa.service.ts | NotaVentaService | `listar(page?, size?): Observable`, `buscarPorId(id): Observable` |
| 400 | notaventa.service.ts | NotaVentaService | `actualizar(id, datos): Observable`, `eliminar(id): Observable` |
| 401 | notaventa.service.ts | NotaVentaService | `firmar(id): Observable`, `actualizarDetalle(id, detalle): Observable` |
| 402 | ordencompra.service.ts | OrdenCompraService | `registrar(datos): Observable`, `listar(page?, size?, fecha?): Observable` |
| 403 | ordencompra.service.ts | OrdenCompraService | `buscarPorId(id): Observable`, `actualizar(id, datos): Observable` |
| 404 | ordencompra.service.ts | OrdenCompraService | `eliminar(id): Observable`, `confirmar(id): Observable` |
| 405 | ordencompra.service.ts | OrdenCompraService | `listarNotasPorOrden(id): Observable`, `generarTodasNotas(id): Observable` |
| 406 | extra.service.ts | ExtraService | `crear(datos): Observable`, `listarPorOrden(ordenCompraId): Observable` |
| 407 | extra.service.ts | ExtraService | `firmar(id): Observable`, `eliminar(id): Observable` |
| 408 | factura.service.ts | FacturaService | `generar(datos): Observable`, `generarExtras(datos): Observable` |
| 409 | factura.service.ts | FacturaService | `listar(): Observable`, `obtenerPorId(id): Observable` |
| 410 | factura.service.ts | FacturaService | `obtenerPorOrdenCompraId(id): Observable`, `obtenerFacturaExtrasPorOrdenCompraId(id): Observable` |
| 411 | cancelacion.service.ts | CancelacionService | `crear(datos): Observable`, `listarPorOrden(id): Observable` |
| 412 | cancelacion.service.ts | CancelacionService | `validar(id): Observable`, `eliminar(id): Observable` |
| 413 | cancelacion.service.ts | CancelacionService | `reconstruirNotas(ordenCompraId): Observable` |
| 414 | enums.service.ts | EnumsService | `getPartidas(): Observable`, `getUnidadesMedida(): Observable` |
| 415 | enums.service.ts | EnumsService | `getModulos(): Observable`, `getAcciones(): Observable` |
| 416 | reporte-produccion.service.ts | ReporteProduccionService | `obtenerReporte(semana): Observable` |

---

# 3. Totales

| Ámbito | Funciones |
|--------|-----------|
| Backend — Controllers | 74 |
| Backend — Services | 73 |
| Backend — Helpers/Validaciones | 18 |
| Backend — Infrastructure/Security | 25 |
| Backend — Domain entities | 13 |
| Backend — Validators/Enums | 8 |
| Backend — Repository custom methods | 35+ |
| **Subtotal Backend** | **~246** |
| Frontend — Components | ~145 |
| Frontend — Services | 45 |
| **Subtotal Frontend** | **~190** |
| **Total Sistema** | **~436 funciones** |
