package com.swift.swift_service.model.dto;

import java.util.List;

public record CountrySwiftCodeResponse(
    String countryISO2,
    String countryName,
    List<SwiftCodeBranchDTO> branches
) {}
