package org.example.exception;

import org.example.config.InvalidApiKeyException;
import org.example.config.MissingApiKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return buildResponse(HttpStatus.BAD_REQUEST, "Error de validacion", errors);
    }

    @ExceptionHandler(MissingApiKeyException.class)
    public ResponseEntity<Map<String, Object>> handleMissingApiKey(MissingApiKeyException exception) {
        return buildResponse(HttpStatus.UNAUTHORIZED, exception.getMessage(), null);
    }

    @ExceptionHandler(InvalidApiKeyException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidApiKey(InvalidApiKeyException exception) {
        return buildResponse(HttpStatus.UNAUTHORIZED, exception.getMessage(), null);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProductNotFound(ProductNotFoundException exception) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), null);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCustomerNotFound(CustomerNotFoundException exception) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), null);
    }

    @ExceptionHandler(InvalidPriceRangeException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidPriceRange(InvalidPriceRangeException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), null);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingRequestParameter(MissingServletRequestParameterException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Falta el parametro requerido: " + exception.getParameterName(), null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Valor invalido para el parametro: " + exception.getName(), null);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, Object>> handleMissingHeader(MissingRequestHeaderException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Falta el header requerido: " + exception.getHeaderName(), null);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message, Object errors) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("message", message);

        if (errors != null) {
            body.put("errors", errors);
        }

        return ResponseEntity.status(status).body(body);
    }
}
