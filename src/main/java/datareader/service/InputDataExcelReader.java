package datareader.service;

import datareader.model.InputData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
public class InputDataExcelReader implements InputDataReader {

    public List<InputData> read(String pathToFile) {
        log.info("Attempting to read file '{}'", pathToFile);
        List<InputData> inputData = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(pathToFile);
             XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {

            XSSFSheet worksheet = workbook.getSheetAt(0);
            IteratorUtils.toList(worksheet.rowIterator())
                    .stream()
                    .skip(1)
                    .filter(this::rowIsNotEmpty)
                    .map(this::createInputDataEntry)
                    .forEach(inputData::add);
        } catch (IOException | NullPointerException e) {
            log.error("Failed to open and process file {}", pathToFile);
            printExceptionDetails(e);
            return Collections.emptyList();
        } catch (NumberFormatException e) {
            log.error("Price is not a valid number in input file");
            printExceptionDetails(e);
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Unexpected error");
            printExceptionDetails(e);
            return Collections.emptyList();
        }
        log.info("Successfully extracted {} row(s)", inputData.size());
        return inputData;
    }

    private boolean rowIsNotEmpty(Row row) {
        String firstCellValue = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
        String secondCellValue = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
        String thirdCellValue = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
        return !(isBlank(firstCellValue) && isBlank(secondCellValue) && isBlank(thirdCellValue));
    }

    private InputData createInputDataEntry(Row row) {
        return InputData.builder()
                .minPrice(readLong(row, 0))
                .maxPrice(readLong(row, 1))
                .area(readAndNormalizeString(row, 2))
                .build();
    }

    private String readAndNormalizeString(Row row, int cellNumber) {
        DataFormatter formatter = new DataFormatter(Locale.US);
        Cell cell = row.getCell(cellNumber, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return formatter.formatCellValue(cell);
    }

    private long readLong(Row row, int cellNumber) {
        String cellStringValue = readAndNormalizeString(row, cellNumber);
        return (long) Double.parseDouble(cellStringValue);
    }

    private void printExceptionDetails(Exception e) {
        log.error("Exception type: {}", e.getClass().getName());
        log.error("Exception message: {}", e.getMessage());
    }
}

