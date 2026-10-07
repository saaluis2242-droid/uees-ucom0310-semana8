# Guía breve de defensa

1. **Problema y objetivo (1–2 min):** API de reservas con reglas de negocio claras, probada y trazable.
2. **Arquitectura (2–3 min):** Controller → Service → Domain → Repository. Controller sin lógica.
3. **Patrones y refactorización (2 min):** Repository, DTO, inyección de dependencias, manejador global de errores. Refactorización 500 → 404 y duplicados → 409.
4. **Demostración (3–4 min):** salud, puede-cancelar, POST 201, POST repetido 409, GET 200, GET inexistente 404.
5. **Pruebas y cobertura (2 min):** 28 pruebas en 5 clases. JaCoCo: el hallazgo del repositorio en 0 %.
6. **Git y trazabilidad (1 min):** ramas, commits, PR hacia main, etiqueta v1.0.0.
7. **Limitaciones y cierre (1–2 min):** datos en memoria, tipo sin validar, sin endpoints de confirmar/cancelar.
