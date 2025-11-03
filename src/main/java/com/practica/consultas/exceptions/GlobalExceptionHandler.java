package com.practica.consultas.exceptions;

import com.practica.consultas.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponse> handleReglaNegocioException(ReglaNegocioException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLockingFailureException(ObjectOptimisticLockingFailureException ex, WebRequest request) {
        String message = "Error de concurrencia: El registro fue modificado por otra transacción. Por favor, inténtelo de nuevo.";
        return buildErrorResponse(HttpStatus.CONFLICT, message, request);
    }

    @ExceptionHandler({AccessDeniedException.class, ForbiddenAccessException.class})
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(Exception ex, WebRequest request) {
        String message = "Acceso denegado. No tiene los permisos necesarios para realizar esta acción.";
        return buildErrorResponse(HttpStatus.FORBIDDEN, message, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ex.printStackTrace();

        String message = "Ocurrió un error inesperado en el servidor. Por favor, contacte al administrador.";
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, request);
    }
}
