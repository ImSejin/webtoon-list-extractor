package io.github.imsejin.excel.util;

import io.github.imsejin.file.model.Webtoon;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;

import static io.github.imsejin.common.Constants.excel.SHEET_NAME_DATABASE;
import static io.github.imsejin.common.Constants.excel.SHEET_NAME_METADATA;

/**
 * ExcelReader
 * 
 * @author SEJIN
 */
public final class ExcelReader {

    private ExcelReader() {}

    /**
     * The `SXSSFWorkbook` constructor that takes the `XSSFWorkbook` as param.
     * You cannot override or access the initial rows in the template file.
     * You must not use `new SXSSFWorkbook(new XSSFWorkbook(FileInputStream))`.
     */
    public static List<Webtoon> read(Workbook workbook) {
        return getData(workbook);
    }

    private static List<Webtoon> getData(Workbook workbook) {
        // Reads the sheet for database.
        List<Webtoon> webtoons = new ArrayList<>();
        Sheet sheet = workbook.getSheet(SHEET_NAME_DATABASE);
        Row row;

        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);

            // Reads value of the cells.
            String platform = row.getCell(0).getStringCellValue();
            String title = row.getCell(1).getStringCellValue();
            String authors = row.getCell(2).getStringCellValue();
            boolean completed = row.getCell(3).getBooleanCellValue();
            String creationTime = row.getCell(4).getStringCellValue();

            // Sets the value into webtoon.
            Webtoon webtoon = Webtoon.builder()
                    .platform(platform)
                    .title(title)
                    .authors(authors)
                    .completed(completed)
                    .creationTime(creationTime)
                    .build();

            // Puts the webtoon into list.
            webtoons.add(webtoon);
        }

        return webtoons;
    }

    private static String getVersion(Workbook workbook) {
        String version = "";

        try {
            Sheet sheet = workbook.getSheet(SHEET_NAME_METADATA);
            Row row = sheet.getRow(0);
            Cell cell = row.getCell(1);

            version = cell.getStringCellValue();
        } catch (NullPointerException | IllegalArgumentException ex) {
            System.out.println();
            ex.printStackTrace();
            System.out.println(": Metadata is corrupted.");
        }

        return version;
    }

}
