# 📚 Guía de Pruebas en Memoria RAM - BookStore Online (Blazor .NET 10)

Esta guía contiene todos los datos de prueba, credenciales de usuarios, contraseñas, ventas pre-cargadas y licencias DRM disponibles en **modo 100% en memoria (RAM)** para evaluar la aplicación sin necesidad de conectar una base de datos externa ni backend.

---

## 🌐 Enlace de la Aplicación
- **URL Local:** [http://localhost:5021](http://localhost:5021)
- **Tecnología:** Blazor Server en .NET 10 (C#)
- **Persistencia:** Modo Híbrido en Memoria RAM (Reactivo y Rápido)

---

## 🔐 1. Credenciales de Acceso (Usuarios y Contraseñas)

Puedes iniciar sesión en [http://localhost:5021/login](http://localhost:5021/login) con cualquiera de estas cuentas:

| Perfil / Rol | Usuario (Login) | Contraseña | Nombre Completo | Correo Electrónico | ¿Qué puede hacer en la app? |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **👑 Administrador** | `admin` | **`admin123`** *(o `admin`)* | Renzo Administrador | `admin@bookstore.pe` | **Acceso Total:** Tienda, Mi Carrito, Biblioteca, y **Panel de Administración** (CRUD Libros, Ventas, Licencias DRM, Promociones y Calificaciones). |
| **👤 Cliente 1** | `carlos.mendoza` | **`carlos123`** | Carlos Mendoza | `carlos.mendoza@gmail.com` | Tienda, Carrito y **2 libros ya comprados** en su biblioteca digital listos para leer y calificar. |
| **👤 Cliente 2** | `ana.torres` | **`ana123`** | Ana Torres | `ana.torres@hotmail.com` | Tienda, Carrito y **1 libro ya comprado** (*Clean Architecture*) con código DRM asignado. |
| **👤 Cliente Demo** | `user` | **`user123`** *(o `user`)* | Usuario Cliente Demo | `cliente@bookstore.pe` | Tienda, Carrito y **1 libro de Bases de Datos** comprado. Ideal para probar nuevos registros. |

> [!TIP]
> También puedes registrar un usuario nuevo en [http://localhost:5021/registro](http://localhost:5021/registro) con cualquier usuario y contraseña de tu preferencia; se guardará al instante en memoria RAM y podrás iniciar sesión inmediatamente.

---

## 📦 2. Datos de Prueba Precargados en Memoria (RAM)

### 📖 A. Catálogo de Libros (6 Títulos)
1. **C# 13 and .NET 10 – Modern Cross-Platform Development**
   - Autor: Mark J. Price | Editorial: Packt Publishing | Categoría: Programación | Precio: S/ 89.90
2. **Clean Architecture: A Craftsman's Guide to Software Structure**
   - Autor: Robert C. Martin | Editorial: O'Reilly Media | Categoría: Arquitectura | Precio: S/ 75.50
3. **Cloud Native Go: Building Web Applications and Microservices**
   - Autor: Matthew Titmus | Editorial: O'Reilly Media | Categoría: Cloud Computing | Precio: S/ 59.90
4. **Diseño de Bases de Datos Relacionales y SQL Avanzado**
   - Autor: C. J. Date | Editorial: Planeta Digital | Categoría: Base de Datos | Precio: S/ 49.00
5. **Desarrollo Web Full-Stack con Blazor y Spring Boot**
   - Autor: Martin Fowler | Editorial: Manning Publications | Categoría: Desarrollo Web | Precio: S/ 95.00
6. **Patrones de Microservicios y Sistemas Distribuidos**
   - Autor: Chris Richardson | Editorial: Manning Publications | Categoría: Arquitectura | Precio: S/ 82.50

---

### 🎟️ B. Promociones y Cupones Activos
- **`PROMO20`**: 20% de descuento en todo el catálogo (100 cupos disponibles).
- **`FLASH35`**: 35% de descuento especial en tecnología (50 cupos disponibles).
- **`LECTURA15`**: 15% de descuento en novedades editoriales (200 cupos disponibles).

*Para probarlos:* Agrega libros al carrito, selecciona una promoción del menú desplegable o escribe el cupón y verás el descuento aplicado antes del cálculo del IGV (18%).

---

### 🧾 C. Historial de Ventas Registradas (Comprobantes B001)

Visibles en el panel de **Historial de Ventas** (`/ventas`) al ingresar como `admin`:

| Comprobante | Cliente Comprador | Libros Comprados | Subtotal | Descuento | IGV (18%) | Total Fiscal |
| :--- | :--- | :--- | :---: | :---: | :---: | :---: |
| **`B001-00000101`** | `carlos.mendoza` | C# 13 + Cloud Native Go | S/ 149.80 | S/ 0.00 | S/ 26.96 | **S/ 176.76** |
| **`B001-00000102`** | `ana.torres` | Clean Architecture (con PROMO20) | S/ 75.50 | S/ 15.10 | S/ 10.87 | **S/ 71.27** |
| **`B001-00000103`** | `user` | Bases de Datos Relacionales | S/ 49.00 | S/ 0.00 | S/ 8.82 | **S/ 57.82** |
| **`B001-00000104`** | `admin` | Full-Stack Blazor y Spring Boot | S/ 95.00 | S/ 19.00 | S/ 13.68 | **S/ 89.68** |

---

### 🔑 D. Licencias Digitales DRM y Stock de Almacén

Visibles en **Gestión de Licencias** (`/admin/licencias`):

1. **Licencias ya Vendidas / Asignadas a Clientes:**
   - `LIC-2026-NET10-CM01` ➔ Asignada a `carlos.mendoza` (*C# 13 and .NET 10*).
   - `LIC-2026-GOLNG-CM02` ➔ Asignada a `carlos.mendoza` (*Cloud Native Go*).
   - `LIC-2026-ARCH-AT01` ➔ Asignada a `ana.torres` (*Clean Architecture*).
   - `LIC-2026-BD-US01` ➔ Asignada a `user` (*Bases de Datos Relacionales*).
   - `LIC-2026-BLAZOR-ADM` ➔ Asignada a `admin` (*Full-Stack Blazor*).

2. **Licencias en Almacén Disponibles para Nuevas Compras (`DISPONIBLE`):**
   - `STOCK-PACKT-NET10-001` (Libro 1: C# 13)
   - `STOCK-OREILLY-ARCH-002` (Libro 2: Clean Architecture)
   - `STOCK-MANNING-MICRO-003` (Libro 6: Microservicios)

> [!NOTE]
> Cuando realices una nueva compra en el carrito, el sistema tomará automáticamente estas licencias de almacén y las cambiará a `VENDIDA` con la fecha y hora de tu compra.

---

### ⭐ E. Calificaciones y Opiniones de Lectores

Visibles en **Calificaciones de Usuarios** (`/admin/calificaciones`):
- **Carlos Mendoza** (5 Estrellas): *"Excelente experiencia de compra y lectura digital de C# 13 y .NET 10 sin interrupciones."*
- **Ana Torres** (5 Estrellas): *"El libro de Clean Architecture es oro puro para desarrolladores. La clave DRM se activó al instante."*
- **Usuario Demo** (4 Estrellas): *"Muy buen catálogo sobre microservicios y cloud native. Interfaz súper intuitiva."*

---

## 🧪 3. Guía Paso a Paso para Probar la Aplicación

### Paso 1: Ver el Catálogo y Carrito como Cliente
1. Abre [http://localhost:5021](http://localhost:5021).
2. Usa el buscador: escribe *"Clean"* o filtra por la editorial *"O'Reilly Media"*.
3. Haz clic en **"Comprar"** en un libro. Verás la alerta de confirmación y el contador en el carrito.
4. Entra a **"Mi Carrito"**, aplica el cupón `PROMO20` y presiona **"Pagar y Generar Licencias"**.

### Paso 2: Ver tu Biblioteca y Calificar
1. Tras pagar, haz clic en **"Ir a Mis Libros y Licencias Digitales"** (o en la barra de navegación).
2. Verás tus libros adquiridos con sus códigos DRM.
3. Haz clic en **"Leer Libro Online"**: se abrirá el visor de lectura.
4. En el modal, califícalo con 5 estrellas, escribe tu comentario y haz clic en **"Enviar Calificación"**.

### Paso 3: Entrar como Administrador
1. Haz clic en **"Iniciar Sesión"** en el menú lateral.
2. Ingresa `admin` y contraseña `admin123`.
3. Verás que se despliega el bloque dorado **"Panel de Administración"** con:
   - **Gestión de Libros:** Crea o edita libros del catálogo.
   - **Historial de Ventas:** Visualiza los comprobantes con IGV y compradores.
   - **Gestión de Licencias:** Revisa el porcentaje de licencias vendidas y el stock disponible.
   - **Gestión de Promociones:** Configura cupones y cupos.
   - **Calificaciones:** Revisa el promedio de satisfacción general de los clientes.
