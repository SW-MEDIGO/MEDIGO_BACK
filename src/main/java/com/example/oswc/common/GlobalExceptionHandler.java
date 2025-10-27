package com.example.oswc.common;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> body(HttpStatus status, String code, String message) {
        return ResponseEntity.status(status).body(Map.of(
                "status", status.value(),
                "code", code,
                "message", message == null || message.isBlank() ? code : message
        ));
    }

    // ---------- 400 Bad Request 계열 ----------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getAllErrors().stream()
                .findFirst().map(x -> x.getDefaultMessage()).orElse("BAD_REQUEST");
        return body(HttpStatus.BAD_REQUEST, "BAD_REQUEST", msg);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraint(ConstraintViolationException e) {
        return body(HttpStatus.BAD_REQUEST, "BAD_REQUEST", e.getMessage());
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            HttpMediaTypeNotSupportedException.class
    })
    public ResponseEntity<Map<String, Object>> handleReadable(Exception e) {
        return body(HttpStatus.BAD_REQUEST, "BAD_REQUEST", e.getMessage());
    }

    // ---------- 비즈니스/커스텀 코드 매핑 ----------
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArg(IllegalArgumentException e) {
        String code = e.getMessage() == null ? "ILLEGAL_ARGUMENT" : e.getMessage();

        // 필요한 코드만 선별적으로 상태 결정
        return switch (code) {
            case "INVALID_CREDENTIALS" -> body(HttpStatus.UNAUTHORIZED, code, null);          // 401
            case "INVALID_REFRESH_TOKEN", "REUSED_REFRESH_TOKEN" -> body(HttpStatus.UNAUTHORIZED, code, null); // 401
            case "EMAIL_EXISTS", "USERNAME_EXISTS" -> body(HttpStatus.CONFLICT, code, null);   // 409
            case "USER_NOT_FOUND" -> body(HttpStatus.NOT_FOUND, code, null);                  // 404
            case "EMPTY_FILE", "UNSUPPORTED_FILE_TYPE", "FILE_TOO_LARGE" -> body(HttpStatus.BAD_REQUEST, code, null); // 400
            default -> body(HttpStatus.BAD_REQUEST, code, null);                               // 기본 400
        };
    }

    // ---------- 500 Internal Server Error ----------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleEtc(Exception e) {
        return body(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", e.getMessage());
    }
}
