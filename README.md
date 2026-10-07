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

## Cobertura
Después de `mvn clean test`, abrir:
`target/site/jacoco/index.html`
