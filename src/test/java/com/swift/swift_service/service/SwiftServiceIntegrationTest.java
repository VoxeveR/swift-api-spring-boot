package com.swift.swift_service.service;


import com.swift.swift_service.model.dto.SwiftCodeDTO;
import com.swift.swift_service.repository.SwiftRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SwiftServiceIntegrationTest {

    @Autowired
    SwiftRepository swiftRepo;

    @Autowired
    SwiftService swiftService;

    @Test
    void shouldSaveAndDeleteSwiftCodeFromDatabaseThenGiveResponseSwiftCodeDeleted() {
        SwiftCodeDTO swiftCodeDTO = new SwiftCodeDTO(
                "testAdress",
                "NAPAD NA BANK WSZYSYC GÓRY RENCE",
                "TE",
                "testCountryName",
                false,
                "10203040101");

        swiftService.addNewSwiftCode(swiftCodeDTO);

        ResponseEntity<Map<String, String>> mapResponseEntity
                = swiftService.deleteSwiftCode(swiftCodeDTO.swiftCode());

        assertThat(mapResponseEntity.getBody().get("message")).isEqualTo("Swift Code Deleted");
    }
}
