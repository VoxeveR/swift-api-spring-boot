package com.swift.swift_service.model.dto;

public record SwiftCodeDTO(
    String address,
    String bankName,
    String countryISO2,
    String countryName,
    Boolean isHeadquarter,
    String swiftCode
    ) implements SwiftCodeResponse {}
