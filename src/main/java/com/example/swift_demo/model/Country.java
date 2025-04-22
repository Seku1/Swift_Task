package com.example.swift_demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {
    @Id
    @Column(length = 2)
    private String iso2;

    private String name;

    @OneToMany(mappedBy = "country")
    private List<SwiftCode> swiftCodes;
}
