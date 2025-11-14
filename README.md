# 🏥 MediTrack - Backend (Spring Boot)

Backend del sistema **MediTrack**, desarrollado con **Spring Boot**, encargado de manejar usuarios, perfiles médicos, citas y autenticación con JWT.

---

## ⚙️ Dependencias Principales

El proyecto utiliza las siguientes tecnologías clave:

* **Spring Boot Web**
* **Spring Security**
* **Spring Data JPA**
* **MySQL / MariaDB**
* **JWT** (JSON Web Tokens)
* **Lombok**
* **Spring Boot DevTools**

---

## 🔌 Endpoints Principales

### Autenticación
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| **POST** | `/api/auth/login` | Inicia sesión y devuelve el JWT (token de acceso). |
| **POST** | `/api/auth/register` | Registro de nuevos usuarios. |

### Usuarios
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| **GET** | `/api/users/{codigo}` | Obtener datos de un usuario específico. |
| **GET** | `/api/users` | Listar todos los usuarios. |

### Pacientes / Citas
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| **GET** | `/api/appointments/paciente/{codigo}` | Listar citas de un paciente. |
| **POST** | `/api/appointments` | Crear una nueva cita. |

### Recuperar Contraseña
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| **POST** | `/password/request` | Solicitar token para cambio. |
| **POST** | `/password/validate` | Validar token recibido. |
| **POST** | `/password/reset` | Cambiar contraseña. |

> 🔹 **Nota:** Otros endpoints se encuentran documentados en los controladores correspondientes.

---

## 🛠️ Configuración de Variables de Entorno

En **Render** o tu entorno local, necesitas configurar las siguientes variables de entorno:

| Variable | Descripción |
| :--- | :--- |
| `SPRING_DATASOURCE_URL` | URL de la base de datos (Ej: `jdbc:mysql://...`). |
| `SPRING_DATASOURCE_USERNAME` | Usuario de la base de datos. |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de la base de datos. |
| `JWT_SECRET` | Clave secreta para firmar los JWT. |
| `SERVER_PORT` (opcional) | Puerto del servidor (default: `8080`). |

---

## 🚀 Despliegue en Render

### 1. Crear un Nuevo Servicio Web
* Tipo: **Web Service**.
* Conectar al repositorio **GitHub** del backend.
* Branch: `main` (o el que corresponda).

### 2. Configurar Build y Start Commands
* **Build Command:**
    ```bash
    ./mvnw clean package
    ```
* **Start Command:**
    ```bash
    java -jar target/mediTrack-backend-0.0.1-SNAPSHOT.jar
    ```
    > ⚠️ **Asegúrate** de que el nombre del archivo `.jar` coincida con el generado por Maven.

### 3. Configurar Variables de Entorno en Render
Añade las variables de entorno (`SPRING_DATASOURCE_*`, `JWT_SECRET`, etc.) en la configuración de Render.

### 4. Deploy
Inicia el despliegue manual o automático al hacer *push* a GitHub.

### 5. Verificación
Render proporcionará una URL pública, por ejemplo:
```arduino
[https://mediTrack-back.onrender.com](https://mediTrack-back.onrender.com)
