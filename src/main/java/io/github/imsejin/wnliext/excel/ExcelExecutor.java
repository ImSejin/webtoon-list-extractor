package io.github.imsejin.wnliext.excel;

import com.github.javaxcel.factory.ExcelReaderFactory;
import com.github.javaxcel.factory.ExcelWriterFactory;
import io.github.imsejin.common.util.DateTimeUtils;
import io.github.imsejin.wnliext.common.util.GeneralUtils;
import io.github.imsejin.wnliext.file.model.Webtoon;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

import static io.github.imsejin.wnliext.common.Constants.file.EXCEL_FILE_PREFIX;

/**
 * Excel executor
 */
public final class ExcelExecutor {

    private ExcelExecutor() {
    }

    public static void create(List<Webtoon> webtoons, String pathname) {
        File file = createFile(pathname, webtoons);
        System.out.printf("\nCannot find a list.\nCreate a new list: '%s'\n", file);

        write(file, webtoons);
    }

    public static void update(List<Webtoon> webtoons, String pathname, File file) {
        File newFile = createFile(pathname, webtoons);
        System.out.printf("\nFound the latest list: '%s'\nCreate a new list: '%s'\n", file, newFile);

        // Overwrites creation time of new item with old item.
        List<Webtoon> oldList = read(file);
        overwriteDateTime(oldList, webtoons);

        write(newFile, webtoons);
    }

    private static File createFile(String pathname, List<Webtoon> webtoons) {
        String version = GeneralUtils.calcVersion(webtoons);
        String now = DateTimeUtils.now();
        String filename = String.format("%s-%s-%s.xlsx", EXCEL_FILE_PREFIX, version, now);

        return new File(pathname, filename);
    }

    @SneakyThrows
    private static void write(File file, List<Webtoon> webtoons) {
        OutputStream out = new FileOutputStream(file);
        Workbook newWorkbook = new XSSFWorkbook();

        ExcelWriterFactory.create(newWorkbook, Webtoon.class)
                .sheetName("Webtoons")
                .disableRolling()
                .autoResizeCols().hideExtraCols()
                .write(out, webtoons);
    }

    @SneakyThrows
    private static List<Webtoon> read(File file) {
        Workbook oldWorkbook = new XSSFWorkbook(new FileInputStream(file));
        return ExcelReaderFactory.create(oldWorkbook, Webtoon.class).read();
    }

    /**
     * Overwrites creation time of new item with old item.
     *
     * @param oldList old webtoon list
     * @param newList new webtoon list
     */
    private static void overwriteDateTime(List<Webtoon> oldList, List<Webtoon> newList) {
        for (Webtoon oldThing : oldList) {
            Optional<Webtoon> maybeNewThing = newList.stream().filter(oldThing::equals).findFirst();
            maybeNewThing.ifPresent(it -> {
                if (oldThing.getCreationTime().isEqual(it.getCreationTime())) return;
                it.setCreationTime(oldThing.getCreationTime());
            });
        }
    }

}
