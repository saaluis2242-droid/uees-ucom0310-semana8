package edu.uees.reservas.api;

import edu.uees.reservas.service.ReservaNoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce las excepciones del negocio a códigos HTTP en un solo lugar,
 * para no llenar el Controller de try-catch.
 */
@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(ReservaNoEncontradaException.class)
    public ResponseEntity<ErrorRespuesta> reservaNoEncontrada(ReservaNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorRespuesta(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }
}
