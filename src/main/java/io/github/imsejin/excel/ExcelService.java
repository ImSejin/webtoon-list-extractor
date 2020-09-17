package io.github.imsejin.excel;

import io.github.imsejin.excel.util.ExcelReader;
import io.github.imsejin.excel.util.ExcelWriter;
import io.github.imsejin.file.model.Webtoon;
import io.github.imsejin.util.DateTimeUtils;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static io.github.imsejin.common.Constants.excel.SHEET_NAME_LIST;
import static io.github.imsejin.common.Constants.excel.SHEET_NAME_METADATA;
import static io.github.imsejin.common.Constants.file.EXCEL_FILE_PREFIX;
import static io.github.imsejin.common.Constants.file.XLSX_FILE_EXTENSION;
import static io.github.imsejin.common.util.GeneralUtils.calculateMetadata;
import static io.github.imsejin.excel.util.ExcelStyler.*;

/**
 * ExcelService
 * 
 * @author SEJIN
 */
public class ExcelService {

    private final String suffix = "-" + DateTimeUtils.now();

    private final List<Webtoon> webtoons;
    private final XSSFWorkbook workbook;
    private final File file;
    private List<Webtoon> previousWebtoons;

    private ExcelService(List<Webtoon> webtoons) {
        this.webtoons = webtoons;
        this.workbook = new XSSFWorkbook();
        this.file = null;
    }

    @SneakyThrows({ InvalidFormatException.class, IOException.class })
    private ExcelService(List<Webtoon> webtoons, File file) {
        this.webtoons = webtoons;
        this.workbook = new XSSFWorkbook(file);
        this.file = file;
    }

    //////////////////////////////////////// When create webtoon list ////////////////////////////////////////

    static ExcelService forCreating(@NonNull List<Webtoon> webtoons) {
        return new ExcelService(webtoons);
    }

    ExcelService create() {
        ExcelWriter.create(webtoons, workbook);

        return this;
    }

    //////////////////////////////////////// When update webtoon list ////////////////////////////////////////

    static ExcelService forUpdating(@NonNull List<Webtoon> webtoons, @NonNull File file) {
        return new ExcelService(webtoons, file);
    }

    ExcelService read() {
        this.previousWebtoons = ExcelReader.read(workbook);

        return this;
    }

    ExcelService update() {
        ExcelWriter.update(previousWebtoons, webtoons, workbook);

        return this;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    ExcelService decorate() {
        XSSFSheet listSheet = workbook.getSheet(SHEET_NAME_LIST);
        XSSFSheet metadataSheet = workbook.getSheet(SHEET_NAME_METADATA);

        makeColumnsFitContent(listSheet, webtoons);
        makeColumnsFitContent(metadataSheet);

        // Should execute this before hiding extraneous rows.
//      initializeRowHeight(listSheet);

        hideExtraneousRows(listSheet);
        hideExtraneousRows(metadataSheet);

        // When create webtoon list
        if (file == null) {
            hideExtraneousColumns(listSheet);
            hideExtraneousColumns(metadataSheet);
        }

        return this;
    }

    @SneakyThrows(IOException.class)
    File save(@NonNull String pathname) {
        String filename = EXCEL_FILE_PREFIX + calculateMetadata(webtoons)[0] + suffix + "." + XLSX_FILE_EXTENSION;
        File newWebtoonList = new File(pathname, filename);

        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(newWebtoonList))) {
            workbook.write(out);
        } finally {
            if (workbook != null) workbook.close();
        }

        return newWebtoonList;
    }

}
