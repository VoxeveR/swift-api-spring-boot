package com.swift.swift_service.model;

import com.swift.swift_service.model.dto.CountrySwiftCodeResponse;
import com.swift.swift_service.model.dto.SwiftCodeBranchDTO;
import com.swift.swift_service.model.dto.SwiftCodeHeadquarterResponseDTO;
import com.swift.swift_service.util.XLSXRecord;
import java.util.List;

public class SwiftCodeMapper {

    public static SwiftCodeBranchDTO mapSwiftCodeToSwiftCodeBranchResponseDTO(SwiftCode swiftCode) {

        return new SwiftCodeBranchDTO(
                swiftCode.getAddress(),
                swiftCode.getInstitutionName(),
                swiftCode.getCountryIso2(),
                swiftCode.getIsHeadquarter(),
                swiftCode.getSwiftCode()
        );
    }

    public static SwiftCodeHeadquarterResponseDTO mapSwiftCodeToSwiftCodeHeadquarterResponseDTO(SwiftCode swiftCode
            , List<SwiftCodeBranchDTO> branches) {

        return new SwiftCodeHeadquarterResponseDTO(
                swiftCode.getAddress(),
                swiftCode.getInstitutionName(),
                swiftCode.getCountryIso2(),
                swiftCode.getCountryName(),
                swiftCode.getIsHeadquarter(),
                swiftCode.getSwiftCode(),
                branches
        );
    }

    public static SwiftCode mapXLSXRecordToSwiftCode(XLSXRecord record) {
        return SwiftCode.builder()
                .codeType(record.codeType().strip())
                .countryIso2(record.countryIso2().toUpperCase().strip())
                .address(record.address().strip())
                .countryName(record.countryName().toUpperCase().strip())
                .institutionName(record.institutionName().strip())
                .swiftCode(record.swiftCode().strip())
                .timeZone(record.timeZone().strip())
                .townName(record.townName().strip()).build();
    }

}
