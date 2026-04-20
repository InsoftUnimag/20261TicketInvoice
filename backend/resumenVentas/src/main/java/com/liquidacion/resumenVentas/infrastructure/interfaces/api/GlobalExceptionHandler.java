package com.liquidacion.resumenVentas.infrastructure.interfaces.api;

import com.liquidacion.resumenVentas.application.dto.ErrorResponse;
import com.liquidacion.resumenVentas.shared.errors.BusinessException;
import com.liquidacion.resumenVentas.shared.errors.ErrorCode;
import com.liquidacion.resumenVentas.shared.errors.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        log.warn("Error de negocio: {} - {}", ex.getErrorCode().getCode(), ex.getMessage());
        
        HttpStatus status = switch (ex.getErrorCode()) {
            case EVENT_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case EVENT_NOT_CLOSED -> HttpStatus.CONFLICT;
            case INVALID_REQUEST -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.BAD_REQUEST;
        };
        
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode().getCode(), ex.getMessage());
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<ErrorResponse> handleTechnicalException(TechnicalException ex) {
        log.error("Error técnico: {} - {}", ex.getErrorCode().getCode(), ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode().getCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Error inesperado: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse("INTERNAL_ERROR", "Error interno del servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}