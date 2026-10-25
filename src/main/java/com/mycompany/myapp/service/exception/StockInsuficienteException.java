package com.mycompany.myapp.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando no hay licencias digitales disponibles o cupos en una promoción.
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class StockInsuficienteException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
