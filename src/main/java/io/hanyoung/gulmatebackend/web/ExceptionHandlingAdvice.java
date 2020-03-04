package io.hanyoung.gulmatebackend.web;

import io.hanyoung.gulmatebackend.web.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlingAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> notFoundExceptionHandler(ResourceNotFoundException exception) {
        return ResponseEntity
                .notFound()
                .build();
    }

}
