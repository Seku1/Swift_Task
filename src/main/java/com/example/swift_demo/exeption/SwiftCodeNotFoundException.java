package com.example.swift_demo.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SwiftCodeNotFoundException extends RuntimeException {
    public SwiftCodeNotFoundException(String swiftCode) {
        super("SWIFT code not found: " + swiftCode);
    }
}
