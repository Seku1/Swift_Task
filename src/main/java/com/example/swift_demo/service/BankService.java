package com.example.swift_demo.service;

import com.example.swift_demo.model.Bank;

import java.util.List;

public interface BankService {
    Bank getBankById(Long id);
    Bank getOrCreateBank(String name);
    List<Bank> getAllBanks();
    void deleteBank(Long id);

}
