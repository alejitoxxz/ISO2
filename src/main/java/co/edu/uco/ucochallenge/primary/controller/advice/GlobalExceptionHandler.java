package co.edu.uco.ucochallenge.primary.controller.advice;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.edu.uco.ucochallenge.crosscuting.exception.BusinessException;
import co.edu.uco.ucochallenge.crosscuting.exception.DomainValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
        private static final String DEFAULT_CODE = "exception.general.unexpected";

        @ExceptionHandler(DomainValidationException.class)
        public ResponseEntity<ErrorResponse> handleDomainValidation(final DomainValidationException exception,
                        final HttpServletRequest request) {
                final var response = new ErrorResponse(exception.getCode(), HttpStatus.BAD_REQUEST.value());
                logWarning(exception.getCode(), exception, request);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleInvalidArguments(final MethodArgumentNotValidException exception,
                        final HttpServletRequest request) {
                final String code = Optional.ofNullable(exception.getBindingResult().getFieldError())
                                .map(FieldError::getDefaultMessage)
                                .orElse(DEFAULT_CODE);
                final var response = new ErrorResponse(code, HttpStatus.BAD_REQUEST.value());
                logWarning(code, exception, request);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ErrorResponse> handleConstraintViolation(final ConstraintViolationException exception,
                        final HttpServletRequest request) {
                final String code = exception.getConstraintViolations().stream()
                                .map(violation -> Optional.ofNullable(violation.getMessage()).orElse(DEFAULT_CODE))
                                .findFirst()
                                .orElse(DEFAULT_CODE);
                final var response = new ErrorResponse(code, HttpStatus.BAD_REQUEST.value());
                logWarning(code, exception, request);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException exception,
                        final HttpServletRequest request) {
                final var response = new ErrorResponse(exception.getCode(), HttpStatus.CONFLICT.value());
                logWarning(exception.getCode(), exception, request);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ErrorResponse> handleDataIntegrity(final DataIntegrityViolationException exception,
                        final HttpServletRequest request) {
                final var response = new ErrorResponse("register.user.email.duplicated", HttpStatus.CONFLICT.value());
                logWarning(response.code(), exception, request);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(final Exception exception,
                        final HttpServletRequest request) {
                final var response = new ErrorResponse(DEFAULT_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value());
                logError(response.code(), exception, request);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        private void logWarning(final String code, final Exception exception, final HttpServletRequest request) {
                LOGGER.warn("requestId={}, path={}, code={}, exception={}",
                                MDC.get("requestId"),
                                request.getRequestURI(),
                                code,
                                exception.getClass().getSimpleName());
        }

        private void logError(final String code, final Exception exception, final HttpServletRequest request) {
                LOGGER.error("requestId={}, path={}, code={}, exception={}",
                                MDC.get("requestId"),
                                request.getRequestURI(),
                                code,
                                exception.getClass().getSimpleName(),
                                exception);
        }

        public record ErrorResponse(String code, int status) {
        }
}
