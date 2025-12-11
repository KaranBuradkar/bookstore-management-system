package com.karanburadkar.bookstore.exceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import com.karanburadkar.bookstore.dto.ApiResponse;
import com.karanburadkar.bookstore.exception.authentication.AuthException;
import com.karanburadkar.bookstore.exception.business.BusinessException;
import com.karanburadkar.bookstore.exception.database.EntityNotFoundException;
import com.karanburadkar.bookstore.exception.security.CustomAccessDeniedException;
import com.karanburadkar.bookstore.exception.validation.NullArgumentException;
import com.karanburadkar.bookstore.exception.database.DataIntegrityViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final HttpServletRequest request;

    public GlobalExceptionHandler(HttpServletRequest request) {
        this.request = request;
    }

    private ResponseEntity<ApiResponse<Void>> buildResponse(HttpStatus status, String message, Exception e) {
        return ResponseEntity.status(status).body(new ApiResponse<>(false, message, request.getRequestURI()));
    }

    private ResponseEntity<ApiResponse<Object>> buildResponse(HttpStatus status, String message, Object data, Exception e) {
        return ResponseEntity.status(status).body(new ApiResponse<>(false, message, data, request.getRequestURI()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEntityNotFoundException(EntityNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
    }

    @ExceptionHandler(CustomAccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomAccessDeniedException(CustomAccessDeniedException exception) {
        log.error(exception.getMessage(), exception);
        return buildResponse(HttpStatus.FORBIDDEN, exception.getMessage(), exception);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException exception) {
        log.error(exception.getMessage());
        return buildResponse(HttpStatus.FORBIDDEN, "Access Denied", exception);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.error(exception.getMessage(), exception);
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
    }

    @ExceptionHandler(NullArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleNullArgumentException(NullArgumentException exception) {
        log.error(exception.getMessage(), exception);
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException exception) {
        log.error(exception.getMessage(), exception);
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), exception.getData(), exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        log.error(exception.getMessage(), exception);

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                false, "validation error",
                errors, request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request
    ) {
        String message = String.format("Request method '%s' is not supported for this endpoint. Supported methods: %s",
                ex.getMethod(),
                ex.getSupportedHttpMethods());

        log.warn(message);
        log.warn(ex.getMessage());

        ApiResponse<Void> response = new ApiResponse<>(false, message, null, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidDataAccess(
            InvalidDataAccessApiUsageException ex,
            HttpServletRequest request
    ) {
        log.error(ex.getMessage(), ex);
        ApiResponse<Void> response = new ApiResponse<>(
                false, "Internal database error occurred.",
                null, request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleSQLIntegrityConstraintViolationException(
          SQLIntegrityConstraintViolationException exception
    ) {
        log.error(exception.getMessage(), exception);
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthException(AuthException ex, HttpServletRequest request) {
        log.error("Authentication error", ex);
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex); // 409 Conflict
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<Void>> expiredJwtException(ExpiredJwtException exception) {
        log.error(exception.getMessage(), exception);
        return buildResponse(HttpStatus.NOT_FOUND,
                "Your jwt token is expired please refresh token.",
                exception
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex
    ) {
        // Log raw DB error for debugging (safe)
        log.error("Data integrity violation: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(
                        false, "A data integrity violation occurred.",
                        ex.getData(), request.getRequestURI())
                );
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ApiResponse<Void>> handleServletException(ServletException e) {
        log.error(e.getMessage(), e);
        String message = getServletMessage(e);
        return buildResponse(HttpStatus.BAD_REQUEST, message, e);
    }

    private String getServletMessage(ServletException e) {
        if (e instanceof MissingServletRequestParameterException) {
            return e.getMessage();
        }
        if (e instanceof NoResourceFoundException) {
            return "wrong api requested";
        }
        return "Servlet exception";
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        String message = "Something unexpected internal happened at runtime";
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception exception) {
        log.error(exception.getMessage(), exception);
        String message = "Something unexpected internal happened";
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, exception);
    }
}
