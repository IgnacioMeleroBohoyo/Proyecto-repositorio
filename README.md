Proyecto Demo: Spring Boot + PostgreSQL + Angular

Este repositorio contiene un ejemplo mínimo pero completo que demuestra:

- API REST con Spring Boot
- Autenticación JWT (registro / login / endpoints protegidos)
- Persistencia en PostgreSQL (JPA/Hibernate)
- Frontend SPA en Angular que consume la API
- Contenedores Docker y `docker-compose` para orquestar la app completa

Estructura relevante:

- `backend/` — código Java Spring Boot
- `frontend/` — aplicación Angular + Dockerfile
- `docker-compose.yml` — Postgres, backend y frontend

Rápido inicio (Docker)

1. Levantar todo (compila backend y frontend y arranca contenedores):

```bash
docker-compose up --build
```

Servicios expuestos localmente:

- Backend: http://localhost:8080
- Frontend (nginx): http://localhost:4200

Ejecutar solo backend (sin Docker)

```bash
cd backend
mvn -B -DskipTests package
java -jar target/demo-backend-0.0.1-SNAPSHOT.jar
```

Variables de entorno útiles (docker-compose ya las inyecta):

- `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET` (cambiar para producción)
- `CORS_ALLOWED_ORIGINS` (orígenes permitidos separados por comas)

Despliegue usando GitHub Pages

GitHub Pages solo ejecuta el frontend estático. El backend y PostgreSQL deben ejecutarse
en un servicio externo compatible con Docker, por ejemplo Render, Railway o Azure.

1. Publica este repositorio en GitHub.
2. Despliega `backend/` y PostgreSQL en tu proveedor, configura
   `CORS_ALLOWED_ORIGINS` con la URL final de GitHub Pages y copia la URL HTTPS del
   backend, por ejemplo `https://mi-backend.example.com`.
3. En GitHub, abre `Settings > Secrets and variables > Actions > Variables` y crea
   `API_URL` con esa URL, sin terminar en `/`.
4. En `Settings > Pages`, selecciona `GitHub Actions` como fuente de publicación.

El workflow `.github/workflows/deploy-pages.yml` construirá y publicará Angular
automáticamente en cada push a `main`. La URL será
`https://TU_USUARIO.github.io/NOMBRE_DEL_REPOSITORIO/`.

Desarrollo del frontend (servidor dev con proxy a backend):

```bash
cd frontend
npm install
npm start
```

El servidor dev usa `proxy.conf.json` para reenviar `/api` a `http://localhost:8080`.

API - ejemplos curl

1. Registro de usuario:

```bash
curl -X POST http://localhost:8080/api/auth/register \
	-H "Content-Type: application/json" \
	-d '{"username":"alice","password":"cambiame"}'
```

2. Login (recibe token JWT):

```bash
curl -X POST http://localhost:8080/api/auth/login \
	-H "Content-Type: application/json" \
	-d '{"username":"alice","password":"cambiame"}'
```

Respuesta esperada:

```json
{ "token": "<JWT_TOKEN>" }
```

3. Obtener usuario actual (`/api/users/me`) usando el token:

```bash
curl http://localhost:8080/api/users/me \
	-H "Authorization: Bearer <JWT_TOKEN>"
```

4. Health check (útil para orquestadores):

```bash
curl http://localhost:8080/api/health
```

Respuesta esperada:

```json
{ "status": "UP" }
```

Notas de despliegue y producción

- Cambia `JWT_SECRET` por un secreto seguro antes de exponer la API.
- Para producción se recomienda servir el frontend desde un CDN o desde el propio servidor backend.
- Los Dockerfiles incluidos construyen el backend con Maven y el frontend con Node, sirviendo la SPA mediante nginx.

Robustez y orquestación

- `docker-compose.yml` incluye `healthcheck` y políticas `restart` para servicios (Postgres, backend, frontend) para reinicios automáticos y comprobaciones de salud.
- El `backend/Dockerfile` define un `HEALTHCHECK` que consulta `/api/health`.

Próximos pasos que puedo ejecutar por ti:

- Añadir README más detallado con ejemplos Postman y colección exportable
- Añadir tests unitarios/integración y pipeline CI (GitHub Actions)
- Mejorar manejo de errores y validaciones en el backend
