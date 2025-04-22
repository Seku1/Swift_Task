package com.example.swift_demo.service;

import com.example.swift_demo.model.Bank;
import com.example.swift_demo.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;

    @Override
    public Bank getBankById(Long id) {
        return bankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank not found: " + id));
    }

    @Override
    public Bank getOrCreateBank(String name) {
        return bankRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> bankRepository.save(new Bank(null, name, List.of())));
    }

    @Override
    public List<Bank> getAllBanks() {
        return bankRepository.findAll();
    }

    @Override
    public void deleteBank(Long id) {
        if (!bankRepository.existsById(id)) {
            throw new RuntimeException("Bank not found: " + id);
        }
        bankRepository.deleteById(id);
    }
}