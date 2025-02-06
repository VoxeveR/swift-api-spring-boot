package com.swift.swift_service.util;

import com.swift.swift_service.SwiftServiceApplication;
import com.swift.swift_service.model.SwiftCode;
import com.swift.swift_service.model.SwiftCodeMapper;
import com.swift.swift_service.repository.SwiftRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class XLSXParser {

    private final SwiftRepository swiftRepo;

    public XLSXParser(SwiftRepository swiftRepo) {
        this.swiftRepo = swiftRepo;
    }

    public void importXLSXData(String filePath, int sheetNumber){
        Iterator<Row> rowIterator = getSpreadsheetRowIterator(filePath, sheetNumber);

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            XLSXRecord xlsxRecord = getXLSXRecord(row);
            String type = xlsxRecord.swiftCode().endsWith("XXX") ? "HEADQUARTERS" : "BRANCH";
            SwiftCode swiftCode = SwiftCodeMapper.mapXLSXRecordToSwiftCode(xlsxRecord);
            typeSave(swiftCode, xlsxRecord.swiftCode(), type);
        }
    }

    private XLSXRecord getXLSXRecord(Row row){
        return new XLSXRecord(
                getCellValueAsString(row.getCell(0)).toUpperCase(),
                getCellValueAsString(row.getCell(1)),
                getCellValueAsString(row.getCell(2)),
                getCellValueAsString(row.getCell(3)),
                getCellValueAsString(row.getCell(4)),
                getCellValueAsString(row.getCell(5)),
                getCellValueAsString(row.getCell(6)).toUpperCase(),
                getCellValueAsString(row.getCell(7))
        );
    }

    private Iterator<Row> getSpreadsheetRowIterator(String filePath, int sheetNumber) {
        try {
            InputStream fileStream = new ClassPathResource("Interns_2025_SWIFT_CODES.xlsx").getInputStream();

            XSSFWorkbook workbook = new XSSFWorkbook(fileStream);
            XSSFSheet sheet = workbook.getSheetAt(sheetNumber);
            Iterator<Row> rowIterator = sheet.iterator();

            skipHeadersRow(rowIterator);
            return rowIterator;
        } catch (IOException e) {
            log.error("Error while reading file {}", filePath, e);
            throw new RuntimeException(e);
        }
    }

    private void skipHeadersRow(Iterator<Row> rowIterator){
        if(rowIterator.hasNext()){
            rowIterator.next();
        }
    }

    private void typeSave(SwiftCode swiftCodeEntity, String swiftCode, String type){
        if (type.equals("HEADQUARTERS")) {
            swiftCodeEntity.setIsHeadquarter(true);
            String branchSwiftCodePattern = swiftCode.substring(0, 8) + "%";
            List<SwiftCode> branches = swiftRepo.findBySwiftCodeLike(branchSwiftCodePattern);
            swiftRepo.save(swiftCodeEntity);

            for (SwiftCode branch : branches) {
                if (!branch.getSwiftCode().equals(swiftCode)) { // Ensure it's not the headquarters itself
                    branch.setHeadquarter(swiftCodeEntity); // Set the headquarters
                    swiftRepo.save(branch); // Save the updated branch
                }
            }
        }
        if (type.equals("BRANCH")) {
            swiftCodeEntity.setIsHeadquarter(false);
            String headquartersSwiftCode = swiftCode.substring(0, 8) + "XXX";
            SwiftCode headquarters = swiftRepo.findBySwiftCode(headquartersSwiftCode);
            if (headquarters != null) {
                swiftCodeEntity.setHeadquarter(headquarters);
            }
            swiftRepo.save(swiftCodeEntity);
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((int) cell.getNumericCellValue()); // Assuming numeric values are integers
        } else {
            return cell.getStringCellValue();
        }
    }
}
