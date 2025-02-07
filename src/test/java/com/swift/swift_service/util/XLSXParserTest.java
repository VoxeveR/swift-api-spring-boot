package com.swift.swift_service.util;

import com.swift.swift_service.model.SwiftCode;
import com.swift.swift_service.repository.SwiftRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class XLSXParserTest {

    @Mock
    private SwiftRepository swiftRepo;

    @InjectMocks
    private XLSXParser xlsxParser;

    @Captor
    ArgumentCaptor<SwiftCode> swiftCodeCaptor;

    @BeforeEach
    void setUp() {
        xlsxParser = new XLSXParser(swiftRepo);
    }

    @Test
    void shouldSaveHeadquarterSwiftCodeRowToDatabase() {
        String filePath = "testBranchHeadquarter.xlsx";
        int sheetNumber = 0;

        xlsxParser.importXLSXData(filePath, sheetNumber);

        verify(swiftRepo).save(swiftCodeCaptor.capture());
        SwiftCode swiftCode = swiftCodeCaptor.getValue();
        assertThat(swiftCode.getIsHeadquarter()).isTrue();
    }

    @Test
    void shouldSaveBranchRowToDatabase() {
        String filePath = "testBranch.xlsx";
        int sheetNumber = 0;

        xlsxParser.importXLSXData(filePath, sheetNumber);
        verify(swiftRepo).save(swiftCodeCaptor.capture());
        SwiftCode swiftCode = swiftCodeCaptor.getValue();
        assertThat(swiftCode.getIsHeadquarter()).isFalse();
    }

    @Test
    void shouldImportFirstRowFromTestXLSX() throws Exception {
        InputStream inputStream = new ClassPathResource("test.xlsx").getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        xlsxParser.skipHeadersRow(rowIterator);
        Row row = rowIterator.next();

        assertEquals("AL", row.getCell(0).getStringCellValue());
        assertEquals("AAISALTRXXX", row.getCell(1).getStringCellValue());
        assertEquals("BIC11", row.getCell(2).getStringCellValue());
        assertEquals("UNITED BANK OF ALBANIA SH.A", row.getCell(3).getStringCellValue());
        assertEquals("HYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023", row.getCell(4).getStringCellValue());
        assertEquals("TIRANA", row.getCell(5).getStringCellValue());
        assertEquals("ALBANIA", row.getCell(6).getStringCellValue());
        assertEquals("Europe/Tirane", row.getCell(7).getStringCellValue());
    }

}