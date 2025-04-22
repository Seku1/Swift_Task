package com.example.swift_demo.mastruct.dtos;


import com.example.swift_demo.model.SwiftCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwiftCodeResponseDTO {
    private String swiftCode;
    private String bankName;
    private String address;
    private String countryISO2;
    private String countryName;
    private boolean isHeadquarter;
    private List<SwiftCodeResponseDTO> branches;
}