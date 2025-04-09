# Descripción General de la Aplicación

La aplicación es una tienda online especializada en la venta de productos electrónicos, como teléfonos, audífonos, consolas de videojuegos y relojes inteligentes. Está diseñada para ofrecer una experiencia de compra intuitiva y eficiente para los clientes, mientras que facilita la gestión y supervisión de la plataforma para los administradores y gestores.

## Roles y Funcionalidades

### 1. Usuario (Cliente)
- **Registro e inicio de sesión**: Los usuarios pueden crear una cuenta e iniciar sesión para acceder a funcionalidades personalizadas.
- **Exploración del catálogo**: Navegan por los productos disponibles, filtran por categorías, precios o características.
- **Carrito de compras**: Agregan productos al carrito, modifican cantidades o eliminan productos antes de finalizar la compra.
- **Reseñas y calificaciones**: Pueden dejar opiniones y puntuar los productos que han comprado.

### 2. Gestor 
- **Gestión del catálogo**: Agregan, modifican o eliminan productos del catálogo.
- **Control de inventario**: Actualizan el stock y la disponibilidad de los productos.
- **Interacción con clientes**: Responden a reseñas, preguntas o consultas de los usuarios.
- **Gestión de usuarios**: Pueden asignar roles, bloquear o eliminar cuentas de usuarios (bajo supervisión del administrador).

### 3. Administrador
- **Supervisión de la plataforma**: Aseguran el correcto funcionamiento de la tienda.
- **Gestión de roles**: Asignan roles (Usuario, Gestor, Administrador) y gestionan cuentas (bloqueo o eliminación).
- **Informes y estadísticas**: Acceden a informes de ventas, actividad de usuarios y métricas clave de la tienda.

---

## Requisitos Funcionales

### 1. Gestión de Usuarios y Gestores
- Registro e inicio de sesión seguro.
- Asignación de roles (Usuario, Gestor, Administrador).
- Bloqueo o eliminación de cuentas.

### 2. Catálogo de Productos
- Listado de productos con imágenes, descripciones, precios y disponibilidad.
- Filtros avanzados (por categoría, precio, marca, etc.).
- Búsqueda por palabras clave.

### 3. Carrito de Compras
- Agregar, modificar o eliminar productos del carrito.
- Visualización del total de la compra.

### 4. Procesamiento de Compras
- Opciones de pago (a implementar en futuras actualizaciones).
- Generación de facturas y confirmación de compra.

### 5. Gestión de Pedidos *(Futuras Actualizaciones)*
- Seguimiento del estado del pedido (en proceso, enviado, entregado).
- Notificaciones de actualización del pedido (por correo electrónico o en la aplicación).

### 6. Administración de la Tienda *(Futuras Actualizaciones)*
- Panel de control para administradores y gestores.
- Estadísticas de ventas, productos más vendidos y actividad de usuarios.

### 7. Reseñas y Calificaciones
- Los clientes pueden dejar reseñas y puntuar productos.
- Los gestores pueden responder a las reseñas o consultas de los clientes.

---

## Relaciones

- **Usuario → Carrito (1:1)** → Un usuario tiene un carrito.
- **Usuario → Compra (1:N)** → Un usuario puede realizar muchas compras.
- **Usuario → Reseña (1:N)** → Un usuario puede dejar muchas reseñas.
- **Producto → Carrito (M:N)** → Un producto puede estar en muchos carritos, y un carrito puede tener muchos productos.
- **Producto → Compra (M:N)** → Un producto puede estar en muchas compras, y una compra puede tener muchos productos.
- **Producto → Reseña (1:N)** → Un producto puede tener muchas reseñas.
- **Compra → Factura (1:1)** → Una compra genera una factura.

**Diagrama:**
[Enlace al diagrama](https://drive.google.com/file/d/1y-3f1LVQX3touRDQBo3iZXLlyBGF--9G/view?usp=sharing)

---

## Tecnologías a Utilizar

### 1. Base de Datos
- **PostgreSQL**: Sistema de gestión de bases de datos para almacenar y gestionar información como usuarios, productos, compras, reseñas, etc.
- **PgAdmin**: Herramienta de administración para PostgreSQL.

### 2. Desarrollo Multiplataforma
- **Python con Flask**: Framework ligero para desarrollar el backend y gestionar servicios web RESTful.
  - **Herramienta de desarrollo**: PyCharm.
- **Kotlin**: Para el desarrollo de la interfaz gráfica en aplicaciones móviles o de escritorio.
  - **Herramienta de desarrollo**: IntelliJ IDEA.

### 3. Diseño
- **Figma**: Diseño de la interfaz gráfica.
[Enlace al diseño](https://www.figma.com/design/rk9io4BbsmmsJw76MXquuK/Untitled?node-id=3-3876&t=sa8I2t0RAmQn82Gr-1)

---

## Cronograma

| Fecha | Entrega |
|--------|-------------|
| **12 Marzo** | Entrega de anteproyecto |
| **26 Marzo** | Entrega de base de datos y esqueleto del proyecto |
| **9 Abril** | Entrega básica de la interfaz con el modelo de la base de datos |
| **30 Abril** | Creación de las APIs |
| **14 Mayo** | Interfaz del proyecto con las APIs |
| **19-23 Mayo** | Entrega de proyecto |
