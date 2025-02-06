package com.swift.swift_service.util;

public record XLSXRecord(
    String countryIso2,
    String swiftCode,
    String codeType,
    String institutionName,
    String address,
    String townName,
    String countryName,
    String timeZone
) {
}
