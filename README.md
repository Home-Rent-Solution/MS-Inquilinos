---

### 4. 👤 `README.md` para **ms-inquilinos** (Puerto 8083)

```markdown
# 👤 HomeRentSolution - Microservicio de Inquilinos (ms-inquilinos)

Este microservicio se encarga del registro, control, perfiles y las validaciones de los clientes arrendatarios (Inquilinos) de la plataforma.

---

## 🛠️ Tecnologías y Requisitos

* **Java:** 25
* **Framework:** Spring Boot 4.0.6
* **Base de Datos:** MySQL 8.x (vía Spring Data JPA)
* **Documentación:** Springdoc OpenAPI v2 (Swagger)

---

## ⚙️ Configuración del Entorno (`application.yml`)

El servicio cuenta con una arquitectura de perfiles dinámicos:
* **Perfil `dev` (Desarrollo):** Conectado a la base de datos `db_inquilinos_dev`.
* **Perfil `test` (Pruebas):** Suite de pruebas automatizadas para el control de inquilinos.

### Puerto de Escucha:
* **Local:** `http://localhost:8083`

---

## 🚀 Instalación y Despliegue Local

1. Asegúrate de tener creada la base de datos en tu MySQL local: `db_inquilinos_dev`.
