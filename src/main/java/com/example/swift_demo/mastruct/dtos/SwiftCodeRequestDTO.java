package com.example.swift_demo.mastruct.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwiftCodeRequestDTO {
    @NotBlank(message = "SWIFT code must not bee blank")
    private String swiftCode;

    @NotBlank(message = "Bank name must not be blank")
    private String bankName;

    @NotBlank(message = "Address must not be blank")
    private String address;

    @NotBlank(message = "Country ISO2 must not be blank")
    @Pattern(regexp = "[A-za-z]{2}", message = "Country ISO2 must be two letters")
    private String countryISO2;

    @NotBlank(message = "Country name must not be blank")
    private String countryName;

    private String countryCode;

    private boolean isHeadquarter;
}
