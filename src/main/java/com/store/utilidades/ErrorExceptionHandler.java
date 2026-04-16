package com.store.utilidades;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ErrorExceptionHandler {

    public static Logger logger = LoggerFactory.getLogger(ErrorExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrity(DataIntegrityViolationException ex) {
    	Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
    	Map<String, String> error = new HashMap<>();
    	HttpStatus status = HttpStatus.CONFLICT;
        if (ex instanceof DataIntegrityViolationException) {        	

            if (cause instanceof org.hibernate.exception.ConstraintViolationException cve) {
                String constraintName = cve.getConstraintName();
                error.put("codigo", "10001");
                error.put("error", "Ya existe un registro con ese valor.- " + constraintName + " - Por favor usa otro.");
            } else {
                error.put("codigo", "10002");
                error.put("error", "Violación de integridad: ");
            }
        }
        return ResponseEntity.status(status).body(error);
    } 
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFound(EntityNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("codigo", "10003");
        error.put("error", "El recurso solicitado no existe");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        HttpStatus status;
       
        if (ex instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException msrpe = (MissingServletRequestParameterException) ex;
            status = HttpStatus.BAD_REQUEST;
            error.put("codigo", "10005");
            error.put("error", "El parametro '" + msrpe.getParameterName() + "' es obligatorio y no fue enviado");
        } else if (ex.getCause() instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException msrpe =
                    (MissingServletRequestParameterException) ex.getCause();
            status = HttpStatus.BAD_REQUEST;
            error.put("codigo", "10005");
            error.put("error", "El parametro '" + msrpe.getParameterName() + "' es obligatorio y no fue enviado");

        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            error.put("codigo", "10004");
            error.put("error", "Ha ocurrido un error inesperado");
        }

        return ResponseEntity.status(status).body(error);
    }


    
    /*
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;

        HttpStatus status;

        if (ex instanceof DataIntegrityViolationException) {
            status = HttpStatus.CONFLICT;

            if (cause instanceof org.hibernate.exception.ConstraintViolationException cve) {
                String constraintName = cve.getConstraintName();
                error.put("codigo", "10001");
                error.put("error", "Ya existe un registro con ese valor.- " + constraintName + " - Por favor usa otro.");
            } else {
                error.put("codigo", "10002");
                error.put("error", "Violación de integridad: ");
            }

        } else if (ex instanceof EntityNotFoundException) {
            status = HttpStatus.NOT_FOUND;
            error.put("codigo", "10003");
            error.put("error", "El recurso solicitado no existe");

        } else if (ex instanceof MissingServletRequestParameterException msrpe) {
            status = HttpStatus.BAD_REQUEST;
            String paramName = msrpe.getParameterName();
            error.put("codigo", "10005");
            error.put("error", "El parámetro '" + paramName + "' es obligatorio y no fue enviado");

        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            error.put("codigo", "10004");
            error.put("error", "Ha ocurrido un error inesperado");
        }
	
        logger.error("Excepción capturada: ", ex);
        return ResponseEntity.status(status).body(error);

    }*/

}