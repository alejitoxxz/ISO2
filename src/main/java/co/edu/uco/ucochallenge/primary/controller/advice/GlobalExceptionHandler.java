package co.edu.uco.ucochallenge.primary.controller.advice;

import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
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
import co.edu.uco.ucochallenge.crosscuting.exception.BusinessException;
import co.edu.uco.ucochallenge.crosscuting.exception.DomainValidationException;
import co.edu.uco.ucochallenge.crosscuting.exception.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
        private static final String GENERIC_ERROR_MESSAGE = "An unexpected error has occurred";

        @ExceptionHandler(DomainValidationException.class)
        public ResponseEntity<ApiErrorResponse> handleDomainValidation(final DomainValidationException exception,
                        final HttpServletRequest request) {
                log.warn("DomainValidationException on request {}", request.getRequestURI(), exception);
                final var response = ApiErrorResponse.businessError(exception.getCode());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiErrorResponse> handleInvalidArguments(final MethodArgumentNotValidException exception,
                        final HttpServletRequest request) {
                final String message = Optional.ofNullable(exception.getBindingResult().getFieldError())
                                .map(FieldError::getDefaultMessage)
                                .orElse(GENERIC_ERROR_MESSAGE);
                log.warn("MethodArgumentNotValidException on request {}", request.getRequestURI(), exception);
                final var response = ApiErrorResponse.businessError(message);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
        public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
                        final jakarta.validation.ConstraintViolationException exception,
                        final HttpServletRequest request) {
                final String message = exception.getConstraintViolations().stream()
                                .map(violation -> Optional.ofNullable(violation.getMessage()).orElse(GENERIC_ERROR_MESSAGE))
                                .findFirst()
                                .orElse(GENERIC_ERROR_MESSAGE);
                log.warn("ConstraintViolationException on request {}", request.getRequestURI(), exception);
                final var response = ApiErrorResponse.businessError(message);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ApiErrorResponse> handleBusinessException(final BusinessException exception,
                        final HttpServletRequest request) {
                log.warn("BusinessException on request {}", request.getRequestURI(), exception);
                final var response = ApiErrorResponse.businessError(exception.getCode());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<ApiErrorResponse> handleNotFoundException(final NotFoundException exception,
                        final HttpServletRequest request) {
                log.warn("NotFoundException on request {}", request.getRequestURI(), exception);
                final var response = ApiErrorResponse.businessError(exception.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ApiErrorResponse> handleDataIntegrity(final DataIntegrityViolationException exception) {
                log.error("DataIntegrityViolationException on user registration", exception);
                final var response = ApiErrorResponse.businessError("Data integrity violation (duplicate or invalid value)");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ApiErrorResponse> handleConstraint(final ConstraintViolationException exception) {
                log.error("ConstraintViolationException on user registration", exception);
                final var response = ApiErrorResponse.businessError("Invalid data or foreign key constraint");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ApiErrorResponse> handleEntityNotFound(final EntityNotFoundException exception) {
                log.error("EntityNotFoundException on user registration", exception);
                final var response = ApiErrorResponse.businessError("Related catalog entity not found");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiErrorResponse> handleGenericException(final Exception exception,
                        final HttpServletRequest request) {
                log.error("Unexpected error on request {}", request.getRequestURI(), exception);
                final var response = ApiErrorResponse.unexpectedError(GENERIC_ERROR_MESSAGE);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
}
