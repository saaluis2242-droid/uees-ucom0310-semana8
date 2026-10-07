package edu.uees.reservas.service;

/**
 * Se lanza cuando se intenta crear una reserva con un id que ya existe.
 * Antes el repositorio simplemente reemplazaba la reserva anterior sin avisar.
 */
public class ReservaDuplicadaException extends RuntimeException {

    public ReservaDuplicadaException(String id) {
        super("Ya existe una reserva con id: " + id);
    }
}
