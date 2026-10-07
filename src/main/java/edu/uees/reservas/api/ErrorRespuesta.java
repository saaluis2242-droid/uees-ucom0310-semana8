package edu.uees.reservas.api;

/**
 * Cuerpo JSON que devuelve la API cuando ocurre un error de negocio.
 */
public record ErrorRespuesta(int estado, String mensaje) {}
