package co.edu.uco.ucochallenge.primary.controller.advice;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import co.edu.uco.ucochallenge.application.ApiErrorResponse;
import co.edu.uco.ucochallenge.crosscutting.dto.MessageDTO;
import co.edu.uco.ucochallenge.crosscuting.exception.BusinessException;
import co.edu.uco.ucochallenge.crosscuting.exception.NotFoundException;
import co.edu.uco.ucochallenge.secondary.ports.restclient.MessageServicePort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String GENERIC_ERROR_MESSAGE = "An unexpected error has occurred";
    private final MessageServicePort messageServicePort;

    public GlobalExceptionHandler(MessageServicePort messageServicePort) {
        this.messageServicePort = messageServicePort;
    }

    private String resolveCatalogMessage(String code) {
        if (code == null || code.isBlank()) {
            return code;
        }
        try {
            return messageServicePort.getMessage(code)
                    .map(MessageDTO::getUserMessage)
                    .blockOptional()
                    .orElse(code);
        } catch (Exception exception) {
            log.warn("Unable to resolve message for code {}", code, exception);
            return code;
        }
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(final BusinessException ex) {
        String message = resolveCatalogMessage(ex.getMessage());
        return new ResponseEntity<>(ApiErrorResponse.businessError(message), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(final NotFoundException ex) {
        String message = resolveCatalogMessage(ex.getMessage());
        return new ResponseEntity<>(ApiErrorResponse.businessError(message), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleJakartaConstraint(final ConstraintViolationException ex) {
        final String message = ex.getConstraintViolations().stream()
                .map(v -> Optional.ofNullable(v.getMessage()).orElse(GENERIC_ERROR_MESSAGE))
                .findFirst().orElse(GENERIC_ERROR_MESSAGE);
        return new ResponseEntity<>(ApiErrorResponse.businessError(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex) {
        final String message = Optional.ofNullable(ex.getBindingResult().getFieldError())
                .map(FieldError::getDefaultMessage)
                .orElse(GENERIC_ERROR_MESSAGE);
        return new ResponseEntity<>(ApiErrorResponse.businessError(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(final DataIntegrityViolationException ex) {
        log.error("Data integrity violation", ex);
        return new ResponseEntity<>(
                ApiErrorResponse.businessError("Data integrity violation (duplicate or invalid value)"),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFound(final EntityNotFoundException ex) {
        return new ResponseEntity<>(ApiErrorResponse.businessError("Related catalog entity not found"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(final Exception ex) {
        log.error("Unexpected error", ex);
        return new ResponseEntity<>(ApiErrorResponse.unexpectedError(GENERIC_ERROR_MESSAGE),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
