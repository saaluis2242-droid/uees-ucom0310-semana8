# feat: entrega final Ae7 – API de reservas con manejo de errores HTTP

## Objetivo
Entregar la versión final de la API de reservas: integrada con Spring Boot, con pruebas, cobertura revisada y un manejo de errores HTTP correcto.

## Cambios
- Integración de la API (GET de regla de negocio, POST con DTO y validación, GET por id).
- Pruebas de Service, Controller, repositorio real, dominio e integración (28 pruebas).
- Refactorización: `ReservaNoEncontradaException` en lugar de `IllegalArgumentException`.
- `@RestControllerAdvice`: id inexistente responde 404 (antes 500).
- Regla nueva en el Service: id repetido responde 409 (antes se reemplazaba la reserva).
- README, auditoría, decisiones de diseño y guía de defensa.

## Cómo verificar
```bash
git switch final/ae7-proyecto-final
mvn clean test
mvn spring-boot:run
```
Esperado: 28 pruebas en verde. Luego seguir la sección "Probar la API desde Windows" del README (201, 409, 200, 404).

## Cobertura
Reporte en `target/site/jacoco/index.html`. Todas las clases quedan cubiertas excepto `ReservasApplication.main`. En la auditoría (Actividad 3) se detectó que `ReservaRepositoryMemoria` estaba en 0 % porque todas las pruebas usaban un mock; se agregaron pruebas del repositorio real y una prueba de integración sin mocks.

## Limitaciones
- Datos en memoria.
- `tipo` sin validar contra los valores permitidos.
- No hay endpoints para confirmar o cancelar.

## Uso de IA
Herramienta: Claude (Anthropic).
Propósito: apoyo para proponer casos de prueba, generar borradores de código (pruebas, excepciones y manejador de errores) y de la documentación, y preparar la guía de defensa.
Declaración: el código y las decisiones fueron revisados, ejecutados y comprendidos por el autor. Las pruebas se ejecutaron con `mvn clean test`, la API se probó manualmente con `curl.exe` y el reporte de JaCoCo fue revisado antes de la entrega.
