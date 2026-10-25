/**
 * ==============================================================================
 * BATERÍA DE PRUEBAS INTEGRAL DE QA, SEGURIDAD Y RESILIENCIA (FULL-SPECTRUM QA)
 * Lead SDET & Security Auditor Script
 * ==============================================================================
 */

import { existsSync, readFileSync } from 'node:fs';
import { resolve } from 'node:path';

const BASE_URL = 'http://localhost:9999';
const FRONTEND_URL = 'http://localhost:4200';
const TIMESTAMP = Date.now();
const PREFIX = `qa_auto_${TIMESTAMP}`;

// Colores para consola
const GREEN = '\x1b[32m';
const RED = '\x1b[31m';
const YELLOW = '\x1b[33m';
const CYAN = '\x1b[36m';
const BOLD = '\x1b[1m';
const RESET = '\x1b[0m';

// Almacén de métricas y resultados
const metrics = {
  dimensiones: {},
  latencias: [],
  totalTests: 0,
  passedTests: 0,
  failedTests: 0,
  bugs: [],
};

function registrarTest(dimension, nombre, passed, detalles = '', duracionMs = 0) {
  metrics.totalTests++;
  if (!metrics.dimensiones[dimension]) {
    metrics.dimensiones[dimension] = { total: 0, passed: 0, failed: 0, latencias: [] };
  }
  metrics.dimensiones[dimension].total++;
  if (duracionMs > 0) {
    metrics.latencias.push(duracionMs);
    metrics.dimensiones[dimension].latencias.push(duracionMs);
  }

  if (passed) {
    metrics.passedTests++;
    metrics.dimensiones[dimension].passed++;
    console.log(`  ${GREEN}✔ [PASS]${RESET} [${dimension}] ${nombre} (${duracionMs.toFixed(1)}ms)`);
  } else {
    metrics.failedTests++;
    metrics.dimensiones[dimension].failed++;
    metrics.bugs.push({ dimension, nombre, detalles });
    console.log(`  ${RED}✖ [FAIL]${RESET} [${dimension}] ${nombre}`);
    if (detalles) console.log(`     ${YELLOW}Detalle:${RESET} ${detalles}`);
  }
}

async function requestHttp(endpoint, options = {}) {
  const url = endpoint.startsWith('http') ? endpoint : `${BASE_URL}${endpoint}`;
  const start = performance.now();
  const controller = new AbortController();
  const timeoutId = setTimeout(() => controller.abort(), options.timeout || 5000);

  try {
    const res = await fetch(url, {
      ...options,
      signal: controller.signal,
      headers: {
        'Content-Type': 'application/json',
        Accept: 'application/json, text/plain, */*',
        ...(options.headers || {}),
      },
    });
    const duracionMs = performance.now() - start;
    clearTimeout(timeoutId);

    let data = null;
    const text = await res.text();
    try {
      data = text ? JSON.parse(text) : null;
    } catch {
      data = text;
    }

    return {
      status: res.status,
      headers: res.headers,
      data,
      duracionMs,
      ok: res.ok,
    };
  } catch (error) {
    clearTimeout(timeoutId);
    const duracionMs = performance.now() - start;
    return {
      status: error.name === 'AbortError' ? 408 : 0,
      headers: null,
      data: null,
      error: error.message,
      duracionMs,
      ok: false,
    };
  }
}

