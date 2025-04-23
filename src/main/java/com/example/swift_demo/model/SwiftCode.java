package com.example.swift_demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwiftCode {
    @Id
    private String swiftCode;

    private String address;

    // Lombok generates isHeadquarter() getter for this field
    private boolean headquarter;  // Changed from isHeadquarter for consistency

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_iso2", nullable = false)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "headquarter_id")
    private SwiftCode headquarterSwiftCode;  // Renamed to avoid conflict

    @OneToMany
    private List<SwiftCode> branches = new ArrayList<>();
}