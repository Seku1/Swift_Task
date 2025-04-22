package com.example.swift_demo.mastruct.mappers;

import com.example.swift_demo.mastruct.dtos.CountrySwiftCodesResponseDTO;
import com.example.swift_demo.mastruct.dtos.SwiftCodeRequestDTO;
import com.example.swift_demo.mastruct.dtos.SwiftCodeResponseDTO;
import com.example.swift_demo.model.Country;
import com.example.swift_demo.model.SwiftCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SwiftCodeMapper {
    @Mappings({
            @Mapping(source = "swiftCode.swiftCode", target = "swiftCode"),
            @Mapping(source = "swiftCode.bank.name", target = "bankName"),
            @Mapping(source = "swiftCode.address", target = "address"),
            @Mapping(source = "swiftCode.country.iso2", target = "countryISO2"),
            @Mapping(source = "swiftCode.country.name", target = "countryName"),
            @Mapping(source = "swiftCode.isHeadquarter", target = "isHeadquarter"),
            @Mapping(target = "branches", ignore = true)
    })
    SwiftCodeResponseDTO swiftCodeToDto(SwiftCode swiftCode);

    List<SwiftCodeResponseDTO> swiftCodesToDTOs(List<SwiftCode> swiftCodes);

    @Mappings({
            @Mapping(source = "swiftCode", target = "swiftCode"),
            @Mapping(source = "address", target = "address"),
            @Mapping(source = "isHeadquarter", target = "isHeadquarter")
    })
    SwiftCode dtoToSwiftCode(SwiftCodeRequestDTO dto);

    @Mappings({
            @Mapping(source = "country.iso2", target = "countryISO2"),
            @Mapping(source = "country.name", target = "countryName"),
            @Mapping(source = "swiftCodes", target = "swiftCodes")
    })
    CountrySwiftCodesResponseDTO toCountrySwiftCodesResponseDTO(Country country, List<SwiftCodeResponseDTO> swiftCodes);
}