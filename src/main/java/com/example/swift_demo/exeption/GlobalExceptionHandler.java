package com.example.swift_demo.exeption;

import com.example.swift_demo.mastruct.dtos.MessageResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SwiftCodeNotFoundException.class)
    public ResponseEntity<MessageResponseDTO> handleSwiftCodeNotFoundException(SwiftCodeNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO("SWIFT code not found: " + ex.getMessage()));
    }

    @ExceptionHandler(CountryNotFoundException.class)
    public ResponseEntity<MessageResponseDTO> handleCountryNotFoundException(CountryNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO("Country not found: " + ex.getMessage()));
    }

    @ExceptionHandler(DuplicateSwiftCodeException.class)
    public ResponseEntity<MessageResponseDTO> handleDuplicateSwiftCodeException(DuplicateSwiftCodeException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new MessageResponseDTO("SWIFT code already exists: " + ex.getMessage()));
    }

    @ExceptionHandler(SwiftCodeDeletionException.class)
    public ResponseEntity<MessageResponseDTO> handleSwiftCodeDeletionException(SwiftCodeDeletionException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO("Cannot delete SWIFT code: " + ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponseDTO("Validation error: " + errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponseDTO> handleGeneralException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponseDTO("An unexpected error occurred: " + ex.getMessage()));
    }
}