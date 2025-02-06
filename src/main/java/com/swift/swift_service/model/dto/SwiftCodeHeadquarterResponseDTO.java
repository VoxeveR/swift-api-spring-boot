package com.swift.swift_service.model.dto;
import java.util.List;

public record SwiftCodeHeadquarterResponseDTO(
        String address,
        String bankName,
        String countryISO2,
        String countryName,
        Boolean isHeadquarter,
        String swiftCode,
        List<SwiftCodeHeadquartersBranchDTO> branches
) implements SwiftCodeResponse {}