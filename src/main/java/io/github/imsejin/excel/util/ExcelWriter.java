package io.github.imsejin.excel.util;

import static io.github.imsejin.common.Constants.excel.SHEET_NAME_DATABASE;
import static io.github.imsejin.common.Constants.excel.SHEET_NAME_LIST;
import static io.github.imsejin.common.Constants.excel.SHEET_NAME_METADATA;
import static io.github.imsejin.common.util.GeneralUtil.calculateMetadata;
import static io.github.imsejin.excel.util.ExcelStyler.decorateCell;
import static io.github.imsejin.excel.util.ExcelStyler.getContentCellStyle;
import static io.github.imsejin.excel.util.ExcelStyler.getContentCellStyleWithAlignment;
import static io.github.imsejin.excel.util.ExcelStyler.getHeaderCellStyle;
import static io.github.imsejin.excel.util.ExcelStyler.increaseRowHeight;
import static io.github.imsejin.excel.util.ExcelStyler.removeAllRows;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import io.github.imsejin.excel.model.ListHeader;
import io.github.imsejin.excel.model.MetadataHeader;
import io.github.imsejin.file.model.Webtoon;
import lombok.experimental.UtilityClass;

/**
 * ExcelWriter
 * 
 * @author SEJIN
 */
@UtilityClass
public class ExcelWriter {

    private static final ListHeader[] listHeaders = ListHeader.values();

    private static final MetadataHeader[] metadataHeaders = MetadataHeader.values();

    public void create(List<Webtoon> webtoons, XSSFWorkbook workbook) {
        createListSheet(webtoons, workbook);
        createMetadataSheet(webtoons, workbook);
        createDatabaseSheet(webtoons, workbook);
    }

    private void createListSheet(List<Webtoon> webtoons, XSSFWorkbook workbook) {
        // Constitutes excel sheet.
        XSSFSheet sheet = workbook.createSheet(SHEET_NAME_LIST);
        XSSFRow row = sheet.createRow(0);

        // Creates decorated head row.
        increaseRowHeight(sheet, row, 1.5);
        CellStyle styleOfHeader = getHeaderCellStyle(workbook);
        for (int i = 0; i < listHeaders.length; i++) {
            decorateCell(row.createCell(i), styleOfHeader).setCellValue(listHeaders[i].name());
        }

        // Creates decorated rows of content.
        Webtoon webtoon;
        CellStyle styleOfContent = getContentCellStyle(workbook);
        CellStyle styleOfImportationDate = getContentCellStyleWithAlignment(workbook);
        for (int i = 0; i < webtoons.size(); i++) {
            webtoon = webtoons.get(i);
            row = sheet.createRow(i + 1);

            // Sets the data of webtoon into cell.
            decorateCell(row.createCell(0), styleOfContent).setCellValue(webtoon.getPlatform());
            decorateCell(row.createCell(1), styleOfContent).setCellValue(webtoon.getTitle());
            decorateCell(row.createCell(2), styleOfContent).setCellValue(webtoon.getAuthors());
            decorateCell(row.createCell(3), styleOfContent).setCellValue(webtoon.isCompleted());
            decorateCell(row.createCell(4), styleOfImportationDate).setCellValue(webtoon.getCreationTime());
        }
    }

    private void createMetadataSheet(List<Webtoon> webtoons, XSSFWorkbook workbook) {
        // Constitutes excel sheet.
        XSSFSheet sheet = workbook.createSheet(SHEET_NAME_METADATA);
        XSSFRow row;

        // Gets calculated metadata.
        String[] metadataContents = calculateMetadata(webtoons);

        // Creates decorated title cells and content cells.
        CellStyle titleStyle = getHeaderCellStyle(workbook);
        CellStyle contentStyle = getContentCellStyleWithAlignment(workbook);
        for (int i = 0; i < 2; i++) {
            row = sheet.createRow(i);
            increaseRowHeight(sheet, row, 1.5);

            decorateCell(row.createCell(0), titleStyle).setCellValue(metadataHeaders[i].name());
            decorateCell(row.createCell(1), contentStyle).setCellValue(metadataContents[i]);
        }
    }

