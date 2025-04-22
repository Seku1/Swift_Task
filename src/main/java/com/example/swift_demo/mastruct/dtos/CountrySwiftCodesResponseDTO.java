package com.example.swift_demo.mastruct.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountrySwiftCodesResponseDTO {

    private String countryISO2;
    private String countryName;
    private List<SwiftCodeResponseDTO> swiftCodes;
}