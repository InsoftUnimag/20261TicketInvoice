package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ErrorResponse;
import com.ticketevents.liquidation.shared.errors.BusinessException;
import com.ticketevents.liquidation.shared.errors.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
        HttpStatus status = ex.getErrorCode() == ErrorCode.VENUE_NOT_FOUND ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(new ErrorResponse(ex.getErrorCode(), ex.getMessage(), Instant.now()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ConstraintViolationException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(ErrorCode.VALIDATION_ERROR, ex.getMessage(), Instant.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ErrorCode.INTERNAL_ERROR, "Error interno del servidor", Instant.now()));
    }
}