    /**
     * Creates sheet for database.
     * Do not use `hideExtraneousRows`
     * because it causes `Sheet.getLastRowNum` not to work.
     */
    private void createDatabaseSheet(List<Webtoon> webtoons, XSSFWorkbook workbook) {
        // Constitutes excel sheet.
        XSSFSheet sheet = workbook.createSheet(SHEET_NAME_DATABASE);
        XSSFRow row;

        // Creates decorated rows of content.
        Webtoon webtoon;
        for (int i = 0; i < webtoons.size(); i++) {
            webtoon = webtoons.get(i);
            row = sheet.createRow(i);

            // Sets the data of webtoon into cell.
            row.createCell(0).setCellValue(webtoon.getPlatform());
            row.createCell(1).setCellValue(webtoon.getTitle());
            row.createCell(2).setCellValue(webtoon.getAuthors());
            row.createCell(3).setCellValue(webtoon.isCompleted());
            row.createCell(4).setCellValue(webtoon.getCreationTime());
        }
    }

    public void update(List<Webtoon> previousList, List<Webtoon> presentList, XSSFWorkbook workbook) {
        updateListSheet(previousList, presentList, workbook);
        updateMetadataSheet(presentList, workbook);
        updateDatabaseSheet(previousList, presentList, workbook);
    }

    private void updateListSheet(List<Webtoon> previousList, List<Webtoon> presentList, XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.getSheet(SHEET_NAME_LIST);
        XSSFRow row;

        // Overwrites importation time in present list to importation time in previous list.
        overwriteCreationTime(previousList, presentList);

        // Removes all rows except for header.
        removeAllRows(sheet);

        // Creates decorated rows of content.
        Webtoon webtoon;
        CellStyle styleOfContent = getContentCellStyle(workbook);
        CellStyle styleOfImportationDate = getContentCellStyleWithAlignment(workbook);
        for (int i = 0; i < presentList.size(); i++) {
            webtoon = presentList.get(i);
            row = sheet.createRow(i + 1);

            // Sets the data of webtoon into cell.
            decorateCell(row.createCell(0), styleOfContent).setCellValue(webtoon.getPlatform());
            decorateCell(row.createCell(1), styleOfContent).setCellValue(webtoon.getTitle());
            decorateCell(row.createCell(2), styleOfContent).setCellValue(webtoon.getAuthors());
            decorateCell(row.createCell(3), styleOfContent).setCellValue(webtoon.isCompleted());
            decorateCell(row.createCell(4), styleOfImportationDate).setCellValue(webtoon.getCreationTime());
        }
    }

    private void updateMetadataSheet(List<Webtoon> presentList, XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.getSheet(SHEET_NAME_METADATA);
        XSSFRow row;
        XSSFCell cell;

        // Gets calculated metadata.
        String[] metadataContents = calculateMetadata(presentList);

        // Updates metadata.
        for (int i = 0; i < metadataContents.length; i++) {
            row = sheet.getRow(i);
            cell = row.getCell(1);
            cell.setCellValue(metadataContents[i]);
        }
    }

    private void updateDatabaseSheet(List<Webtoon> previousList, List<Webtoon> presentList, XSSFWorkbook workbook) {
        // Removes the sheet for database.
        workbook.removeSheetAt(2);
        XSSFSheet sheet = workbook.createSheet(SHEET_NAME_DATABASE);
        workbook.setSheetOrder(SHEET_NAME_DATABASE, 2);
        XSSFRow row;

        // Removes duplicated webtoons in present list.
        List<Webtoon> dummy = new ArrayList<>(presentList);
        for (Webtoon previous : previousList) {
            dummy.removeIf(present -> present.equals(previous));
        }

        // Merges previous list with new webtoons in present list.
        previousList.addAll(dummy);

        // Sorts the previous list.
        previousList.sort(Comparator.comparing(Webtoon::getPlatform).thenComparing(Webtoon::getTitle));

        // Creates decorated rows of content.
        Webtoon webtoon;
        for (int i = 0; i < previousList.size(); i++) {
            webtoon = previousList.get(i);
            row = sheet.createRow(i);

            // Sets the data of webtoon into cell.
            row.createCell(0).setCellValue(webtoon.getPlatform());
            row.createCell(1).setCellValue(webtoon.getTitle());
            row.createCell(2).setCellValue(webtoon.getAuthors());
            row.createCell(3).setCellValue(webtoon.isCompleted());
            row.createCell(4).setCellValue(webtoon.getCreationTime());
        }
    }

    private void overwriteCreationTime(List<Webtoon> fromList, List<Webtoon> toList) {
        for (Webtoon from : fromList) {
            String time = from.getCreationTime();

            for (Webtoon to : toList) {
                if (from.equals(to)) to.setCreationTime(time);
            }
        }
    }

}
