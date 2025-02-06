package com.swift.swift_service.controller;

import com.swift.swift_service.model.dto.CountrySwiftCodeResponse;
import com.swift.swift_service.model.dto.SwiftCodeDTO;
import com.swift.swift_service.model.dto.SwiftCodeResponse;
import com.swift.swift_service.service.SwiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftController {

    SwiftService swiftService;

    @Autowired
    public SwiftController(SwiftService swiftService) {
        this.swiftService = swiftService;
    }

    @GetMapping("/{swift-code}")
    public ResponseEntity<SwiftCodeResponse> getSwiftData(@PathVariable("swift-code") String swiftCode) {
        return swiftService.getSwiftData(swiftCode);
    }
    
    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<CountrySwiftCodeResponse> getCountrySwiftData(@PathVariable("countryISO2code") String countryISO2) {
        return swiftService.getCountrySwiftData(countryISO2);
    }

    @PostMapping
    public ResponseEntity<Map<String,String>> addNewSwiftCode(@RequestBody SwiftCodeDTO swiftCodeRequest) {
        return swiftService.addNewSwiftCode(swiftCodeRequest);
    }

    @DeleteMapping("/{swift-code}")
    public ResponseEntity<Map<String, String>> deleteSwiftCode(@PathVariable("swift-code") String swiftCode) {
        return swiftService.deleteSwiftCode(swiftCode);
    }
}
