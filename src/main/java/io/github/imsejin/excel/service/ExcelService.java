package io.github.imsejin.excel.service;

import static io.github.imsejin.common.Constants.excel.*;
import static io.github.imsejin.common.Constants.file.*;
import static io.github.imsejin.common.util.GeneralUtil.*;
import static io.github.imsejin.excel.util.ExcelStyleUtil.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import io.github.imsejin.common.util.DateUtil;
import io.github.imsejin.excel.model.ListHeader;
import io.github.imsejin.excel.model.MetadataHeader;
import io.github.imsejin.file.model.Webtoon;
import lombok.SneakyThrows;

/**
 * ExcelService
 * 
 * @author SEJIN
 */
public class ExcelService {

    private static final ListHeader[] listHeaders = ListHeader.values();
    private static final MetadataHeader[] metadataHeaders = MetadataHeader.values();

    public final ExcelService.reader reader = new reader();
    public final ExcelService.writer writer = new writer();

    private final String suffix = "-" + DateUtil.getCurrentDateTime();

    public class reader {

        private reader() {}

        /**
         * The `SXSSFWorkbook` constructor that takes the `XSSFWorkbook` as param.
         * You cannot override or access the initial rows in the template file.
         * You must not use `new SXSSFWorkbook(new XSSFWorkbook(FileInputStream))`.
         */
        @SneakyThrows({ FileNotFoundException.class, IOException.class })
        public Object[] read(String pathName, String latestFileName) {
            Object[] interactions = new Object[4];
            File file = new File(pathName, latestFileName);

            // Do not close these yet.
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            interactions[0] = fis;
            interactions[1] = workbook;
            interactions[2] = getData(workbook);
            interactions[3] = getVersion(workbook);

            return interactions;
        }

        private List<Webtoon> getData(Workbook workbook) {
            // Reads the sheet for database.
            List<Webtoon> webtoonsList = new ArrayList<>();
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
                webtoonsList.add(webtoon);
            }

            return webtoonsList;
        }

