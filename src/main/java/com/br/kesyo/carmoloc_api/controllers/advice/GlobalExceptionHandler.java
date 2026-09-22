package com.br.kesyo.carmoloc_api.controllers.advice;

import com.br.kesyo.carmoloc_api.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(DuplicateDocumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflict(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", details);
    }

    @ExceptionHandler({EquipmentNotFoundException.class, EquipmentUnitNotFoundException.class})
    public ResponseEntity<ErrorResponseDTO> handleEquipmentNotFound(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler({InvalidPricingConfigurationException.class, InvalidAssetCodeListException.class})
    public ResponseEntity<ErrorResponseDTO> handleEquipmentBadRequest(RuntimeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(RentalOrderNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleRentalOrderNotFound(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(InsufficientAvailabilityException.class)
    public ResponseEntity<ErrorResponseDTO> handleInsufficientAvailability(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler({InvalidOrderStatusTransitionException.class, InvalidRentalPeriodException.class})
    public ResponseEntity<ErrorResponseDTO> handleRentalOrderBadRequest(RuntimeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(NotEnoughEquipmentUnitsException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotEnoughEquipmentUnits(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(EquipmentNotAvailableException.class)
    public ResponseEntity<ErrorResponseDTO> handleEquipmentNotAvailable(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(EquipmentHasActiveRentalOrdersException.class)
    public ResponseEntity<ErrorResponseDTO> handleEquipmentHasActiveRentalOrders(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationException(Exception ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Usuário e/ou senha inválidos", null);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidRefreshToken(RuntimeException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFound(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler({DuplicateUsernameException.class, DuplicateEmailException.class})
    public ResponseEntity<ErrorResponseDTO> handleUserConflict(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse(HttpStatus status, String message, List<String> details) {
        ErrorResponseDTO body = ErrorResponseDTO.builder()
            .timestamp(Instant.now())
            .status(status.value())
            .error(status.getReasonPhrase())
            .message(message)
            .details(details)
            .build();
        return ResponseEntity.status(status).body(body);
    }
}
