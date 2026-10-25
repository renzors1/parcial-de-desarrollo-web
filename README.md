# Plataforma de Gestión y Venta de Libros Digitales (`ejemplo01`)

Proyecto integral desarrollado con **Spring Boot (Java 21)** en el backend y **Angular 22** en el frontend, siguiendo una arquitectura limpia por capas, desacoplada y con un sistema de diseño editorial moderno (*Clean Craft & Soft Badges*).

---

## 👥 Guía para Colaboradores (Trabajar en Equipo con GitHub)

Si eres compañero de equipo y acabas de clonar el proyecto, sigue esta guía para dejarlo funcionando en tu máquina en menos de 5 minutos.

### 1. Clonar el repositorio
Abre tu terminal (PowerShell, Git Bash o CMD) y corre:
```bash
git clone https://github.com/renzors1/parcial-de-desarrollo-web.git
cd parcial-de-desarrollo-web
```

---

## ⚙️ Puertos y Configuración del Entorno Local

| Componente | Puerto | URL Local | Descripción |
| :--- | :---: | :--- | :--- |
| **Frontend (Angular)** | `4200` | [http://localhost:4200](http://localhost:4200) | Aplicación cliente SPA con recarga en caliente (HMR). |
| **Backend (API Spring)**| `9999` | [http://localhost:9999](http://localhost:9999) | Servidor REST API y seguridad JWT. |
| **Consola Base de Datos**| `9999` | [http://localhost:9999/h2-console](http://localhost:9999/h2-console) | Consola web de la base de datos H2 en memoria. |
| **Documentación OpenAPI**| `9999` | [http://localhost:9999/swagger-ui](http://localhost:9999/swagger-ui) | Swagger UI para probar endpoints de la API. |

---

## 🚀 Requisitos Previos e Instalación

### Requisitos Mínimos:
1. **Java JDK 21 (LTS)** instalado y en la variable `JAVA_HOME`.
2. **Node.js (v20 o v22)** instalado en el sistema.
3. **Git** instalado.

---

### Paso a Paso para Encender el Proyecto:

Para trabajar cómodamente, se recomienda abrir **dos terminales separadas** dentro de la carpeta del proyecto:

#### 🟢 Terminal 1: Backend (Spring Boot)
El proyecto cuenta con el ejecutable Maven Wrapper (`mvnw`), por lo que **no necesitas tener Maven instalado globalmente**:

```bash
# En Windows (PowerShell / CMD):
.\mvnw

# En Linux / Mac / Git Bash:
./mvnw
```
> *La primera vez tardará un par de minutos mientras descarga las dependencias y levanta Liquibase con la base de datos H2.*

#### 🔵 Terminal 2: Frontend (Angular)
Instala las dependencias y levanta el servidor de desarrollo:

```bash
# 1. Instalar librerías de Node
npm install

# 2. Iniciar el servidor web de desarrollo
npm start
```

> **Nota del Proxy:** El frontend ya está preconfigurado (`proxy.config.mjs`) para redirigir automáticamente todas las peticiones `/api/*` y `/management/*` hacia el puerto backend **`http://localhost:9999`**. No tendrás problemas de CORS.

---

## 🔑 Credenciales de Acceso (Entorno Local)

Para iniciar sesión en la plataforma y probar los permisos:

| Rol | Usuario | Contraseña | Permisos |
| :--- | :---: | :---: | :--- |
| **Administrador** | `admin` | `admin` | Acceso total: CRUD de libros, gestión de ventas, promociones, licencias, usuarios y métricas. |
| **Usuario Estándar** | `user` | `user` | Catálogo público, visualización y compras de libros. |

---

## 🗄️ Acceso a la Base de Datos (H2 Console)

Si necesitas ver las tablas, datos de prueba o hacer consultas SQL directas:
* **URL:** [http://localhost:9999/h2-console](http://localhost:9999/h2-console)
* **JDBC URL:** `jdbc:h2:file:./target/h2db/db/ejemplo01` (o en memoria `jdbc:h2:mem:ejemplo01`)
* **User Name:** `ejemplo01`
* **Password:** *(dejar vacío)*

---

## 🔄 Flujo de Trabajo en Git para el Equipo

Para evitar conflictos y trabajar ordenados:

1. **Antes de empezar a programar:** Descarga siempre los últimos cambios:
   ```bash
   git pull origin main
   ```
2. **Crear una rama para tu tarea/feature:**
   ```bash
   git checkout -b feature/nombre-de-tu-cambio
   ```
3. **Guardar y subir tus cambios:**
   ```bash
   git add .
   git commit -m "feat: descripción clara de lo que hiciste"
   git push origin feature/nombre-de-tu-cambio
   ```
4. **Verificar antes de subir:**
   * Frontend linter: `npm run lint`
   * Pruebas completas de regresión: `node qa-full-spectrum-audit.mjs`

---

## 🛠️ Herramientas de Auditoría y Exportación Incluidas

* **Exportar todo el código del frontend a un solo archivo de texto:**
  ```bash
  npm run export:frontend
  ```
  *(Genera `frontend_completo.txt` con el árbol de archivos y contenido completo).*

* **Ejecutar batería de 47 pruebas automáticas de QA y Seguridad (Headless):**
  ```bash
  node qa-full-spectrum-audit.mjs
  ```
