# Laboratorio de integración Spring Boot – Semana 8

Estudiante: Luis Saá Garzón · Rama: `feature/integracion-api`

## Endpoints de la API
| Método | Ruta | Qué hace | Capa que decide |
|---|---|---|---|
| GET | `/api/reservas/salud` | Comprueba que la API está activa | Controller |
| GET | `/api/reservas/puede-cancelar?horas=N` | Aplica la regla de cancelación (>= 2 horas) | Service |
| POST | `/api/reservas` | Crea una reserva a partir de `CrearReservaRequest` | DTO valida, Service crea, Domain valida id |
| GET | `/api/reservas/{id}` | Busca una reserva por id | Service |

## Pruebas manuales (curl en Windows)
| Petición | Respuesta esperada |
|---|---|
| GET salud | 200 · `API activa` |
| GET puede-cancelar?horas=2 | 200 · `true` |
| GET puede-cancelar?horas=1 | 200 · `false` |
| GET puede-cancelar (sin horas) | 400 Bad Request |
| POST `{"id":"R-001","tipo":"NORMAL"}` | 201 · `{"id":"R-001","tipo":"NORMAL","estado":"PENDIENTE"}` |
| POST `{"id":"","tipo":"NORMAL"}` | 400 Bad Request |
| GET /api/reservas/R-001 (después del POST) | 200 · la reserva creada |
| GET /api/reservas/R-999 | 500 Internal Server Error |

## Pruebas automatizadas
- `service/ReservaServiceTest`: 10 pruebas (regla de cancelación, crear, buscar y confirmar).
- `api/ReservaControllerTest`: 9 pruebas con `@WebMvcTest` y `@MockitoBean` (códigos HTTP, validación del DTO y delegación al Service).
- Total: 19 pruebas.

## Reto: GET /api/reservas/{id} con un id que no existe
**Comportamiento actual:** el Service lanza `IllegalArgumentException("Reserva no encontrada")`. Nadie la maneja, así que Spring responde **500 Internal Server Error** y el mensaje no llega al cliente. La prueba `buscarIdInexistenteHoyNoTieneManejoHttpDeErrores` deja registrado este comportamiento.

**Mejora futura:**
1. Crear una excepción propia, `ReservaNoEncontradaException`, para no confundir "no existe" con "datos inválidos" (`Id obligatorio` también es `IllegalArgumentException`).
2. Agregar un `@RestControllerAdvice` que la convierta en **404 Not Found** con un mensaje claro.
3. Cambiar la prueba del reto para que espere `status().isNotFound()`.

## Limitaciones observadas
- El repositorio es en memoria: los datos se pierden al reiniciar la aplicación.
- Un POST con un id repetido reemplaza la reserva anterior (no hay 409 Conflict).
- El campo `tipo` acepta cualquier texto (por ejemplo `"XYZ"`).
