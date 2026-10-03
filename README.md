![Duoc UC](https://www.duoc.cl/wp-content/uploads/2022/09/logo-0.png)

# 🧠 Semana 8 | Actividad Sumativa 3: Persistiendo datos con objetos y base de datos

# 🚚 Sistema de Gestión - SpeedFast

## 👤 Autor del proyecto

- **Nombre completo:** Fabrizio Fernandini
- **Ramo:** Desarrollo Orientado a Objetos II
- **Sección:** 005A
- **Carrera:** Analista Programador Computacional
- **Sede:** Campus Virtual

---

## 📝 Descripción del Sistema

SpeedFast es una aplicación de escritorio desarrollada en Java para la gestión y
simulación de envíos a domicilio.

El sistema administra de manera completa las operaciones CRUD 
(Crear, Leer, Actualizar y Eliminar), garantizando la integridad referencial 
de los datos, el cierre automático de recursos mediante `try-with-resources`, 
el uso seguro de `PreparedStatement`, y validaciones estrictas en la interfaz 
gráfica (formatos de fecha, hora y tipos de datos).

---

## 🧱 Estructura de paquetes y clases

```plaintext
SpeedFastS8/
└── 📁 src/
│   └── 📁 main/
│       └── 📁 java/
│           └── 📁 cl/
│               └── 📁 speedfast/
│                   ├── 📁 app/
│                   │   └── 📄 Main.java
│                   ├── 📁 controller/
│                   │   └── 📄 ControladorPedidos.java
│                   ├── 📁 dao/
│                   │   ├── 📄 EntregaDAO.java
│                   │   ├── 📄 PedidoDAO.java
│                   │   └── 📄 RepartidorDAO.java
│                   ├── 📁 gui/
│                   │   ├── 📄 VentanaEntregas.java
│                   │   ├── 📄 VentanaEntregas.form
│                   │   ├── 📄 VentanaListaPedidos.java
│                   │   ├── 📄 VentanaListaPedidos.form
│                   │   ├── 📄 VentanaPrincipal.java
│                   │   ├── 📄 VentanaPrincipal.form
│                   │   ├── 📄 VentanaRegistroPedido.java
│                   │   ├── 📄 VentanaRegistroPedido.form
│                   │   ├── 📄 VentanaRepartidores.java
│                   │   └── 📄 VentanaRepartidores.form
│                   ├── 📁 model/
│                   │   ├── 📄 Entrega.java
│                   │   ├── 📄 EstadoPedido.java
│                   │   ├── 📄 Pedido.java
│                   │   ├── 📄 Repartidor.java
│                   │   ├── 📄 TipoPedido.java
│                   │   └── 📄 ZonaDeCarga.java
│                   └── 📁 util/
│                       ├── 📄 ConexionBD.java
│                       └── 📄 UtilidadesGui.java
├── pom.xml
├── README.md
└── BBDD.sql

```
---

---

---

## 🚀 Funcionalidades Principales del Sistema (CRUD)

El sistema **SpeedFast S8** gestiona la persistencia de datos mediante el patrón DAO sobre MySQL, integrando las siguientes funcionalidades a través de sus interfaces gráficas:

*   **🛡️ Validación al Iniciar:** Comprueba la conexión con la base de datos antes de cargar la interfaz principal; si falla, notifica al usuario y finaliza la aplicación.
*   **📝 Registrar Pedido:** Permite ingresar la dirección, tipo (`COMIDA`, `ENCOMIENDA`, `EXPRESS`) y estado inicial (`PENDIENTE`, `EN_REPARTO`, `ENTREGADO`). Incluye botón para limpiar campos.
*   **📋 Listar Pedidos:** Muestra los pedidos en tabla con opción de **filtros combinados** por Tipo y Estado, además de botones para actualizar, editar datos y eliminar registros.
*   **🚴 Gestión de Repartidores:** Permite crear nuevos repartidores, actualizar sus nombres, listar en tabla y eliminarlos (validando restricciones de clave foránea).
*   **🚚 Gestión de Entregas:** Asocia pedidos y repartidores mediante desplegables dinámicos (`ComboItem`). Valida estrictamente la fecha (`DD-MM-AAAA`) y hora (`HH:MM:SS`), mostrando el estado del pedido en la tabla y permitiendo su edición y eliminación.
*   **🚪 Salir:** Cierra la aplicación de manera segura previa confirmación del usuario.

### 🛠️ Prerrequisitos

* Tener instalado **Java JDK 17, 21 o superior (Desarrollado y testeado en JDK 26)**.
* Un IDE compatible (se recomienda IntelliJ IDEA).
* **Gestor de proyectos:** Apache Maven (incluido por defecto en IntelliJ IDEA).
* Base de Datos: MySQL Server 8.0 o superior instalado y en ejecución.

### 🗄️ Configuración de la Base de Datos

Abrir su cliente de MySQL (MySQL Workbench, DBeaver o consola) y ejecutar el 
script de creación de tablas ubicado en la raíz del proyecto (`BBDD.sql`):

```SQL
CREATE DATABASE IF NOT EXISTS speedfast_db;
USE speedfast_db;

CREATE TABLE repartidores (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              nombre VARCHAR(100) NOT NULL
);

CREATE TABLE pedidos (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         direccion VARCHAR(100) NOT NULL,
                         tipo ENUM('COMIDA','ENCOMIENDA','EXPRESS'),
                         estado ENUM('PENDIENTE','EN_REPARTO','ENTREGADO')
);

CREATE TABLE entregas (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          id_pedido INT,
                          id_repartidor INT,
                          fecha DATE,
                          hora TIME,
                          FOREIGN KEY (id_pedido) REFERENCES pedidos(id),
                          FOREIGN KEY (id_repartidor) REFERENCES repartidores(id)
);


```

### ⚙️ Configuración de Credenciales de Conexión

Antes de ejecutar la aplicación, se deben ajustar los parámetros de acceso a la 
base de datos local:

```java
// Archivo: cl.speedfast.util.ConexionBD

private static final String URL = "jdbc:mysql://127.0.0.1:3306/speedfast_db"; // Reemplazar con la ruta de la BBDD
private static final String USER = "usuario";           // Reemplazar por usuario de MySQL (ej: root)
private static final String PASSWORD = "contrasenia";   // Reemplazar por contraseña local
```

## 🚀 Instrucciones para ejecutar Main

1. **Clonar/Abrir el proyecto:** Asegúrese de estar en la rama correspondiente a la Semana actual.

2. **Abra el proyecto en su IDE** (IntelliJ IDEA de preferencia).

3. **Cargar la Base de Datos:** Ejecute el archivo `BBDD.sql` en su servidor local de MySQL.

4. Cargar dependencias de Maven: Al abrir el proyecto, el IDE descargará 
automáticamente la dependencia del conector MySQL (mysql-connector-j) 
definida en el archivo pom.xml. Si no lo hace de forma automática, 
ejecute Reload All Maven Projects (en IntelliJ a la derecha aparece una M, pulsar botón reload).

5. Navegue hasta la clase cl.speedfast.app.Main.

6. Ejecute el método main.

---

## Repositorio

```bash
https://github.com/Capa8cl/poo2-speedfast-semana8
```

---
© Duoc UC | Escuela de Informática y Telecomunicaciones | Semana 8