package com.example.swift_demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Country {
    @Id
    @Column(length = 2)
    private String iso2;

    private String name;

    @OneToMany(mappedBy = "country")
    private List<SwiftCode> swiftCodes;
}
