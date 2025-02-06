package com.swift.swift_service.model.dto;

public record SwiftCodeBranchDTO(
        String address,
        String bankName,
        String countryISO2,
        Boolean isHeadquarter,
        String swiftCode
) implements SwiftCodeResponse {}
