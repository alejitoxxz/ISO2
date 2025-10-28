package co.edu.uco.ucochallenge.primary.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.edu.uco.ucochallenge.application.ApiErrorResponse;
import co.edu.uco.ucochallenge.crosscuting.exception.BusinessException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final String GENERIC_ERROR_MESSAGE = "An unexpected error has occurred";

        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ApiErrorResponse> handleBusinessException(final BusinessException exception) {
                final var response = ApiErrorResponse.businessError(exception.getMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiErrorResponse> handleGenericException(final Exception exception) {
                final var response = ApiErrorResponse.unexpectedError(GENERIC_ERROR_MESSAGE);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
}
