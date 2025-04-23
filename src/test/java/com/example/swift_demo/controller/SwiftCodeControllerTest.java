package com.example.swift_demo.controller;


import com.example.swift_demo.exeption.DuplicateSwiftCodeException;
import com.example.swift_demo.exeption.SwiftCodeDeletionException;
import com.example.swift_demo.exeption.SwiftCodeNotFoundException;
import com.example.swift_demo.mastruct.dtos.CountrySwiftCodesResponseDTO;
import com.example.swift_demo.mastruct.dtos.SwiftCodeRequestDTO;
import com.example.swift_demo.mastruct.dtos.SwiftCodeResponseDTO;
import com.example.swift_demo.service.SwiftCodeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SwiftCodeController.class)
@Import(TestConfig.class)
class SwiftCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SwiftCodeService swiftCodeService;

    @Autowired
    private ObjectMapper objectMapper;

    private SwiftCodeResponseDTO headquarterDto;
    private SwiftCodeResponseDTO branchDto;
    private SwiftCodeRequestDTO requestDto;
    private CountrySwiftCodesResponseDTO countryDto;

    @BeforeEach
    void setUp() {
        reset(swiftCodeService);

        // Set up test data
        branchDto = new SwiftCodeResponseDTO();
        branchDto.setSwiftCode("ABCDUS33");
        branchDto.setBankName("TEST BANK");
        branchDto.setCountryISO2("US");
        branchDto.setCountryName("UNITED STATES");
        branchDto.setAddress("123 MAIN ST, NEW YORK");
        branchDto.setHeadquarter(false);

        headquarterDto = new SwiftCodeResponseDTO();
        headquarterDto.setSwiftCode("ABCDUSXXX");
        headquarterDto.setBankName("TEST BANK");
        headquarterDto.setCountryISO2("US");
        headquarterDto.setCountryName("UNITED STATES");
        headquarterDto.setAddress("456 WALL ST, NEW YORK");
        headquarterDto.setHeadquarter(true);
        headquarterDto.setBranches(List.of(branchDto));

        requestDto = new SwiftCodeRequestDTO();
        requestDto.setSwiftCode("ABCDUS33");
        requestDto.setBankName("TEST BANK");
        requestDto.setCountryISO2("US");
        requestDto.setCountryName("UNITED STATES");
        requestDto.setAddress("123 MAIN ST, NEW YORK");
        requestDto.setHeadquarter(false);

        countryDto = new CountrySwiftCodesResponseDTO();
        countryDto.setCountryISO2("US");
        countryDto.setCountryName("UNITED STATES");
        countryDto.setSwiftCodes(List.of(headquarterDto, branchDto));
    }

    @Test
    void getSwiftCodeDetails_HQ_ReturnsCorrectResponse() throws Exception {
        when(swiftCodeService.getBySwiftCode("ABCDUSXXX")).thenReturn(headquarterDto);

        mockMvc.perform(get("/v1/swift-codes/ABCDUSXXX"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.swiftCode", is("ABCDUSXXX")))
                .andExpect(jsonPath("$.bankName", is("TEST BANK")))
                .andExpect(jsonPath("$.headquarter", is(true)))  // Changed from "isHeadquarter" to "headquarter"
                .andExpect(jsonPath("$.branches", hasSize(1)))
                .andExpect(jsonPath("$.branches[0].swiftCode", is("ABCDUS33")));

        verify(swiftCodeService).getBySwiftCode("ABCDUSXXX");
    }

    @Test
    void getSwiftCodeDetails_Branch_ReturnsCorrectResponse() throws Exception {
        when(swiftCodeService.getBySwiftCode("ABCDUS33")).thenReturn(branchDto);

        mockMvc.perform(get("/v1/swift-codes/ABCDUS33"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.swiftCode", is("ABCDUS33")))
                .andExpect(jsonPath("$.bankName", is("TEST BANK")))
                .andExpect(jsonPath("$.headquarter", is(false)))  // Changed from "isHeadquarter" to "headquarter"
                .andExpect(jsonPath("$.branches").doesNotExist());

        verify(swiftCodeService).getBySwiftCode("ABCDUS33");
    }

    @Test
    void getSwiftCodeDetails_NotFound_Returns404() throws Exception {
        when(swiftCodeService.getBySwiftCode("NONEXISTENT")).thenThrow(new SwiftCodeNotFoundException("NONEXISTENT"));

        mockMvc.perform(get("/v1/swift-codes/NONEXISTENT"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("NONEXISTENT")));

        verify(swiftCodeService).getBySwiftCode("NONEXISTENT");
    }

    @Test
    void getSwiftCodesByCountry_ReturnsCorrectResponse() throws Exception {
        when(swiftCodeService.getByCountry("US")).thenReturn(countryDto);

        mockMvc.perform(get("/v1/swift-codes/country/US"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.countryISO2", is("US")))
                .andExpect(jsonPath("$.countryName", is("UNITED STATES")))
                .andExpect(jsonPath("$.swiftCodes", hasSize(2)))
                .andExpect(jsonPath("$.swiftCodes[0].swiftCode", is("ABCDUSXXX")))
                .andExpect(jsonPath("$.swiftCodes[1].swiftCode", is("ABCDUS33")));

        verify(swiftCodeService).getByCountry("US");
    }

    @Test
    void addSwiftCode_Success_Returns201Created() throws Exception {
        doNothing().when(swiftCodeService).addSwiftCode(any(SwiftCodeRequestDTO.class));

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", containsString("successfully added")));

        verify(swiftCodeService).addSwiftCode(any(SwiftCodeRequestDTO.class));
    }

    @Test
    void addSwiftCode_Duplicate_Returns409Conflict() throws Exception {
        doThrow(new DuplicateSwiftCodeException("ABCDUS33")).when(swiftCodeService).addSwiftCode(any(SwiftCodeRequestDTO.class));

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString("ABCDUS33")));

        verify(swiftCodeService).addSwiftCode(any(SwiftCodeRequestDTO.class));
    }

    @Test
    void deleteSwiftCode_Success_Returns200Ok() throws Exception {
        doNothing().when(swiftCodeService).deleteBySwiftCode(anyString());

        mockMvc.perform(delete("/v1/swift-codes/ABCDUS33"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("successfully deleted")));

        verify(swiftCodeService).deleteBySwiftCode("ABCDUS33");
    }

    @Test
    void deleteSwiftCode_NotFound_Returns404NotFound() throws Exception {
        doThrow(new SwiftCodeDeletionException("NONEXISTENT")).when(swiftCodeService).deleteBySwiftCode("NONEXISTENT");

        mockMvc.perform(delete("/v1/swift-codes/NONEXISTENT"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("NONEXISTENT")));

        verify(swiftCodeService).deleteBySwiftCode("NONEXISTENT");
    }
}