package com.swift.swift_service.service;

import com.swift.swift_service.model.SwiftCode;
import com.swift.swift_service.model.SwiftCodeMapper;
import com.swift.swift_service.model.dto.CountrySwiftCodeResponse;
import com.swift.swift_service.model.dto.SwiftCodeHeadquartersBranchDTO;
import com.swift.swift_service.model.dto.SwiftCodeDTO;
import com.swift.swift_service.model.dto.SwiftCodeResponse;
import com.swift.swift_service.model.BICType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.swift.swift_service.repository.SwiftRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                return ResponseEntity.ok((SwiftCodeMapper.mapSwiftCodeToSwiftCodeDTO(swiftCode)));
            } else {

                List<SwiftCode> headquarterBranches = getHeadquarterBranches(swiftCode);

                List<SwiftCodeHeadquartersBranchDTO> list =
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

        List<SwiftCodeHeadquartersBranchDTO> allBranches
                = foundCountrySwiftCode.stream().map(SwiftCodeMapper::mapSwiftCodeToSwiftCodeBranchResponseDTO).toList();

        CountrySwiftCodeResponse response = new CountrySwiftCodeResponse(
                foundCountrySwiftCode.get(0).getCountryIso2(),
                foundCountrySwiftCode.get(0).getCountryName(),
                allBranches
        );

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String,String>> addNewSwiftCode(SwiftCodeDTO newSwiftCode){
        Map<String, String> response = getConstraintResponse(newSwiftCode);

        if (!response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }

        BICType codeBIC = getBICCode(newSwiftCode.swiftCode());

        Optional<SwiftCode> headquarter = getHeadquarterForBranch(newSwiftCode);

        SwiftCode insert = SwiftCodeMapper.mapSwiftCodeDTOToSwiftCode(newSwiftCode);

        headquarter.ifPresent(insert::setHeadquarter);
        insert.setCodeType(codeBIC);

        swiftRepo.save(insert);

        response.put("message","Swift added successfully");
        return ResponseEntity.ok(response);
    }

    private BICType getBICCode(String swiftCodeAsString) {
        return (swiftCodeAsString.length() == 8) ? BICType.BIC8 : BICType.BIC11;
    }

    private Optional<SwiftCode> getHeadquarterForBranch(SwiftCodeDTO swiftCode) {
        Optional<SwiftCode> headquarter = Optional.empty();

        if (!swiftCode.isHeadquarter()) {
            headquarter = Optional.ofNullable(swiftRepo.findBySwiftCodeStartsWithAndIsHeadquarterTrue(swiftCode.swiftCode().substring(0, 8)));
        }

        return headquarter;
    }

    private Map<String, String> getConstraintResponse(SwiftCodeDTO newSwiftCode) {
        Map<String, String> validationMessages = new HashMap<>();
        String swiftCode = newSwiftCode.swiftCode();
        int length = swiftCode.length();

        if (swiftRepo.findBySwiftCode(swiftCode) != null) {
            validationMessages.put("message", "Already Exists");
            return validationMessages;
        }

        if (checkLengthConstraint(length)) {
            validationMessages.put("message", "Swift Code must be 8 or 11 characters");
            return validationMessages;
        }

        if (newSwiftCode.isHeadquarter()) {
            boolean isValidHeadquarterCode = (length == 8) || (swiftCode.endsWith("XXX"));
            if (!isValidHeadquarterCode) {
                validationMessages.put("message", "Headquarter SWIFT Code must be exactly 8 characters or 11 characters ending with 'XXX'");
                return validationMessages;
            }
        }

        return validationMessages;
    }

    private Boolean checkLengthConstraint(int length){
        return length != 8 && length != 11;
    }

    public ResponseEntity<Map<String, String>> deleteSwiftCode(String swiftCode) {

        Map<String, String> response = new HashMap<>();

        if (swiftRepo.findBySwiftCode(swiftCode) == null) {
            response.put("message", "Already Doesn't Exist");
            return ResponseEntity.unprocessableEntity().body(response);
        }

        swiftRepo.removeSwiftCodeBySwiftCode(swiftCode);
        response.put("message", "Swift Code Deleted");
        return ResponseEntity.ok().body(response);
    }
}