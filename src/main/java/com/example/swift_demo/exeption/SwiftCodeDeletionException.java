package com.example.swift_demo.exeption;

public class SwiftCodeDeletionException extends RuntimeException {
    public SwiftCodeDeletionException(String swiftCode) {
        super("Cannot delete SWIFT code; not found: " + swiftCode);
    }
}
