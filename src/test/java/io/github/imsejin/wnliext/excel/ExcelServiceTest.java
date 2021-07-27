package io.github.imsejin.wnliext.excel;

import com.github.javaxcel.factory.ExcelReaderFactory;
import com.github.javaxcel.factory.ExcelWriterFactory;
import io.github.imsejin.common.util.DateTimeUtils;
import io.github.imsejin.wnliext.file.FileFinder;
import io.github.imsejin.wnliext.file.model.Webtoon;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.*;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ExcelServiceTest {

    @Test
    public void getAllFontNames() {
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Arrays.stream(fontNames).forEach(System.out::println);
    }

    @Test
    @SneakyThrows
    public void write(@TempDir Path path) {
        // given
        String pathname = "D:/Cartoons/Webtoons";
        List<Webtoon> webtoons = FileFinder.findWebtoons(pathname);

        // when
        @Cleanup Workbook workbook = new XSSFWorkbook();
        File file = new File(path.toFile(), "webtoonList-" + DateTimeUtils.now() + ".xlsx");
        @Cleanup FileOutputStream out = new FileOutputStream(file);

        ExcelWriterFactory.create(workbook, Webtoon.class)
                .sheetName("List")
                .autoResizeColumns()
                .hideExtraColumns()
                .write(out, webtoons);

        // then
        System.out.println(webtoons);
    }

    @Test
    @SneakyThrows
    public void read() {
        // given
        String pathname = "D:/Cartoons/Webtoons";

        // when & then
        File webtoonList = FileFinder.findLatestWebtoonList(pathname);
        assertThat(webtoonList).isNotNull();

        // when
        @Cleanup Workbook workbook = new XSSFWorkbook(new FileInputStream(webtoonList));
        List<Webtoon> webtoons = ExcelReaderFactory.create(workbook, Webtoon.class).parallel().read();

        // then
        webtoons.forEach(System.out::println);
        System.out.printf("filename: %s\nnumber of webtoons: %,d\n", webtoonList, webtoons.size());
    }

}