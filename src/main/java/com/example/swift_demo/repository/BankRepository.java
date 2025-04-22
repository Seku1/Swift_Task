package com.example.swift_demo.repository;

import com.example.swift_demo.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BankRepository extends JpaRepository<Bank, String> {
    Optional<Bank> findByNameIgnoreCase(String name);
}
