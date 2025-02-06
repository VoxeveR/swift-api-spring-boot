package com.swift.swift_service.model;

import com.swift.swift_service.model.dto.SwiftCodeDTO;
import com.swift.swift_service.model.dto.SwiftCodeHeadquartersBranchDTO;
import com.swift.swift_service.model.dto.SwiftCodeHeadquarterResponseDTO;
import com.swift.swift_service.util.XLSXRecord;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SwiftCodeMapper {

    public static SwiftCodeHeadquartersBranchDTO mapSwiftCodeToSwiftCodeBranchResponseDTO(SwiftCode swiftCode) {

        return new SwiftCodeHeadquartersBranchDTO(
                swiftCode.getAddress(),
                swiftCode.getInstitutionName(),
                swiftCode.getCountryIso2(),
                swiftCode.getIsHeadquarter(),
                swiftCode.getSwiftCode()
        );
    }

    public static SwiftCodeHeadquarterResponseDTO mapSwiftCodeToSwiftCodeHeadquarterResponseDTO(SwiftCode swiftCode
            , List<SwiftCodeHeadquartersBranchDTO> branches) {

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
                .codeType(BICType.valueOf(record.codeType().strip()))
                .countryIso2(record.countryIso2().toUpperCase().strip())
                .address(record.address().strip())
                .countryName(record.countryName().toUpperCase().strip())
                .institutionName(record.institutionName().strip())
                .swiftCode(record.swiftCode().strip())
                .timeZone(record.timeZone().strip())
                .townName(record.townName().strip())
                .build();
    }

    public static SwiftCodeDTO mapSwiftCodeToSwiftCodeDTO(SwiftCode swiftCode) {
        return new SwiftCodeDTO(
                swiftCode.getAddress(),
                swiftCode.getInstitutionName(),
                swiftCode.getCountryIso2(),
                swiftCode.getCountryName(),
                swiftCode.getIsHeadquarter(),
                swiftCode.getSwiftCode()
        );
    }

    public static SwiftCode mapSwiftCodeDTOToSwiftCode(SwiftCodeDTO swiftCodeDTO) {
        return SwiftCode.builder()
                .address(swiftCodeDTO.address())
                .countryName(swiftCodeDTO.countryName())
                .countryIso2(swiftCodeDTO.countryISO2())
                .institutionName(swiftCodeDTO.bankName())
                .isHeadquarter(swiftCodeDTO.isHeadquarter())
                .swiftCode(swiftCodeDTO.swiftCode())
                .build();
    }

}
