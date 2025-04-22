package com.example.swift_demo.controller;

import com.example.swift_demo.mastruct.dtos.CountrySwiftCodesResponseDTO;
import com.example.swift_demo.mastruct.dtos.MessageResponseDTO;
import com.example.swift_demo.mastruct.dtos.SwiftCodeRequestDTO;
import com.example.swift_demo.mastruct.dtos.SwiftCodeResponseDTO;
import com.example.swift_demo.service.SwiftCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/swift-codes")
@RequiredArgsConstructor
public class SwiftCodeController {

    private final SwiftCodeService swiftCodeService;


    @GetMapping("/{swiftCode}")
    public ResponseEntity<SwiftCodeResponseDTO> getSwiftCodeDetails(@PathVariable String swiftCode) {
        SwiftCodeResponseDTO response = swiftCodeService.getBySwiftCode(swiftCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/country/{countryISO2}")
    public ResponseEntity<CountrySwiftCodesResponseDTO> getSwiftCodesByCountry(@PathVariable String countryISO2) {
        CountrySwiftCodesResponseDTO response = swiftCodeService.getByCountry(countryISO2);
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<MessageResponseDTO> addSwiftCode(@Valid @RequestBody SwiftCodeRequestDTO requestDTO) {
        swiftCodeService.addSwiftCode(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MessageResponseDTO("SWIFT code successfully added"));
    }

    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<MessageResponseDTO> deleteSwiftCode(@PathVariable String swiftCode) {
        swiftCodeService.deleteBySwiftCode(swiftCode);
        return ResponseEntity.ok(new MessageResponseDTO("SWIFT code successfully deleted"));
    }
}