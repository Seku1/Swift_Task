package com.example.swift_demo.service;

import com.example.swift_demo.exeption.CountryNotFoundException;
import com.example.swift_demo.model.Country;
import com.example.swift_demo.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public Country getCountry(String iso2) {
        return countryRepository.findById(iso2.toUpperCase())
                .orElseThrow(() -> new CountryNotFoundException(iso2));
    }

    @Override
    public Country getOrCreateCountry(String iso2, String name) {
        return countryRepository.findById(iso2)
                .orElseGet(() -> {
                    Country country = new Country();
                    country.setIso2(iso2);
                    country.setName(name);
                    return countryRepository.save(country);
                });
    }

    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public void deleteCountry(String iso2) {
        if (!countryRepository.existsById(iso2.toUpperCase())) {
            throw new CountryNotFoundException(iso2);
        }
        countryRepository.deleteById(iso2.toUpperCase());
    }
}
