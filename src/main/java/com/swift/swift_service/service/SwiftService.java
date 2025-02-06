package com.swift.swift_service.service;

import com.swift.swift_service.model.SwiftCode;
import com.swift.swift_service.model.SwiftCodeMapper;
import com.swift.swift_service.model.dto.CountrySwiftCodeResponse;
import com.swift.swift_service.model.dto.SwiftCodeBranchDTO;
import com.swift.swift_service.model.dto.SwiftCodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.swift.swift_service.repository.SwiftRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SwiftService {

    private final SwiftRepository swiftRepo;

    @Autowired
    public SwiftService(SwiftRepository swiftRepo) {
        this.swiftRepo = swiftRepo;
    }

    public ResponseEntity<SwiftCodeResponse> getSwiftData(String swiftCodeAsString) {
        Optional<SwiftCode> foundSwiftCode = Optional.ofNullable(swiftRepo.findBySwiftCode(swiftCodeAsString));

        if(foundSwiftCode.isPresent()) {
            SwiftCode swiftCode = foundSwiftCode.get();
            if(!swiftCode.getIsHeadquarter()){
                return ResponseEntity.ok((SwiftCodeMapper.mapSwiftCodeToSwiftCodeBranchResponseDTO(swiftCode)));
            } else {

                List<SwiftCode> headquarterBranches = getHeadquarterBranches(swiftCode);

                List<SwiftCodeBranchDTO> list =
                        headquarterBranches.stream().map(SwiftCodeMapper::mapSwiftCodeToSwiftCodeBranchResponseDTO).toList();

                return ResponseEntity.ok(SwiftCodeMapper.mapSwiftCodeToSwiftCodeHeadquarterResponseDTO(swiftCode, list));
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private List<SwiftCode> getHeadquarterBranches(SwiftCode swiftCode) {
        return swiftRepo.findAllByHeadquarter(swiftCode);
    }

    public ResponseEntity<CountrySwiftCodeResponse> getCountrySwiftData(String countryISO2AsString) {
        List<SwiftCode> foundCountrySwiftCode = swiftRepo.findAllByCountryIso2(countryISO2AsString);

        if (foundCountrySwiftCode.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<SwiftCodeBranchDTO> allBranches
                = foundCountrySwiftCode.stream().map(SwiftCodeMapper::mapSwiftCodeToSwiftCodeBranchResponseDTO).toList();

        CountrySwiftCodeResponse response = new CountrySwiftCodeResponse(
                foundCountrySwiftCode.get(0).getCountryIso2(),
                foundCountrySwiftCode.get(0).getCountryName(),
                allBranches
        );

        return ResponseEntity.ok(response);
    }
}