        private String getVersion(Workbook workbook) {
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

    public class writer {

        private writer() {}

        public Object[] create(String pathName, List<Webtoon> webtoonList) {
            Object[] interactions = new Object[2];
            String fileName = EXCEL_FILE_PREFIX + calculateMetadata(webtoonList)[0] + suffix + "." + XLSX_FILE_EXTENSION;
            File file = new File(pathName, fileName);
            XSSFWorkbook workbook = new XSSFWorkbook();

            createListSheet(workbook, webtoonList);
            createMetadataSheet(workbook, webtoonList);
            createDatabaseSheet(workbook, webtoonList);

            interactions[0] = file;
            interactions[1] = workbook;

            return interactions;
        }

        private void createListSheet(XSSFWorkbook workbook, List<Webtoon> webtoonList) {
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
            for (int i = 0; i < webtoonList.size(); i++) {
                webtoon = webtoonList.get(i);
                row = sheet.createRow(i + 1);

                // Sets the data of webtoon into cell.
                decorateCell(row.createCell(0), styleOfContent).setCellValue(webtoon.getPlatform());
                decorateCell(row.createCell(1), styleOfContent).setCellValue(webtoon.getTitle());
                decorateCell(row.createCell(2), styleOfContent).setCellValue(webtoon.getAuthors());
                decorateCell(row.createCell(3), styleOfContent).setCellValue(webtoon.isCompleted());
                decorateCell(row.createCell(4), styleOfImportationDate).setCellValue(webtoon.getCreationTime());
            }
        }

        private void createMetadataSheet(XSSFWorkbook workbook, List<Webtoon> webtoonList) {
            // Constitutes excel sheet.
            XSSFSheet sheet = workbook.createSheet(SHEET_NAME_METADATA);
            XSSFRow row;

            // Gets calculated metadata.
            String[] metadataContents = calculateMetadata(webtoonList);

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
        private void createDatabaseSheet(XSSFWorkbook workbook, List<Webtoon> webtoonList) {
            // Constitutes excel sheet.
            XSSFSheet sheet = workbook.createSheet(SHEET_NAME_DATABASE);
            XSSFRow row;

            // Creates decorated rows of content.
            Webtoon webtoon;
            for (int i = 0; i < webtoonList.size(); i++) {
                webtoon = webtoonList.get(i);
                row = sheet.createRow(i);

                // Sets the data of webtoon into cell.
                row.createCell(0).setCellValue(webtoon.getPlatform());
                row.createCell(1).setCellValue(webtoon.getTitle());
                row.createCell(2).setCellValue(webtoon.getAuthors());
                row.createCell(3).setCellValue(webtoon.isCompleted());
                row.createCell(4).setCellValue(webtoon.getCreationTime());
            }
        }

        public File update(String pathName, List<Webtoon> previousList, List<Webtoon> presentList, String version, FileInputStream fis,
                XSSFWorkbook workbook) {
            String fileName = EXCEL_FILE_PREFIX + calculateMetadata(presentList)[0] + suffix + "." + XLSX_FILE_EXTENSION;
            File file = new File(pathName, fileName);

            updateListSheet(workbook, previousList, presentList);
            updateMetadataSheet(workbook, presentList);
            updateDatabaseSheet(workbook, previousList, presentList);

            return file;
        }

        private void updateListSheet(XSSFWorkbook workbook, List<Webtoon> previousList, List<Webtoon> presentList) {
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

        private void updateMetadataSheet(XSSFWorkbook workbook, List<Webtoon> presentList) {
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

        private void updateDatabaseSheet(XSSFWorkbook workbook, List<Webtoon> previousList, List<Webtoon> presentList) {
            // Removes the sheet for database.
            workbook.removeSheetAt(2);
            XSSFSheet sheet = workbook.createSheet(SHEET_NAME_DATABASE);
            workbook.setSheetOrder(SHEET_NAME_DATABASE, 2);
            XSSFRow row;

            // Removes duplicated webtoons in present list.
            List<Webtoon> dummy = new ArrayList<>(presentList);
            previousList.forEach(previous -> {
                dummy.removeIf(present -> {
                    boolean isTitleEqual = previous.getTitle().equals(present.getTitle());
                    boolean isAuthorEqual = previous.getAuthors().equals(present.getAuthors());
                    boolean isPlatformEqual = previous.getPlatform().equals(present.getPlatform());
                    boolean isCompletionEqual = previous.isCompleted() == present.isCompleted();

                    return isTitleEqual && isAuthorEqual && isPlatformEqual && isCompletionEqual;
                });
            });

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
                    boolean isTitleEqual = from.getTitle().equals(to.getTitle());
                    boolean isAuthorEqual = from.getAuthors().equals(to.getAuthors());
                    boolean isPlatformEqual = from.getPlatform().equals(to.getPlatform());
                    boolean isCompletionEqual = from.isCompleted() == to.isCompleted();

                    if (isTitleEqual && isAuthorEqual && isPlatformEqual && isCompletionEqual) to.setCreationTime(time);
                }
            }
        }

    }

    public void decorateWhenCreate(XSSFWorkbook workbook, List<Webtoon> webtoonList) {
        XSSFSheet listSheet = workbook.getSheet(SHEET_NAME_LIST);
        XSSFSheet metadataSheet = workbook.getSheet(SHEET_NAME_METADATA);

        makeColumnsFitContent(listSheet, webtoonList);
        makeColumnsFitContent(metadataSheet);
        hideExtraneousRows(listSheet);
        hideExtraneousRows(metadataSheet);
        hideExtraneousColumns(listSheet);
        hideExtraneousColumns(metadataSheet);
    }

    public void decorateWhenUpdate(XSSFWorkbook workbook, List<Webtoon> webtoonList) {
        XSSFSheet listSheet = workbook.getSheet(SHEET_NAME_LIST);
        XSSFSheet metadataSheet = workbook.getSheet(SHEET_NAME_METADATA);

        makeColumnsFitContent(listSheet, webtoonList);
        makeColumnsFitContent(metadataSheet);

        // Should execute this before hiding extraneous rows.
//		initializeRowHeight(listSheet);

        hideExtraneousRows(listSheet);
        hideExtraneousRows(metadataSheet);
    }

    @SneakyThrows(IOException.class)
    public void save(File file, XSSFWorkbook workbook) {
        try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(file)); XSSFWorkbook wb = workbook) {
            workbook.write(fos);
            workbook.close();
        } finally {
            if (workbook != null) workbook.close();
        }
    }

}
