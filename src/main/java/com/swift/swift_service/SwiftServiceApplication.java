package com.swift.swift_service;

import com.swift.swift_service.util.XLSXParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.swift.swift_service.service.SwiftService;

@SpringBootApplication
public class SwiftServiceApplication implements CommandLineRunner {

    @Autowired
    private SwiftService swiftService;

    @Autowired
    private XLSXParser xlsxParser;

    public static void main(String[] args) {
        SpringApplication.run(SwiftServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        xlsxParser.importXLSXData("Interns_2025_SWIFT_CODES.xlsx", 0);
    }
}
