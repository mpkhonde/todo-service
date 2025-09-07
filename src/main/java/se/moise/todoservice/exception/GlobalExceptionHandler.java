package se.moise.todoservice.exception;

import org.springframework.http.*;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

/**
 * Enkla, tydliga fel i JSON.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---------------------------------------------------
    // 400 – Valideringsfel (@Valid)
    // ---------------------------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        var err = ex.getBindingResult().getFieldErrors().stream().findFirst();
        String msg = err.map(e -> e.getField() + " " + e.getDefaultMessage())
                .orElse("Validation error");
        return ResponseEntity.badRequest().body(Map.of(
                "error", "Bad Request",
                "message", msg
        ));
    }

    // ---------------------------------------------------
    // 400 – Saknad query-parameter (?ids=...)
    // ---------------------------------------------------
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParam(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", "Bad Request",
                "message", "Missing request parameter: " + ex.getParameterName()
        ));
    }

    // ---------------------------------------------------
    // 400 – Typfel (t.ex. ids=abc ger typmismatch)
    // ---------------------------------------------------
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String name = ex.getName();
        String required = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "required type";
        return ResponseEntity.badRequest().body(Map.of(
                "error", "Bad Request",
                "message", "Invalid value for parameter '" + name + "' (expected " + required + ")"
        ));
    }

    // ---------------------------------------------------
    // 405 – Fel HTTP-metod (t.ex. DELETE saknas för en route)
    // ---------------------------------------------------
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(Map.of(
                "error", "Method Not Allowed",
                "message", ex.getMessage()
        ));
    }

    // ---------------------------------------------------
    // 500 – Övriga fel
    // ---------------------------------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Internal Server Error",
                "message", ex.getMessage()
        ));
    }
}
