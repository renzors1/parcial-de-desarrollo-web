# Sistema de Venta de Libros Digitales (BookStore)

Proyecto web para la venta de publicaciones tecnicas y distribucion de licencias de lectura digital (DRM). El sistema esta compuesto por un backend en Spring Boot (JHipster) y un frontend interactivo desarrollado en Blazor Server (.NET 10).

---

## Requisitos del Sistema

Para compilar y ejecutar el proyecto en un entorno local se requiere:

- Java JDK 17 o 21 (variable de entorno JAVA_HOME configurada)
- .NET SDK 10 (o .NET 8+)
- Git

---

## Instrucciones de Ejecucion

El sistema requiere iniciar el backend y el frontend en dos terminales independientes:

### 1. Iniciar el Backend (Spring Boot API)

Desde la carpeta raiz del proyecto, ejecutar el wrapper de Maven:

**En Windows (PowerShell / CMD):**

```bash
.\mvnw.cmd
```

**En Linux / Mac / Git Bash:**

```bash
./mvnw
```

El servidor backend iniciara en el puerto 9999 y levantara automaticamente la base de datos H2 con sus esquemas y datos iniciales.

### 2. Iniciar el Frontend (Blazor Server)

En una segunda terminal, ingresar a la carpeta del cliente y ejecutar:

```bash
cd BlazorApp1
dotnet run
```

La aplicacion web estara disponible en el navegador en la direccion: `http://localhost:5021`

---

## Puertos y Servicios Locales

| Servicio                 | Puerto | URL Local                        | Descripcion                                                           |
| ------------------------ | ------ | -------------------------------- | --------------------------------------------------------------------- |
| Frontend (Blazor Server) | 5021   | http://localhost:5021            | Aplicacion web: catalogo, carrito, lectura y panel de administracion. |
| Backend (Spring Boot)    | 9999   | http://localhost:9999            | API RESTful y servicio de autenticacion con JWT.                      |
| Consola H2 Database      | 9999   | http://localhost:9999/h2-console | Administrador web para consultas directas a la base de datos.         |
| Documentacion OpenAPI    | 9999   | http://localhost:9999/swagger-ui | Interfaz Swagger UI para consultar y probar endpoints.                |

---

## Cuentas de Acceso y Usuarios de Prueba

Para ingresar al sistema y evaluar las funcionalidades segun los roles disponibles, se pueden emplear las siguientes credenciales:

| Perfil           | Usuario        | Contrasena         | Roles                 | Alcance de Permisos                                                                      |
| ---------------- | -------------- | ------------------ | --------------------- | ---------------------------------------------------------------------------------------- |
| Administrador    | admin          | admin (o admin123) | ROLE_ADMIN, ROLE_USER | Acceso total: administracion de libros (CRUD), promociones, usuarios, ventas y reportes. |
| Cliente Lector   | carlos.mendoza | carlos123          | ROLE_USER             | Acceso a compras, carrito, licencias DRM y biblioteca personal de lectura.               |
| Cliente Lector   | ana.torres     | ana123             | ROLE_USER             | Exploracion de catalogo, adquisicion de libros y biblioteca digital.                     |
| Cliente Estandar | user           | user (o user123)   | ROLE_USER             | Perfil de usuario comun para navegacion general y compras.                               |

Nota: Tambien es posible registrar un nuevo lector desde el formulario de la aplicacion (`http://localhost:5021/registro`). La sesion se inicia de forma automatica al completar el registro.

---

## Acceso a la Base de Datos (H2 Console)

Para consultar las tablas del sistema directamente desde el navegador:

- URL: http://localhost:9999/h2-console
- Driver Class: `org.h2.Driver`
- JDBC URL: `jdbc:h2:file:./target/h2db/db/ejemplo01` (o en memoria `jdbc:h2:mem:ejemplo01`)
- User Name: `ejemplo01`
- Password: _(dejar vacio)_

---

## Flujo de Trabajo en Git

Para el trabajo colaborativo en el repositorio:

1. Descargar las ultimas actualizaciones antes de trabajar:

```bash
git pull origin main
```

2. Crear una rama para la tarea correspondiente:

```bash
git checkout -b feature/nombre-de-tarea
```

3. Confirmar los cambios realizados localmente:

```bash
git add .
git commit -m "descripcion clara de los cambios realizados"
git push origin feature/nombre-de-tarea
```
