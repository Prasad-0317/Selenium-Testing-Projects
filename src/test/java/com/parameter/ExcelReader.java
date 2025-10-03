package com.parameter;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelReader {

    private static Workbook workbook;
    private static Map<String, String> testData = new HashMap<>();

    // Load Excel only once
    public static void init(String filePath, String sheetName) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for (int i = 1; i < rowCount; i++) { // skip header row
                Row row = sheet.getRow(i);
                String key = row.getCell(0).getStringCellValue();  // first column = key
                String value = row.getCell(1).getStringCellValue(); // second column = value
                testData.put(key, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Excel file not loaded properly!");
        }
    }

    public static List<Map<String, String>> readExcel(String filePath, String sheetName) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();

        FileInputStream fis = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(sheetName);

        // Read headers (first row)
        Row headerRow = sheet.getRow(0);
        List<String> headers = new ArrayList<>();
        for (Cell cell : headerRow) {
            headers.add(cell.toString().trim());
        }

        // Read data rows
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Map<String, String> rowData = new HashMap<>();
            for (int j = 0; j < headers.size(); j++) {
                Cell cell = row.getCell(j);
                String cellValue = (cell != null) ? cell.toString().trim() : "";
                rowData.put(headers.get(j), cellValue);
            }
            data.add(rowData);
        }

        workbook.close();
        fis.close();

        return data;
    }
    
    // Get value by key
    public static String get(String key) {
        return testData.get(key);
    }
}