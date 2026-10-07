package edu.uees.reservas.service;

/**
 * Se lanza cuando se busca una reserva que no existe.
 * Antes se usaba IllegalArgumentException, que también se usa para datos inválidos
 * ("Id obligatorio"), y así no se podía distinguir un 404 de un 400.
 */
public class ReservaNoEncontradaException extends RuntimeException {

    public ReservaNoEncontradaException(String id) {
        super("Reserva no encontrada: " + id);
    }
}
