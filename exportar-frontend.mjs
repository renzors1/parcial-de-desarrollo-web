import fs from 'fs';
import path from 'path';

/**
 * ==============================================================================
 * SCRIPT GENERADOR DE CÓDIGO CONSOLIDADO DEL FRONTEND
 * ==============================================================================
 * Recorre y unifica todos los archivos de código fuente del Frontend de la
 * aplicación (TypeScript, HTML, SCSS, CSS, JSON de i18n y configuraciones)
 * en un único archivo de texto formateado para análisis, documentación o NotebookLM.
 *
 * USO:
 *   node exportar-frontend.mjs [nombre_archivo.txt] [--include-tests]
 *
 * EJEMPLO:
 *   node exportar-frontend.mjs frontend_completo.txt
 * ==============================================================================
 */

const projectRoot = process.cwd();
const outputFileArg = process.argv[2] && !process.argv[2].startsWith('--') ? process.argv[2] : 'frontend_completo.txt';
const outputPath = path.resolve(projectRoot, outputFileArg);
const includeTests = process.argv.includes('--include-tests');

// Extensiones permitidas de código fuente
const ALLOWED_EXTENSIONS = new Set(['.ts', '.html', '.scss', '.css', '.json']);

// Carpetas o archivos excluidos
const EXCLUDED_DIRS = new Set(['node_modules', 'dist', 'target', 'swagger-ui', '.git', '.system_generated', '.user_uploaded']);

// Archivos de configuración raíz relevantes para el frontend
const ROOT_CONFIG_FILES = ['package.json', 'tsconfig.app.json', 'proxy.config.mjs', 'eslint.config.ts'];

function shouldProcessFile(filePath) {
  const ext = path.extname(filePath).toLowerCase();
  const basename = path.basename(filePath);

  if (!ALLOWED_EXTENSIONS.has(ext)) {
    return false;
  }

  // Excluir archivos de pruebas (.spec.ts) si no se especifica la bandera
  if (!includeTests && basename.endsWith('.spec.ts')) {
    return false;
  }

  return true;
}

function scanDirectory(dir, fileList = []) {
  if (!fs.existsSync(dir)) {
    return fileList;
  }

  const entries = fs.readdirSync(dir, { withFileTypes: true });

  for (const entry of entries) {
    const fullPath = path.join(dir, entry.name);

    if (entry.isDirectory()) {
      if (!EXCLUDED_DIRS.has(entry.name)) {
        scanDirectory(fullPath, fileList);
      }
    } else if (entry.isFile()) {
      if (shouldProcessFile(fullPath)) {
        fileList.push(fullPath);
      }
    }
  }

  return fileList;
}

function main() {
  console.log('======================================================================');
  console.log('   INICIANDO GENERACIÓN DE CÓDIGO CONSOLIDADO DEL FRONTEND            ');
  console.log('======================================================================');

  const webappDir = path.join(projectRoot, 'src', 'main', 'webapp');
  const filesToExport = [];

  // 1. Agregar configuraciones raíz
  for (const configFile of ROOT_CONFIG_FILES) {
    const fullConfigPath = path.join(projectRoot, configFile);
    if (fs.existsSync(fullConfigPath)) {
      filesToExport.push(fullConfigPath);
    }
  }

  // 2. Escanear todo src/main/webapp
  scanDirectory(webappDir, filesToExport);

  // Ordenar alfabéticamente para orden predecible
  filesToExport.sort((a, b) => a.localeCompare(b));

  console.log(`Archivos detectados: ${filesToExport.length}`);
  console.log(`Incluye tests (*.spec.ts): ${includeTests ? 'SÍ' : 'NO (usa --include-tests para incluir)'}`);
  console.log(`Destino: ${outputPath}\n`);

  let totalLines = 0;
  const separator = '='.repeat(80);

  const writeStream = fs.createWriteStream(outputPath, { encoding: 'utf8' });

  // Cabecera General
  writeStream.write(`${separator}\n`);
  writeStream.write(`PROYECTO COMPLETO FRONTEND - LIBRERÍA DIGITAL\n`);
  writeStream.write(`Fecha de generación: ${new Date().toISOString()}\n`);
  writeStream.write(`Total de archivos consolidados: ${filesToExport.length}\n`);
  writeStream.write(`${separator}\n\n`);

  for (const file of filesToExport) {
    const relativePath = path.relative(projectRoot, file).replace(/\\/g, '/');
    const content = fs.readFileSync(file, 'utf8');
    const lines = content.split('\n').length;
    totalLines += lines;

    writeStream.write(`${separator}\n`);
    writeStream.write(`ARCHIVO: ${relativePath}\n`);
    writeStream.write(`${separator}\n\n`);
    writeStream.write(content);
    writeStream.write('\n\n\n');
  }

  writeStream.end(() => {
    const stats = fs.statSync(outputPath);
    const sizeKB = (stats.size / 1024).toFixed(2);
    const sizeMB = (stats.size / (1024 * 1024)).toFixed(2);

    console.log('======================================================================');
    console.log('   GENERACIÓN EXITOSA                                                 ');
    console.log('======================================================================');
    console.log(`Archivo generado : ${path.basename(outputPath)}`);
    console.log(`Ruta absoluta    : ${outputPath}`);
    console.log(`Total archivos   : ${filesToExport.length}`);
    console.log(`Total de líneas  : ${totalLines.toLocaleString()}`);
    console.log(`Tamaño           : ${sizeKB} KB (${sizeMB} MB)`);
    console.log('======================================================================\n');
  });
}

main();
