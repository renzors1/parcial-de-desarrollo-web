package com.mycompany.myapp.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando se intenta crear un recurso duplicado (ISBN, comprobante, email, etc.).
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public DuplicateResourceException(String mensaje) {
        super(mensaje);
    }
}
