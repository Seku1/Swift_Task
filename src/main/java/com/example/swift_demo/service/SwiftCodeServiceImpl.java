package com.example.swift_demo.service;

import com.example.swift_demo.exeption.DuplicateSwiftCodeException;
import com.example.swift_demo.exeption.SwiftCodeDeletionException;
import com.example.swift_demo.exeption.SwiftCodeNotFoundException;
import com.example.swift_demo.mastruct.dtos.CountrySwiftCodesResponseDTO;
import com.example.swift_demo.mastruct.dtos.SwiftCodeRequestDTO;
import com.example.swift_demo.mastruct.dtos.SwiftCodeResponseDTO;
import com.example.swift_demo.mastruct.mappers.SwiftCodeMapper;
import com.example.swift_demo.model.SwiftCode;
import com.example.swift_demo.repository.SwiftCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SwiftCodeServiceImpl implements SwiftCodeService {
    @Qualifier("swiftCodeMapperImpl")
    private final SwiftCodeMapper mapper;
    private final SwiftCodeRepository swiftCodeRepository;
    private final CountryService countryService;
    private final BankService bankService;

    @Override
    public SwiftCodeResponseDTO getBySwiftCode(String swiftCode) {
        SwiftCode code = swiftCodeRepository.findById(swiftCode)
                .orElseThrow(() -> new SwiftCodeNotFoundException(swiftCode));

        // Create the basic DTO from the entity
        SwiftCodeResponseDTO dto = mapper.swiftCodeToDto(code);

        // For headquarters, fetch and add all branches
        if (code.isHeadquarter()) {
            // Find all branches associated with this headquarters
            List<SwiftCode> branchEntities = swiftCodeRepository.findByHeadquarterSwiftCode(code);

            // Map branch entities to DTOs
            List<SwiftCodeResponseDTO> branchDtos = branchEntities.stream()
                    .map(mapper::swiftCodeToDto)
                    .collect(Collectors.toList());

            // Set branches on the headquarters DTO
            dto.setBranches(branchDtos);
        } else {
            // For branches, we don't set a branches list
            dto.setBranches(null);
        }

        return dto;
    }

    @Override
    public CountrySwiftCodesResponseDTO getByCountry(String countryISO2) {
        var country = countryService.getCountry(countryISO2);
        List<SwiftCodeResponseDTO> codes = swiftCodeRepository
                .findByCountryIso2IgnoreCase(country.getIso2())
                .stream()
                .map(mapper::swiftCodeToDto)
                .collect(Collectors.toList());
        return mapper.toCountrySwiftCodesResponseDTO(country, codes);
    }

    @Override
    public void addSwiftCode(SwiftCodeRequestDTO dto) {
        var country = countryService.getOrCreateCountry(
                dto.getCountryISO2().toUpperCase(Locale.ROOT),
                dto.getCountryName().toUpperCase(Locale.ROOT)
        );
        var bank = bankService.getOrCreateBank(dto.getBankName().toUpperCase(Locale.ROOT));

        if (swiftCodeRepository.existsById(dto.getSwiftCode())) {
            throw new DuplicateSwiftCodeException(dto.getSwiftCode());
        }

        SwiftCode entity = mapper.dtoToSwiftCode(dto);
        entity.setCountry(country);
        entity.setBank(bank);

        // If this is a branch, associate it with headquarters
        if (!dto.isHeadquarter()) {
            // SWIFT code format: first 8 chars + XXX for headquarters
            String headquarterCode = dto.getSwiftCode().substring(0, 8) + "XXX";
            swiftCodeRepository.findById(headquarterCode).ifPresent(hq -> {
                entity.setHeadquarterSwiftCode(hq);
            });
        }

        swiftCodeRepository.save(entity);
    }

    @Override
    public void deleteBySwiftCode(String swiftCode) {
        SwiftCode code = swiftCodeRepository.findById(swiftCode)
                .orElseThrow(() -> new SwiftCodeNotFoundException(swiftCode));

        // Check if it's a headquarters with branches
        if (code.isHeadquarter() && !swiftCodeRepository.findByHeadquarterSwiftCode(code).isEmpty()) {
            throw new SwiftCodeDeletionException("Cannot delete headquarters with active branches: " + swiftCode);
        }

        swiftCodeRepository.deleteById(swiftCode);
    }
}