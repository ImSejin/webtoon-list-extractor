package io.github.imsejin.wnliext.excel;

import com.github.javaxcel.factory.ExcelReaderFactory;
import com.github.javaxcel.factory.ExcelWriterFactory;
import io.github.imsejin.common.util.DateTimeUtils;
import io.github.imsejin.wnliext.common.util.GeneralUtils;
import io.github.imsejin.wnliext.file.model.Webtoon;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import static io.github.imsejin.wnliext.common.Constants.file.EXCEL_FILE_PREFIX;

/**
 * ExcelExecutor
 *
 * @author SEJIN
 */
public final class ExcelExecutor {

    private ExcelExecutor() {
    }

    @SneakyThrows
    public static void create(List<Webtoon> webtoons, String pathname) {
        File file = new File(pathname, generateFilename(webtoons));
        System.out.printf("\nCannot find a list.\nCreate a new list: '%s'\n", file);

        OutputStream out = new FileOutputStream(file);
        Workbook workbook = new HSSFWorkbook();

        ExcelWriterFactory.create(workbook, Webtoon.class)
                .sheetName("Webtoons")
                .disableRolling()
                .autoResizeCols().hideExtraCols()
                .write(out, webtoons);
    }

    @SneakyThrows
    public static void update(List<Webtoon> webtoons, String pathname, File file) {
        File newFile = new File(pathname, generateFilename(webtoons));
        System.out.printf("\nFound the latest list: '%s'\nCreate a new list: '%s'\n", file, newFile);

        // Reads the latest webtoon list file.
        Workbook oldWorkbook = new HSSFWorkbook(new FileInputStream(file));
        List<Webtoon> oldList = ExcelReaderFactory.create(oldWorkbook, Webtoon.class).read();

        // Overwrites creation time of new item with old item.
        for (Webtoon webtoon : oldList) {
            Webtoon matched = webtoons.stream().filter(webtoon::equals).findFirst().orElse(null);

            if (matched != null && !webtoon.getCreationTime().isEqual(matched.getCreationTime())) {
                matched.setCreationTime(webtoon.getCreationTime());
            }
        }

        // Writes data of webtoons.
        OutputStream out = new FileOutputStream(newFile);
        Workbook newWorkbook = new HSSFWorkbook();

        ExcelWriterFactory.create(newWorkbook, Webtoon.class)
                .sheetName("Webtoons")
                .disableRolling()
                .autoResizeCols().hideExtraCols()
                .write(out, webtoons);
    }

    private static String generateFilename(List<Webtoon> webtoons) {
        String version = GeneralUtils.calcVersion(webtoons);
        String now = DateTimeUtils.now();

        return String.format("%s-%s-%s.xls", EXCEL_FILE_PREFIX, version, now);
    }

}
