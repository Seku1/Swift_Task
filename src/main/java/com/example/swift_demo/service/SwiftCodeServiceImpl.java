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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SwiftCodeServiceImpl implements SwiftCodeService {
    private final SwiftCodeMapper mapper;
    private final SwiftCodeRepository swiftCodeRepository;
    private final CountryService countryService;
    private final BankService bankService;

    @Override
    public SwiftCodeResponseDTO getBySwiftCode(String swiftCode) {
        SwiftCode code = swiftCodeRepository.findById(swiftCode)
                .orElseThrow(() -> new SwiftCodeNotFoundException(swiftCode));
        SwiftCodeResponseDTO dto = mapper.swiftCodeToDto(code);
        if (code.isHeadquarter()) {
            List<SwiftCodeResponseDTO> branches = code.getBranches().stream()
                    .map(mapper::swiftCodeToDto)
                    .collect(Collectors.toList());
            dto.setBranches(branches);
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
        if (!dto.isHeadquarter()) {
            String prefix = dto.getSwiftCode().substring(0, 8) + "XXX";
            swiftCodeRepository.findById(prefix).ifPresent(entity::setHeadquarter);
        }
        swiftCodeRepository.save(entity);
    }

    @Override
    public void deleteBySwiftCode(String swiftCode) {
        if (!swiftCodeRepository.existsById(swiftCode)) {
            throw new SwiftCodeDeletionException(swiftCode);
        }
        swiftCodeRepository.deleteById(swiftCode);
    }
}