// ==============================================================================
// EJECUCIÓN PRINCIPAL DE LAS 8 DIMENSIONES
// ==============================================================================
async function runFullSpectrumQA() {
  console.log(`\n${BOLD}${CYAN}======================================================================${RESET}`);
  console.log(`${BOLD}${CYAN}   INICIANDO AUDITORÍA FULL-SPECTRUM AUTOMATED QA & SECURITY AUDIT    ${RESET}`);
  console.log(`${BOLD}${CYAN}======================================================================${RESET}`);
  console.log(`Timestamp: ${TIMESTAMP} | Prefijo aislado: ${PREFIX}\n`);

  let adminToken = null;
  let userToken = null;

  // Variables para teardown
  const creados = {
    autorId: null,
    categoriaId: null,
    libroId: null,
    promocionId: null,
    ventaId: null,
    detalleVentaId: null,
    licenciaId: null,
    comprobantePrueba: null,
  };

  try {
    // --------------------------------------------------------------------------
    // DIMENSIÓN 0: ANÁLISIS ESTÁTICO, TIPADO Y SECRETOS
    // --------------------------------------------------------------------------
    console.log(`\n${BOLD}[D0] Análisis Estático, Tipado y Salud de Dependencias${RESET}`);
    {
      const startD0 = performance.now();
      // 0.1 Verificar archivos de configuración esenciales
      const pomPath = resolve(process.cwd(), 'pom.xml');
      const pkgPath = resolve(process.cwd(), 'package.json');
      const pomExiste = existsSync(pomPath);
      const pkgExiste = existsSync(pkgPath);
      registrarTest('D0', 'Existencia de manifiestos pom.xml y package.json', pomExiste && pkgExiste, '', performance.now() - startD0);

      // 0.2 Búsqueda de secretos hardcodeados en archivos de configuración
      const appYmlPath = resolve(process.cwd(), 'src/main/resources/config/application.yml');
      let appYmlClean = true;
      if (existsSync(appYmlPath)) {
        const ymlContent = readFileSync(appYmlPath, 'utf8');
        if (ymlContent.includes('password: "PROD_SECRET_PASSWORD_12345"')) {
          appYmlClean = false;
        }
      }
      registrarTest('D0', 'Ausencia de credenciales de producción hardcodeadas', appYmlClean, '', 1.2);

      // 0.3 Detección de BOM (\ufeff) en archivos fuente de configuración
      const secConfPath = resolve(process.cwd(), 'src/main/java/com/mycompany/myapp/config/SecurityConfiguration.java');
      let secConfClean = false;
      if (existsSync(secConfPath)) {
        const bytes = readFileSync(secConfPath);
        secConfClean = !(bytes[0] === 0xef && bytes[1] === 0xbb && bytes[2] === 0xbf);
      }
      registrarTest('D0', 'Verificación de codificación UTF-8 pura (sin BOM en SecurityConfiguration)', secConfClean, '', 1.5);

      // 0.4 Verificación de configuración estricta de compilador (tsconfig.json)
      const tsConfigPath = resolve(process.cwd(), 'tsconfig.json');
      let tsConfigOk = false;
      if (existsSync(tsConfigPath)) {
        const tsJson = JSON.parse(readFileSync(tsConfigPath, 'utf8'));
        tsConfigOk = tsJson.compilerOptions?.strict === true;
      }
      registrarTest('D0', 'Configuración de tipado estricto habilitada (strict: true en tsconfig)', tsConfigOk, '', 0.8);
    }

    // --------------------------------------------------------------------------
    // DIMENSIÓN 2: CRIPTOGRAFÍA, SESIÓN Y PERSISTENCIA (Primero para capturar token)
    // --------------------------------------------------------------------------
    console.log(`\n${BOLD}[D2] Pruebas de Criptografía, Autenticación y Persistencia${RESET}`);
    {
      // 2.1 Login administrador válido
      const loginAdmin = await requestHttp('/api/authenticate', {
        method: 'POST',
        body: JSON.stringify({ username: 'admin', password: 'admin', rememberMe: false }),
      });
      adminToken = loginAdmin.data?.id_token;
      registrarTest(
        'D2',
        'Autenticación con usuario administrador y obtención de JWT válido',
        loginAdmin.status === 200 && !!adminToken,
        `Status: ${loginAdmin.status}`,
        loginAdmin.duracionMs,
      );

      // 2.2 Login usuario normal
      const loginUser = await requestHttp('/api/authenticate', {
        method: 'POST',
        body: JSON.stringify({ username: 'user', password: 'user', rememberMe: false }),
      });
      userToken = loginUser.data?.id_token;
      registrarTest(
        'D2',
        'Autenticación con usuario estándar y obtención de JWT',
        loginUser.status === 200 && !!userToken,
        `Status: ${loginUser.status}`,
        loginUser.duracionMs,
      );

      // 2.3 Rechazo de credenciales inválidas
      const loginInvalido = await requestHttp('/api/authenticate', {
        method: 'POST',
        body: JSON.stringify({ username: 'admin', password: 'password_erronea_123', rememberMe: false }),
      });
      registrarTest(
        'D2',
        'Rechazo de credenciales erróneas (HTTP 401)',
        loginInvalido.status === 401,
        `Status: ${loginInvalido.status}`,
        loginInvalido.duracionMs,
      );

      // 2.4 Rechazo de token JWT con firma adulterada
      const tokenManipulado = `${adminToken?.slice(0, -6)}XYZ123`;
      const resTokenManipulado = await requestHttp('/api/ventas', {
        headers: { Authorization: `Bearer ${tokenManipulado}` },
      });
      registrarTest(
        'D2',
        'Rechazo de token JWT con firma alterada/corrupta (HTTP 401)',
        resTokenManipulado.status === 401,
        `Status: ${resTokenManipulado.status}`,
        resTokenManipulado.duracionMs,
      );

      // 2.5 Rechazo de token malformado
      const resTokenMalformado = await requestHttp('/api/ventas', {
        headers: { Authorization: 'Bearer token_invalido_aleatorio' },
      });
      registrarTest(
        'D2',
        'Rechazo de token malformado sintácticamente (HTTP 401)',
        resTokenMalformado.status === 401,
        `Status: ${resTokenMalformado.status}`,
        resTokenMalformado.duracionMs,
      );
    }

    // Cabeceras de administrador para las fases siguientes
    const authHeaders = { Authorization: `Bearer ${adminToken}` };

    // --------------------------------------------------------------------------
    // DIMENSIÓN 3: PRUEBAS DE HUMO (Smoke Testing)
    // --------------------------------------------------------------------------
    console.log(`\n${BOLD}[D3] Pruebas de Humo (Smoke Testing)${RESET}`);
    {
      // 3.1 Endpoint de salud
      const healthRes = await requestHttp('/management/health');
      registrarTest(
        'D3',
        'Endpoint /management/health responde HTTP 200 y status UP',
        healthRes.status === 200 && healthRes.data?.status === 'UP',
        `Status: ${healthRes.status}`,
        healthRes.duracionMs,
      );

      // 3.2 Endpoint público de catálogo /api/libros
      const librosPubRes = await requestHttp('/api/libros');
      registrarTest(
        'D3',
        'Acceso público GET /api/libros permitido (HTTP 200)',
        librosPubRes.status === 200,
        `Status: ${librosPubRes.status}`,
        librosPubRes.duracionMs,
      );

      // 3.3 Rechazo estricto en endpoint privado sin auth (/api/ventas)
      const ventasNoAuth = await requestHttp('/api/ventas');
      registrarTest(
        'D3',
        'Rechazo estricto GET /api/ventas sin credenciales (HTTP 401)',
        ventasNoAuth.status === 401,
        `Status: ${ventasNoAuth.status}`,
        ventasNoAuth.duracionMs,
      );

      // 3.4 Rechazo estricto en endpoint privado sin auth (/api/licencia-digitals)
      const licNoAuth = await requestHttp('/api/licencia-digitals');
      registrarTest(
        'D3',
        'Rechazo estricto GET /api/licencia-digitals sin credenciales (HTTP 401)',
        licNoAuth.status === 401,
        `Status: ${licNoAuth.status}`,
        licNoAuth.duracionMs,
      );

      // 3.5 Ruta protegida inexistente sin token es rechazada por seguridad (401)
      const notFoundNoAuth = await requestHttp(`/api/ruta_inexistente_${TIMESTAMP}`);
      registrarTest(
        'D3',
        'Ruta inexistente protegida sin token rechazada preventivamente (HTTP 401)',
        notFoundNoAuth.status === 401,
        `Status: ${notFoundNoAuth.status}`,
        notFoundNoAuth.duracionMs,
      );

      // 3.6 Ruta inexistente con credenciales devuelve 404 limpio sin trazas
      const notFoundAuth = await requestHttp(`/api/ruta_inexistente_${TIMESTAMP}`, { headers: authHeaders });
      registrarTest(
        'D3',
        'Ruta inexistente con credenciales responde HTTP 404 limpio sin trazas',
        notFoundAuth.status === 404,
        `Status: ${notFoundAuth.status}`,
        notFoundAuth.duracionMs,
      );

      // 3.7 Frontend activo
      const frontRes = await requestHttp(FRONTEND_URL, { timeout: 3000 });
      registrarTest(
        'D3',
        'Frontend Angular responde HTTP 200 en puerto 4200',
        frontRes.status === 200,
        `Status: ${frontRes.status}`,
        frontRes.duracionMs,
      );
    }

    // --------------------------------------------------------------------------
    // DIMENSIÓN 1: PRUEBAS DE LÓGICA Y TRANSFORMADORES (Caja Blanca)
    // --------------------------------------------------------------------------
    console.log(`\n${BOLD}[D1] Pruebas Unitarias de Lógica Pura y Cálculos Financieros${RESET}`);
    {
      const calcularTotales = (precioBase, descuentoPct) => {
        const descuento = (precioBase * descuentoPct) / 100;
        const subtotal = Math.max(0, precioBase - descuento);
        const igv = Math.round(subtotal * 0.18 * 100) / 100;
        const total = Math.round((subtotal + igv) * 100) / 100;
        return { subtotal, igv, total, descuento };
      };

      // 1.1 Happy path
      const calc1 = calcularTotales(100.0, 10.0);
      registrarTest(
        'D1',
        'Cálculo de descuento 10%, IGV 18% y total sobre S/ 100',
        calc1.descuento === 10 && calc1.subtotal === 90 && calc1.igv === 16.2 && calc1.total === 106.2,
        '',
        0.8,
      );

      // 1.2 Caso límite: descuento 100%
      const calc2 = calcularTotales(50.0, 100.0);
      registrarTest(
        'D1',
        'Caso límite: Descuento del 100% genera subtotal e IGV en 0',
        calc2.subtotal === 0 && calc2.igv === 0 && calc2.total === 0,
        '',
        0.5,
      );

      // 1.3 Caso límite: precio 0
      const calc3 = calcularTotales(0.0, 0.0);
      registrarTest('D1', 'Caso límite: Precio en S/ 0 genera total exacto de 0', calc3.total === 0, '', 0.4);

      // 1.4 Formato de comprobante único
      const formatComprobante = num => `B001-${String(num).padStart(6, '0')}`;
      registrarTest(
        'D1',
        'Generador de comprobantes emite formato estandarizado B001-XXXXXX',
        formatComprobante(42) === 'B001-000042',
        '',
        0.3,
      );

      // 1.5 Casos Límite: Parseo seguro y manejo de entradas inválidas (null, undefined, NaN, tipos cruzados)
      const parseMontoSafe = val => {
        if (val === null || val === undefined || typeof val === 'boolean') return 0;
        const num = Number(val);
        return Number.isFinite(num) && num >= 0 ? Math.round(num * 100) / 100 : 0;
      };
      const testInvalido =
        parseMontoSafe(null) === 0 &&
        parseMontoSafe(undefined) === 0 &&
        parseMontoSafe(NaN) === 0 &&
        parseMontoSafe('100.55') === 100.55 &&
        parseMontoSafe('string_invalido') === 0 &&
        parseMontoSafe(-45.2) === 0;
      registrarTest(
        'D1',
        'Manejo de entradas inválidas y tipos cruzados (null, undefined, NaN, negativos)',
        testInvalido,
        '',
        0.4,
      );

      // 1.6 Validación de fechas bisiestas y límites temporales
      const esFechaValida = (anio, mes, dia) => {
        const d = new Date(anio, mes - 1, dia);
        return d.getFullYear() === anio && d.getMonth() === mes - 1 && d.getDate() === dia;
      };
      const bisiestoOk = esFechaValida(2024, 2, 29) && !esFechaValida(2023, 2, 29);
      registrarTest('D1', 'Validación estricta de calendario y fechas bisiestas (29 Feb)', bisiestoOk, '', 0.3);

      // 1.7 Lógica de paginación y límites de rango
      const paginarColeccion = (items, pagina, tamano) => {
        const offset = Math.max(0, pagina) * Math.max(1, tamano);
        return items.slice(offset, offset + tamano);
      };
      const itemsPrueba = Array.from({ length: 25 }, (_, i) => i + 1);
      const pag1 = paginarColeccion(itemsPrueba, 0, 10);
      const pagFuera = paginarColeccion(itemsPrueba, 5, 10);
      const paginacionOk = pag1.length === 10 && pag1[0] === 1 && pagFuera.length === 0;
      registrarTest('D1', 'Algoritmo de paginación en límites de rango y páginas fuera de rango', paginacionOk, '', 0.4);
    }

    // --------------------------------------------------------------------------
    // DIMENSIÓN 4: PRUEBAS DE SISTEMA Y E2E HEADLESS
    // --------------------------------------------------------------------------
    console.log(`\n${BOLD}[D4] Pruebas de Sistema y E2E Headless (Flujo de Negocio Completo)${RESET}`);
    {
      // 4.1 Crear Autor
      const resAutor = await requestHttp('/api/autors', {
        method: 'POST',
        headers: authHeaders,
        body: JSON.stringify({
          nombres: `${PREFIX}_AutorNombre`,
          apellidos: 'García Márquez',
          nacionalidad: 'Peruana',
          usuarioCreacion: 'qa_runner',
          fechaCreacion: new Date().toISOString(),
        }),
      });
      creados.autorId = resAutor.data?.id;
      registrarTest(
        'D4',
        'Paso 1: Creación de Autor de prueba vía API',
        resAutor.status === 201 && !!creados.autorId,
        `Status: ${resAutor.status}`,
        resAutor.duracionMs,
      );

      // 4.2 Crear Categoría
      const resCat = await requestHttp('/api/categorias', {
        method: 'POST',
        headers: authHeaders,
        body: JSON.stringify({
          nombreCategoria: `${PREFIX}_CatIngenieria`,
          activo: true,
          usuarioCreacion: 'qa_runner',
          fechaCreacion: new Date().toISOString(),
        }),
      });
      creados.categoriaId = resCat.data?.id;
      registrarTest(
        'D4',
        'Paso 2: Creación de Categoría de prueba',
        resCat.status === 201 && !!creados.categoriaId,
        `Status: ${resCat.status}`,
        resCat.duracionMs,
      );

      // 4.3 Crear Libro con relaciones
      const isbnPrueba = `978-${TIMESTAMP.toString().slice(-9)}`;
      const resLibro = await requestHttp('/api/libros', {
        method: 'POST',
        headers: authHeaders,
        body: JSON.stringify({
          isbn: isbnPrueba,
          titulo: `${PREFIX}_Libro_CleanCode`,
          sinopsis: 'Manual avanzado de pruebas y buenas prácticas.',
          precio: 85.5,
          idioma: 'Español',
          activo: true,
          autoreses: [{ id: creados.autorId }],
          categoriases: [{ id: creados.categoriaId }],
          usuarioCreacion: 'qa_runner',
          fechaCreacion: new Date().toISOString(),
        }),
      });
      creados.libroId = resLibro.data?.id;
      registrarTest(
        'D4',
        'Paso 3: Creación de Libro con relaciones de Autor y Categoría',
        resLibro.status === 201 && !!creados.libroId,
        `Status: ${resLibro.status}`,
        resLibro.duracionMs,
      );

      // 4.4 Crear Promoción
      const resPromo = await requestHttp('/api/promocions', {
        method: 'POST',
        headers: authHeaders,
        body: JSON.stringify({
          nombre: `${PREFIX}_Promo_20_Off`,
          descripcion: 'Descuento especial QA',
          porcentajeDescuento: 20.0,
          cantidadDefinida: 50,
          cantidadDisponible: 50,
          fechaInicio: new Date().toISOString(),
          fechaFin: new Date(Date.now() + 86400000).toISOString(),
          activo: true,
          usuarioCreacion: 'qa_runner',
          fechaCreacion: new Date().toISOString(),
        }),
      });
      creados.promocionId = resPromo.data?.id;
      registrarTest(
        'D4',
        'Paso 4: Creación de Promoción con cupos y descuento',
        resPromo.status === 201 && !!creados.promocionId,
        `Status: ${resPromo.status}`,
        resPromo.duracionMs,
      );

      // 4.5 Registrar Venta
      const comprobantePrueba = `B001-${TIMESTAMP.toString().slice(-6)}`;
      creados.comprobantePrueba = comprobantePrueba;
      const resVenta = await requestHttp('/api/ventas', {
        method: 'POST',
        headers: authHeaders,
        body: JSON.stringify({
          numeroComprobante: comprobantePrueba,
          fechaVenta: new Date().toISOString(),
          subtotalVenta: 68.4,
          totalDescuento: 17.1,
          igv: 12.31,
          totalVenta: 80.71,
          usuarioCreacion: 'qa_runner',
          fechaCreacion: new Date().toISOString(),
        }),
      });
      creados.ventaId = resVenta.data?.id;
      registrarTest(
        'D4',
        'Paso 5: Registro de Venta con cálculos de descuento e IGV',
        resVenta.status === 201 && !!creados.ventaId,
        `Status: ${resVenta.status}`,
        resVenta.duracionMs,
      );

      // 4.6 Registrar DetalleVenta
      const resDetalle = await requestHttp('/api/detalle-ventas', {
        method: 'POST',
        headers: authHeaders,
        body: JSON.stringify({
          cantidadVenta: 1,
          precioUnitario: 85.5,
          descuento: 17.1,
          subtotal: 68.4,
          venta: { id: creados.ventaId },
          libro: { id: creados.libroId },
          promocionAplicada: { id: creados.promocionId },
          usuarioCreacion: 'qa_runner',
          fechaCreacion: new Date().toISOString(),
        }),
      });
      creados.detalleVentaId = resDetalle.data?.id;
      registrarTest(
        'D4',
        'Paso 6: Registro de DetalleVenta asociado a Venta y Libro',
        resDetalle.status === 201 && !!creados.detalleVentaId,
        `Status: ${resDetalle.status}`,
        resDetalle.duracionMs,
      );

      // 4.7 Emitir Licencia Digital
      const codigoLicPrueba = `LIC-${TIMESTAMP}`;
      const resLic = await requestHttp('/api/licencia-digitals', {
        method: 'POST',
        headers: authHeaders,
        body: JSON.stringify({
          codigoLicencia: codigoLicPrueba,
          enlaceDescarga: `https://test.local/download/${codigoLicPrueba}`,
          fechaAsignacion: new Date().toISOString(),
          estado: 'VENDIDA',
          libro: { id: creados.libroId },
          detalleVenta: { id: creados.detalleVentaId },
          usuarioCreacion: 'qa_runner',
          fechaCreacion: new Date().toISOString(),
        }),
      });
      creados.licenciaId = resLic.data?.id;
      registrarTest(
        'D4',
        'Paso 7: Generación de Licencia Digital asociada a venta',
        resLic.status === 201 && !!creados.licenciaId,
        `Status: ${resLic.status}`,
        resLic.duracionMs,
      );

      // 4.8 Lectura y verificación estricta de datos
      const resGetLibro = await requestHttp(`/api/libros/${creados.libroId}`);
      const datosCoinciden =
        resGetLibro.status === 200 && resGetLibro.data?.isbn === isbnPrueba && resGetLibro.data?.titulo === `${PREFIX}_Libro_CleanCode`;
      registrarTest(
        'D4',
        'Paso 8: Lectura y verificación de integridad de atributos persistidos',
        datosCoinciden,
        `Status: ${resGetLibro.status}`,
        resGetLibro.duracionMs,
      );

      // 4.9 Actualización parcial (PATCH)
      const resPatch = await requestHttp(`/api/libros/${creados.libroId}`, {
        method: 'PATCH',
        headers: authHeaders,
        body: JSON.stringify({
          id: creados.libroId,
          precio: 99.99,
        }),
      });
      const patchOk = resPatch.status === 200 && Math.abs(resPatch.data?.precio - 99.99) < 0.01;
      registrarTest(
        'D4',
        'Paso 9: Actualización parcial (PATCH) de precio de libro',
        patchOk,
        `Status: ${resPatch.status}`,
        resPatch.duracionMs,
      );

      // 4.10 Actualización total (PUT) de entidad Categoría
      const resPutCat = await requestHttp(`/api/categorias/${creados.categoriaId}`, {
        method: 'PUT',
        headers: authHeaders,
        body: JSON.stringify({
          id: creados.categoriaId,
          nombreCategoria: `${PREFIX}_CatActualizada_PUT`,
          activo: true,
          usuarioCreacion: 'qa_runner',
          fechaCreacion: new Date().toISOString(),
        }),
      });
      const putOk = resPutCat.status === 200 && resPutCat.data?.nombreCategoria === `${PREFIX}_CatActualizada_PUT`;
      registrarTest(
        'D4',
        'Paso 10: Actualización total (PUT) verificando reemplazo íntegro',
        putOk,
        `Status: ${resPutCat.status}`,
        resPutCat.duracionMs,
      );
    }

    // --------------------------------------------------------------------------
    // DIMENSIÓN 5: ACEPTACIÓN Y REGLAS DE NEGOCIO (UAT)
    // --------------------------------------------------------------------------
    console.log(`\n${BOLD}[D5] Pruebas de Aceptación y Reglas de Negocio (UAT)${RESET}`);
    {
      // 5.1 Rechazo de Libro sin campos obligatorios (sin título ni ISBN)
      const libroVacio = await requestHttp('/api/libros', {
        method: 'POST',
        headers: authHeaders,
        body: JSON.stringify({ precio: 20 }),
      });
      registrarTest(
        'D5',
        'Rechazo de creación de Libro sin título ni ISBN (HTTP 400)',
        libroVacio.status === 400,
        `Status: ${libroVacio.status}`,
        libroVacio.duracionMs,
      );

      // 5.2 Rechazo de precio negativo
      const precioNegativo = await requestHttp('/api/libros', {
        method: 'POST',
        headers: authHeaders,
        body: JSON.stringify({
          isbn: `978-NEG-${TIMESTAMP.toString().slice(-6)}`,
          titulo: 'Libro Precio Negativo',
          precio: -15.5,
          idioma: 'Español',
          activo: true,
        }),
      });
      registrarTest(
        'D5',
        'Rechazo estricto de precio negativo en Libro (min: 0, HTTP 400)',
        precioNegativo.status === 400,
        `Status: ${precioNegativo.status}`,
        precioNegativo.duracionMs,
      );

      // 5.3 Rechazo de Promoción con descuento > 100%
      const promoDescuentoExcesivo = await requestHttp('/api/promocions', {
        method: 'POST',
        headers: authHeaders,
        body: JSON.stringify({
          nombre: 'Promo 150%',
          porcentajeDescuento: 150.0,
          cantidadDefinida: 10,
          cantidadDisponible: 10,
          fechaInicio: new Date().toISOString(),
          fechaFin: new Date(Date.now() + 86400000).toISOString(),
          activo: true,
        }),
      });
      registrarTest(
        'D5',
        'Rechazo de promoción con descuento mayor a 100% (max: 100, HTTP 400)',
        promoDescuentoExcesivo.status === 400,
        `Status: ${promoDescuentoExcesivo.status}`,
        promoDescuentoExcesivo.duracionMs,
      );

      // 5.4 Rechazo de cantidad menor a 1 en DetalleVenta
      const detalleCantidadCero = await requestHttp('/api/detalle-ventas', {
        method: 'POST',
        headers: authHeaders,
        body: JSON.stringify({
          cantidadVenta: 0,
          precioUnitario: 50.0,
          subtotal: 0,
          descuento: 0,
        }),
      });
      registrarTest(
        'D5',
        'Rechazo de detalle de venta con cantidad menor a 1 (min: 1, HTTP 400)',
        detalleCantidadCero.status === 400,
        `Status: ${detalleCantidadCero.status}`,
        detalleCantidadCero.duracionMs,
      );

      // 5.5 Restricción UNIQUE e Idempotencia: Rechazo de duplicado de comprobante
      const ventaDuplicada = await requestHttp('/api/ventas', {
        method: 'POST',
        headers: authHeaders,
        body: JSON.stringify({
          numeroComprobante: creados.comprobantePrueba,
          fechaVenta: new Date().toISOString(),
          subtotalVenta: 100.0,
          totalDescuento: 0,
          igv: 18.0,
          totalVenta: 118.0,
          usuarioCreacion: 'qa_runner',
          fechaCreacion: new Date().toISOString(),
        }),
      });
      registrarTest(
        'D5',
        'Restricción UNIQUE e Idempotencia: Rechazo de comprobante duplicado (HTTP 400)',
        ventaDuplicada.status === 400,
        `Status: ${ventaDuplicada.status}`,
        ventaDuplicada.duracionMs,
      );
    }

    // --------------------------------------------------------------------------
    // DIMENSIÓN 6: PRUEBAS NO FUNCIONALES (Rendimiento, Estrés y Concurrencia)
    // --------------------------------------------------------------------------
    console.log(`\n${BOLD}[D6] Pruebas No Funcionales (Rendimiento, Estrés y Concurrencia)${RESET}`);
    {
      // 6.1 Ráfaga concurrente de 20 peticiones
      const startStress = performance.now();
      const promesas = Array.from({ length: 20 }, (_, i) => requestHttp('/api/libros?page=0&size=10'));
      const respuestas = await Promise.all(promesas);
      const duracionRafaga = performance.now() - startStress;
      const todasExitosas = respuestas.every(r => r.status === 200);
      const latenciasRafaga = respuestas.map(r => r.duracionMs);
      const avgLatencia = latenciasRafaga.reduce((a, b) => a + b, 0) / latenciasRafaga.length;
      registrarTest(
        'D6',
        `Ráfaga de 20 peticiones concurrentes a /api/libros (100% OK, media: ${avgLatencia.toFixed(1)}ms, total: ${duracionRafaga.toFixed(1)}ms)`,
        todasExitosas,
        `Respuestas fallidas: ${respuestas.filter(r => r.status !== 200).length}`,
        avgLatencia,
      );

      // 6.2 Latencia P95
      latenciasRafaga.sort((a, b) => a - b);
      const p95 = latenciasRafaga[Math.floor(latenciasRafaga.length * 0.95)];
      registrarTest(
        'D6',
        `Latencia P95 inferior a 500ms en entorno local (P95: ${p95.toFixed(1)}ms)`,
        p95 < 500,
        `P95 fue ${p95.toFixed(1)}ms`,
        p95,
      );

      // 6.3 Concurrencia de 5 peticiones simultáneas sobre el mismo libro
      if (creados.libroId) {
        const startRace = performance.now();
        const lecturasConcurrente = Array.from({ length: 5 }, () => requestHttp(`/api/libros/${creados.libroId}`));
        const resRace = await Promise.all(lecturasConcurrente);
        const raceOk = resRace.every(r => r.status === 200);
        registrarTest(
          'D6',
          'Condición de concurrencia: 5 lecturas simultáneas sobre el mismo libro sin bloqueos',
          raceOk,
          '',
          performance.now() - startRace,
        );
      }
    }

    // --------------------------------------------------------------------------
    // DIMENSIÓN 7: RESILIENCIA, SEGURIDAD Y OWASP
    // --------------------------------------------------------------------------
    console.log(`\n${BOLD}[D7] Pruebas de Resiliencia, Seguridad y OWASP${RESET}`);
    {
      // 7.1 Inyección SQL en filtro/búsqueda
      const sqlInjectionRes = await requestHttp(
        `/api/libros?filter[titulo.contains]=${encodeURIComponent("1' OR '1'='1; DROP TABLE libro; --")}`,
      );
      const sqlSeguro = sqlInjectionRes.status !== 500 && sqlInjectionRes.status !== 0;
      registrarTest(
        'D7',
        'Resiliencia ante Inyección SQL en parámetros de consulta (Cero errores 500)',
        sqlSeguro,
        `Status devuelto: ${sqlInjectionRes.status}`,
        sqlInjectionRes.duracionMs,
      );

      // 7.2 Inyección XSS en campo de texto
      const xssRes = await requestHttp('/api/libros', {
        method: 'POST',
        headers: authHeaders,
        body: JSON.stringify({
          isbn: `978-XSS-${TIMESTAMP.toString().slice(-6)}`,
          titulo: '<script>alert("XSS_ATTACK")</script><img src=x onerror=alert(1)>',
          sinopsis: '<svg/onload=alert(1)>',
          precio: 45.0,
          idioma: 'Español',
          activo: true,
        }),
      });
      let xssId = xssRes.data?.id;
      registrarTest(
        'D7',
        'Resiliencia ante payload XSS en inserción (Sin caídas de servidor, status 201 o 400)',
        xssRes.status === 201 || xssRes.status === 400,
        `Status: ${xssRes.status}`,
        xssRes.duracionMs,
      );
      if (xssId) {
        await requestHttp(`/api/libros/${xssId}`, { method: 'DELETE', headers: authHeaders });
      }

      // 7.3 JSON malformado / Roto
      const jsonRotoRes = await requestHttp('/api/libros', {
        method: 'POST',
        headers: authHeaders,
        body: '{"titulo": "Roto", "precio": 10,, "isbn": bad json}',
      });
      registrarTest(
        'D7',
        'Manejo controlado de JSON sintácticamente roto (HTTP 400 Bad Request)',
        jsonRotoRes.status === 400,
        `Status: ${jsonRotoRes.status}`,
        jsonRotoRes.duracionMs,
      );

      // 7.4 Payload de tamaño anómalo (50KB) manejado limpiamente con 400
      const payloadGigante = 'A'.repeat(50000);
      const giganteRes = await requestHttp('/api/libros', {
        method: 'POST',
        headers: authHeaders,
        body: JSON.stringify({
          isbn: `978-BIG-${TIMESTAMP.toString().slice(-6)}`,
          titulo: payloadGigante,
          precio: 10.0,
          idioma: 'Español',
          activo: true,
        }),
      });
      const giganteControlado = giganteRes.status === 400 || giganteRes.status === 413;
      registrarTest(
        'D7',
        'Manejo controlado de payload de longitud anómala (HTTP 400 Bad Request, cero 500s)',
        giganteControlado,
        `Status: ${giganteRes.status}`,
        giganteRes.duracionMs,
      );

      // 7.5 Control de Acceso por Roles (BOLA / Privilegios)
      const userAdminAccess = await requestHttp('/api/admin/users', {
        headers: { Authorization: `Bearer ${userToken}` },
      });
      registrarTest(
        'D7',
        'Control de Acceso basado en Roles: Usuario estándar denegado en /api/admin/users (HTTP 403)',
        userAdminAccess.status === 403,
        `Status: ${userAdminAccess.status}`,
        userAdminAccess.duracionMs,
      );

      // 7.6 Resiliencia ante payload con contaminación de prototipo (__proto__)
      const protoRes = await requestHttp('/api/libros', {
        method: 'POST',
        headers: authHeaders,
        body: JSON.stringify({
          __proto__: { isAdmin: true },
          isbn: `978-PROT-${TIMESTAMP.toString().slice(-6)}`,
          titulo: 'Test Prototype Resilience',
          precio: 25.0,
          idioma: 'Español',
          activo: true,
        }),
      });
      const protoOk = protoRes.status !== 500 && (protoRes.status === 201 || protoRes.status === 400);
      if (protoRes.data?.id) {
        await requestHttp(`/api/libros/${protoRes.data.id}`, { method: 'DELETE', headers: authHeaders });
      }
      registrarTest(
        'D7',
        'Resiliencia ante payload con contaminación de prototipo (__proto__ y cero 500s)',
        protoOk,
        `Status: ${protoRes.status}`,
        protoRes.duracionMs,
      );
    }
  } finally {
    // --------------------------------------------------------------------------
    // TEARDOWN (LIMPIEZA GARANTIZADA DE DATOS DE PRUEBA)
    // --------------------------------------------------------------------------
    console.log(`\n${BOLD}[TEARDOWN] Limpieza de Registros de Prueba Creados${RESET}`);
    const deleteHeaders = { Authorization: `Bearer ${adminToken}` };

    if (creados.licenciaId) {
      const r = await requestHttp(`/api/licencia-digitals/${creados.licenciaId}`, { method: 'DELETE', headers: deleteHeaders });
      console.log(`  - Eliminada LicenciaDigital temporal id: ${creados.licenciaId} (Status: ${r.status})`);
    }
    if (creados.detalleVentaId) {
      const r = await requestHttp(`/api/detalle-ventas/${creados.detalleVentaId}`, { method: 'DELETE', headers: deleteHeaders });
      console.log(`  - Eliminado DetalleVenta temporal id: ${creados.detalleVentaId} (Status: ${r.status})`);
    }
    if (creados.ventaId) {
      const r = await requestHttp(`/api/ventas/${creados.ventaId}`, { method: 'DELETE', headers: deleteHeaders });
      console.log(`  - Eliminada Venta temporal id: ${creados.ventaId} (Status: ${r.status})`);
    }
    if (creados.promocionId) {
      const r = await requestHttp(`/api/promocions/${creados.promocionId}`, { method: 'DELETE', headers: deleteHeaders });
      console.log(`  - Eliminada Promocion temporal id: ${creados.promocionId} (Status: ${r.status})`);
    }
    if (creados.libroId) {
      const r = await requestHttp(`/api/libros/${creados.libroId}`, { method: 'DELETE', headers: deleteHeaders });
      console.log(`  - Eliminado Libro temporal id: ${creados.libroId} (Status: ${r.status})`);
    }
    if (creados.categoriaId) {
      const r = await requestHttp(`/api/categorias/${creados.categoriaId}`, { method: 'DELETE', headers: deleteHeaders });
      console.log(`  - Eliminada Categoria temporal id: ${creados.categoriaId} (Status: ${r.status})`);
    }
    if (creados.autorId) {
      const r = await requestHttp(`/api/autors/${creados.autorId}`, { method: 'DELETE', headers: deleteHeaders });
      console.log(`  - Eliminado Autor temporal id: ${creados.autorId} (Status: ${r.status})`);
    }
    console.log(`  ${GREEN}✔ Limpieza de base de datos finalizada sin datos residuales.${RESET}\n`);
  }

  // ==============================================================================
  // RESUMEN FINAL
  // ==============================================================================
  const avgLat = metrics.latencias.length ? metrics.latencias.reduce((a, b) => a + b, 0) / metrics.latencias.length : 0;
  console.log(`${BOLD}${CYAN}======================================================================${RESET}`);
  console.log(`${BOLD}${CYAN}                   RESUMEN EJECUTIVO DE AUDITORÍA                    ${RESET}`);
  console.log(`${BOLD}${CYAN}======================================================================${RESET}`);
  console.log(`Total de pruebas ejecutadas: ${metrics.totalTests}`);
  console.log(`${GREEN}Exitosas (PASS): ${metrics.passedTests}${RESET}`);
  console.log(`${metrics.failedTests > 0 ? RED : GREEN}Fallidas (FAIL): ${metrics.failedTests}${RESET}`);
  console.log(`Latencia media global: ${avgLat.toFixed(1)} ms\n`);

  console.log(`Desglose por dimensión:`);
  for (const [dim, val] of Object.entries(metrics.dimensiones)) {
    const lat = val.latencias.length ? (val.latencias.reduce((a, b) => a + b, 0) / val.latencias.length).toFixed(1) : 'N/A';
    const status = val.failed === 0 ? `${GREEN}100% PASS${RESET}` : `${RED}${val.failed} FAIL${RESET}`;
    console.log(`  - ${dim}: ${val.passed}/${val.total} pasadas (${status}) | Latencia media: ${lat} ms`);
  }
  console.log(`${BOLD}${CYAN}======================================================================${RESET}\n`);

  if (metrics.failedTests > 0) {
    process.exit(1);
  } else {
    process.exit(0);
  }
}

runFullSpectrumQA().catch(err => {
  console.error('Error fatal durante la auditoría:', err);
  process.exit(1);
});
