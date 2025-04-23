package com.example.swift_demo.controller;

import com.example.swift_demo.exeption.DuplicateSwiftCodeException;
import com.example.swift_demo.exeption.SwiftCodeDeletionException;
import com.example.swift_demo.exeption.SwiftCodeNotFoundException;
import com.example.swift_demo.mastruct.dtos.CountrySwiftCodesResponseDTO;
import com.example.swift_demo.mastruct.dtos.SwiftCodeRequestDTO;
import com.example.swift_demo.mastruct.dtos.SwiftCodeResponseDTO;
import com.example.swift_demo.service.SwiftCodeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SwiftCodeController.class)
@Import(TestConfig.class)
class SwiftCodeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SwiftCodeService swiftCodeService;

    @Test
    @DisplayName("GET /v1/swift-codes/{code} - Found HQ")
    void whenGetExistingHQ_thenReturnsJson() throws Exception {
        SwiftCodeResponseDTO branchDto = new SwiftCodeResponseDTO(
                "BRANCH1", "TEST BANK", "123 Main St", "US", "UNITED STATES", false, null);
        SwiftCodeResponseDTO dto = new SwiftCodeResponseDTO(
                "TESTUSXXX", "TEST BANK", "456 Wall St", "US", "UNITED STATES", true, List.of(branchDto));

        given(swiftCodeService.getBySwiftCode("TESTUSXXX")).willReturn(dto);

        mockMvc.perform(get("/v1/swift-codes/TESTUSXXX").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.swiftCode", is("TESTUSXXX")))
                .andExpect(jsonPath("$.headquarter", is(true)))
                .andExpect(jsonPath("$.branches", hasSize(1)));
    }

    @Test
    @DisplayName("GET /v1/swift-codes/{code} - Not Found")
    void whenGetNonexistent_thenReturns404() throws Exception {
        given(swiftCodeService.getBySwiftCode("NONEXISTENT")).willThrow(new SwiftCodeNotFoundException("NONEXISTENT"));

        mockMvc.perform(get("/v1/swift-codes/NONEXISTENT").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString("NONEXISTENT")));
    }

    @Test
    @DisplayName("GET /v1/swift-codes/country/{iso} - Found")
    void whenGetByCountry_thenReturnsList() throws Exception {
        SwiftCodeResponseDTO c1 = new SwiftCodeResponseDTO("C1", "BANK1", "Addr1", "US", "UNITED STATES", true, null);
        SwiftCodeResponseDTO c2 = new SwiftCodeResponseDTO("C2", "BANK2", "Addr2", "US", "UNITED STATES", false, null);
        CountrySwiftCodesResponseDTO dto = new CountrySwiftCodesResponseDTO();
        dto.setCountryISO2("US");
        dto.setCountryName("UNITED STATES");
        dto.setSwiftCodes(List.of(c1, c2));

        given(swiftCodeService.getByCountry("US")).willReturn(dto);

        mockMvc.perform(get("/v1/swift-codes/country/US").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.swiftCodes", hasSize(2)));
    }

    @Test
    @DisplayName("POST /v1/swift-codes - Success")
    void whenPostValid_thenReturns201() throws Exception {
        SwiftCodeRequestDTO request = new SwiftCodeRequestDTO();
        request.setSwiftCode("NEWCODE");
        request.setBankName("BANK");
        request.setCountryISO2("US");
        request.setCountryName("UNITED STATES");
        request.setAddress("ADDR");
        request.setHeadquarter(true);

        doNothing().when(swiftCodeService).addSwiftCode(any(SwiftCodeRequestDTO.class));

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString("successfully added")));
    }

    @Test
    @DisplayName("POST /v1/swift-codes - Duplicate")
    void whenPostDuplicate_thenReturns409() throws Exception {
        SwiftCodeRequestDTO request = new SwiftCodeRequestDTO();
        request.setSwiftCode("DUP");
        request.setBankName("BANK");
        request.setCountryISO2("US");
        request.setCountryName("UNITED STATES");
        request.setAddress("ADDR");
        request.setHeadquarter(true);

        Mockito.doThrow(new DuplicateSwiftCodeException("DUP"))
                .when(swiftCodeService).addSwiftCode(eq(request));

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString("DUP")));
    }

    @Test
    @DisplayName("DELETE /v1/swift-codes/{code} - Success")
    void whenDelete_thenReturnsOk() throws Exception {
        doNothing().when(swiftCodeService).deleteBySwiftCode("CODE");

        mockMvc.perform(delete("/v1/swift-codes/CODE").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString("successfully deleted")));
    }

    @Test
    @DisplayName("DELETE /v1/swift-codes/{code} - Has Branches")
    void whenDeleteHQWithBranches_thenReturnsNotFound() throws Exception {
        Mockito.doThrow(new SwiftCodeDeletionException("Cannot delete"))
                .when(swiftCodeService).deleteBySwiftCode("HQ");

        mockMvc.perform(delete("/v1/swift-codes/HQ").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString("Cannot delete")));
    }
}