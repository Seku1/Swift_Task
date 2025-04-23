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

    SwiftCodeResponseDTO swiftCodeToDto(SwiftCode swiftCode);

    List<SwiftCodeResponseDTO> swiftCodesToDTOs(List<SwiftCode> swiftCodes);


    SwiftCode dtoToSwiftCode(SwiftCodeRequestDTO dto);


    CountrySwiftCodesResponseDTO toCountrySwiftCodesResponseDTO(Country country, List<SwiftCodeResponseDTO> swiftCodes);
}