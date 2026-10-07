# UEES UCOM0310 — Semana 8 — Proyecto base

Proyecto base para las actividades de integración final.

## Requisitos
- Java 21
- Maven
- Git

## Ejecutar pruebas
```bash
mvn clean test
```

## Ejecutar aplicación
```bash
mvn spring-boot:run
```

## Endpoints
| Método | Ruta |
|---|---|
| GET | `/api/reservas/salud` |
| GET | `/api/reservas/puede-cancelar?horas=2` |
| POST | `/api/reservas` (JSON: `{"id":"R-001","tipo":"NORMAL"}`) |
| GET | `/api/reservas/{id}` |

Detalle de pruebas manuales, pruebas automatizadas y reto en `docs/04_LABORATORIO_INTEGRACION.md`.

## Probar la API desde Windows (PowerShell)
Con la aplicación corriendo, en otra ventana:
```powershell
curl.exe http://localhost:8080/api/reservas/salud
curl.exe "http://localhost:8080/api/reservas/puede-cancelar?horas=2"
curl.exe -i -X POST http://localhost:8080/api/reservas -H "Content-Type: application/json" --data "@reserva.json"
```
Usar `curl.exe` (en PowerShell `curl` es un alias de otro comando). El archivo `reserva.json` contiene `{"id":"R-001","tipo":"NORMAL"}`.

## Pruebas
25 pruebas en 4 clases:
| Clase | Qué prueba |
|---|---|
| `service/ReservaServiceTest` | Regla de cancelación, crear, buscar y confirmar (repositorio simulado con Mockito) |
| `api/ReservaControllerTest` | Endpoints, códigos HTTP y validación del DTO (`@WebMvcTest`) |
| `repository/ReservaRepositoryMemoriaTest` | Repositorio real en memoria |
| `domain/ReservaTest` | Validaciones del constructor y cancelación |

## Cobertura
Después de `mvn clean test`, abrir:
`target/site/jacoco/index.html`

`ReservasApplication.main` no se ejecuta en las pruebas (solo arranca la aplicación), por eso aparece sin cubrir.

## Errores conocidos
- `GET /api/reservas/{id}` con un id que no existe responde **500** en lugar de **404** (no hay manejo HTTP de errores).
- Un `POST` con un id repetido reemplaza la reserva anterior sin avisar.
- El campo `tipo` acepta cualquier texto.
- Los datos se guardan en memoria y se pierden al reiniciar.

## Ramas
- `main`: línea base del proyecto.
- `feature/integracion-api`: laboratorio de integración (Actividad 2).
- `release/candidato-final`: versión candidata auditada (Actividad 3).
