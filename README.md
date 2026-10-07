# UEES UCOM0310 — Semana 8 — Proyecto final (Ae7)

API REST de reservas con Spring Boot, organizada en capas Controller → Service → Domain → Repository.
Autor: Luis Saá Garzón

## Requisitos
- Java 21
- Maven 3.9+
- Git

## Ejecutar pruebas
```bash
mvn clean test
```
Resultado esperado: **28 pruebas, 0 fallos, BUILD SUCCESS**.

## Ejecutar la aplicación
```bash
mvn spring-boot:run
```
La API queda en `http://localhost:8080`.

## Endpoints
| Método | Ruta | Respuestas |
|---|---|---|
| GET | `/api/reservas/salud` | 200 `API activa` |
| GET | `/api/reservas/puede-cancelar?horas=N` | 200 `true`/`false` · 400 si falta `horas` o no es número |
| POST | `/api/reservas` (JSON `{"id":"R-001","tipo":"NORMAL"}`) | 201 reserva creada · 400 datos vacíos · 409 id repetido |
| GET | `/api/reservas/{id}` | 200 reserva · 404 si no existe |

Los errores de negocio se devuelven como JSON: `{"estado":404,"mensaje":"Reserva no encontrada: R-999"}`.

## Probar la API desde Windows (PowerShell)
Con la aplicación corriendo, en otra ventana y dentro de una carpeta que tenga `reserva.json`:
```powershell
curl.exe http://localhost:8080/api/reservas/salud
curl.exe "http://localhost:8080/api/reservas/puede-cancelar?horas=2"
curl.exe -i -X POST http://localhost:8080/api/reservas -H "Content-Type: application/json" --data "@reserva.json"
curl.exe -i -X POST http://localhost:8080/api/reservas -H "Content-Type: application/json" --data "@reserva.json"
curl.exe -i http://localhost:8080/api/reservas/R-001
curl.exe -i http://localhost:8080/api/reservas/R-999
```
Usar `curl.exe` (en PowerShell `curl` es un alias de otro comando). `reserva.json` contiene `{"id":"R-001","tipo":"NORMAL"}`.
Resultado esperado: `API activa`, `true`, `201`, `409`, `200`, `404`.

## Pruebas
| Clase | Qué prueba | Dobles |
|---|---|---|
| `service/ReservaServiceTest` (11) | Regla de cancelación, crear (incluye duplicado), buscar, confirmar | Mock del repositorio |
| `api/ReservaControllerTest` (10) | Códigos HTTP, validación del DTO, 404 y 409 | `@MockitoBean` del Service |
| `repository/ReservaRepositoryMemoriaTest` (3) | Repositorio real en memoria | Ninguno |
| `domain/ReservaTest` (3) | Validaciones y estados de `Reserva` | Ninguno |
| `ReservaApiIntegracionTest` (1) | Flujo completo con la aplicación levantada | Ninguno |

## Cobertura
Después de `mvn clean test`, abrir `target/site/jacoco/index.html`.
Todas las clases quedan cubiertas excepto `ReservasApplication.main`, que solo arranca la aplicación.

## Limitaciones conocidas
- Los datos se guardan en memoria y se pierden al reiniciar.
- El campo `tipo` acepta cualquier texto (no se valida contra NORMAL, VIP o ESTUDIANTE).
- No hay endpoint para confirmar ni cancelar reservas, aunque el Service y el dominio ya lo permiten.

## Ramas y versiones
- `main`: línea base del proyecto.
- `feature/integracion-api`: integración de la API.
- `release/candidato-final`: versión auditada.
- `final/ae7-proyecto-final`: refactorización final (manejo de errores HTTP) y documentación de Ae7.
- Etiqueta `v1.0.0`: versión entregada.

## Documentación
- `docs/02_AUDITORIA_FINAL_PLANTILLA.md` — auditoría de calidad.
- `docs/03_DEFENSA_GUIA.md` — guía de defensa.
- `docs/04_LABORATORIO_INTEGRACION.md` — laboratorio de integración.
- `docs/05_DECISIONES_DISENO.md` — patrones, refactorizaciones y decisiones.
- `docs/06_PULL_REQUEST.md` — descripción del Pull Request y declaración de IA.
