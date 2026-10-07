# Decisiones de diseño, patrones y refactorizaciones

## Arquitectura
| Capa | Clases | Responsabilidad |
|---|---|---|
| API | `ReservaController`, `CrearReservaRequest`, `ManejadorErrores`, `ErrorRespuesta` | Traducir HTTP ↔ Java. Sin reglas de negocio. |
| Service | `ReservaService`, `ReservaNoEncontradaException`, `ReservaDuplicadaException` | Reglas del negocio y coordinación con el repositorio. |
| Domain | `Reserva`, `EstadoReserva` | Proteger sus propias invariantes (id obligatorio, estado). |
| Repository | `ReservaRepository`, `ReservaRepositoryMemoria` | Guardar y buscar reservas. |

## Patrones utilizados
| Patrón | Dónde | Problema que resuelve |
|---|---|---|
| Repository | `ReservaRepository` + `ReservaRepositoryMemoria` | El Service no sabe cómo se guardan los datos; se puede cambiar a una base de datos sin tocarlo y probar con un mock. |
| DTO | `CrearReservaRequest`, `ErrorRespuesta` | El cliente solo envía id y tipo, no puede mandar el estado. La salida de errores tiene un formato fijo. |
| Inyección de dependencias | Constructores de `ReservaController` y `ReservaService` | Bajo acoplamiento: Spring entrega las dependencias y en las pruebas se reemplazan por dobles. |
| Arquitectura en capas | Paquetes `api`, `service`, `domain`, `repository` | Cada cambio se hace en un solo lugar. |
| Manejador centralizado de errores (`@RestControllerAdvice`) | `ManejadorErrores` | Convierte excepciones de negocio en 404/409 sin try-catch en el Controller. |

## Patrones evitados
| Patrón | Por qué no se usó |
|---|---|
| Strategy | Solo hay una regla de cancelación. Crear una interfaz y una clase por regla agregaría complejidad sin beneficio. Se usaría si hubiera reglas distintas por tipo de cliente. |
| Factory | `Reserva` se crea con un constructor simple que ya valida. Una fábrica no aportaría nada. |
| Singleton manual | Spring ya maneja una sola instancia de cada `@Service` y `@Repository`. |

## Refactorizaciones
| # | Antes | Después | Deuda técnica reducida |
|---|---|---|---|
| 1 | `buscar` lanzaba `IllegalArgumentException`, la misma excepción que "Id obligatorio" | `ReservaNoEncontradaException` propia | Se puede distinguir "no existe" (404) de "dato inválido" (400). |
| 2 | Id inexistente → 500 Internal Server Error | `@RestControllerAdvice` → 404 con JSON | El cliente recibe un código correcto y un mensaje útil. |
| 3 | Un POST con id repetido reemplazaba la reserva sin avisar | El Service revisa duplicados → `ReservaDuplicadaException` → 409 | Se evita pérdida silenciosa de datos. La regla vive en el Service, no en el repositorio. |

## Evidencia antes / después
| Petición | Antes (release/candidato-final) | Después (final/ae7-proyecto-final) |
|---|---|---|
| GET `/api/reservas/R-999` | 500 sin mensaje | 404 `{"estado":404,"mensaje":"Reserva no encontrada: R-999"}` |
| POST `R-001` dos veces | 201 y 201 (se reemplaza) | 201 y 409 |
