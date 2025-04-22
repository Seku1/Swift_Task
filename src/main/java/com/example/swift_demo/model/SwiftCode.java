package com.example.swift_demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class SwiftCode {
    @Id
    private String swiftCode;

    private String address;

    private boolean isHeadquarter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_iso2", nullable = false)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "headquarter_id")
    private SwiftCode headquarter;

    @OneToMany
    private List<SwiftCode> branches =  new ArrayList<>();
}
