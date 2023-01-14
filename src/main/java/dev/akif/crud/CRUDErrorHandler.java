package dev.akif.crud;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public interface CRUDErrorHandler {
    @ExceptionHandler(CRUDErrorException.class)
    default ResponseEntity<CRUDError> handleCRUDError(final CRUDErrorException e) {
        return e.getError().toResponseEntity();
    }
}
