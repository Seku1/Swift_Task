package com.example.swift_demo.controller;


import com.example.swift_demo.service.SwiftCodeService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestConfig {

    @Bean
    @Primary
    public SwiftCodeService swiftCodeService() {
        return Mockito.mock(SwiftCodeService.class);
    }
}