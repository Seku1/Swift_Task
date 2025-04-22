package com.example.swift_demo.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateSwiftCodeException extends RuntimeException {
    public DuplicateSwiftCodeException(String swiftCode) {
        super("SWIFT code already exists: " + swiftCode);
    }
}
