package com.example.swift_demo.repository;

import com.example.swift_demo.model.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwiftCodeRepository extends JpaRepository<SwiftCode, String> {
    List<SwiftCode> findByCountryIso2IgnoreCase(String countryIso2);
    List<SwiftCode> findByHeadquarterSwiftCode(SwiftCode headquarter);
}
