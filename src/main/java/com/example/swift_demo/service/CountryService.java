package com.example.swift_demo.service;

import com.example.swift_demo.model.Country;

import java.util.List;

public interface CountryService {
    Country getCountry(String iso2);
    Country getOrCreateCountry(String iso2, String name);
    List<Country> getAllCountries();
    void deleteCountry(String iso2);
}
