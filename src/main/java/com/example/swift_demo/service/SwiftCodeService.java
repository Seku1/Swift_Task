package com.example.swift_demo.service;

import com.example.swift_demo.mastruct.dtos.CountrySwiftCodesResponseDTO;
import com.example.swift_demo.mastruct.dtos.SwiftCodeRequestDTO;
import com.example.swift_demo.mastruct.dtos.SwiftCodeResponseDTO;
import com.example.swift_demo.model.Country;
import com.example.swift_demo.model.SwiftCode;

public interface SwiftCodeService {
    SwiftCodeResponseDTO getBySwiftCode(String swiftCode);

    CountrySwiftCodesResponseDTO getByCountry(String countryISO2);

    void addSwiftCode(SwiftCodeRequestDTO dto);

    void deleteBySwiftCode(String swiftCode);
}
