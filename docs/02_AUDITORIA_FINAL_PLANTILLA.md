# Auditoría final

Estudiante: Luis Saá Garzón · Rama: `release/candidato-final`

## Funcional
- [x] Aplicación inicia (`mvn spring-boot:run`, puerto 8080)
- [x] Endpoints principales responden (salud, puede-cancelar, POST, GET por id)
- [x] Errores conocidos documentados (README, sección "Errores conocidos")
- [x] Demostración repetible (comandos `curl.exe` en el README)

## Diseño
- [x] Controller delgado: solo traduce HTTP y delega
- [x] Service claro: regla de cancelación, crear, buscar, confirmar
- [x] Dominio coherente: `Reserva` valida su id y controla su estado

## Pruebas
- [x] mvn clean test exitoso (25 pruebas, 0 fallos)
- [x] Casos normales, límites (0, 1, 2, 3 horas), inválidos y excepciones
- [x] Pruebas de Service y de Controller
- [x] JaCoCo revisado

## Hallazgo JaCoCo y mejora aplicada
| Elemento | Hallazgo | Riesgo | Mejora |
|---|---|---|---|
| `ReservaRepositoryMemoria` | 0 % (0/5 líneas): todas las pruebas usaban un mock del repositorio | El componente que guarda los datos cuando la app corre nunca se probaba | `ReservaRepositoryMemoriaTest` (3 pruebas) → 100 % |
| `Reserva` (constructor) | 4/6 ramas: id nulo y tipo nulo sin probar | Un cambio en esas validaciones no se detectaría | `ReservaTest` (id nulo, tipo nulo) → 6/6 ramas |
| `Reserva.cancelar()` | 0/2 líneas | Cambio de estado sin verificar | `ReservaTest` (cancelar) → 2/2 |

## Git
- [x] Sin `target/` en el repositorio (`.gitignore`)
- [x] Sin secretos (no hay `application.properties` ni credenciales)
- [x] Commits descriptivos (`test:`, `docs:`, `release:`)
- [x] README actualizado
- [x] Rama final identificable: `release/candidato-final`

## Limitaciones
1. Id inexistente responde 500 en lugar de 404. Mejora: excepción propia + `@RestControllerAdvice`.
2. Un POST con id repetido reemplaza la reserva (no hay 409 Conflict).
3. El tipo de reserva no se valida contra NORMAL, VIP o ESTUDIANTE.
4. Repositorio en memoria: los datos no persisten.
5. No hay prueba de integración que levante toda la aplicación (`@SpringBootTest`).
