package io.hanyoung.gulmatebackend.config.web;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<?> handleConflict(RuntimeException ex) {
        return ResponseEntity.badRequest()
                .body(ex.getMessage());
    }
}
