package io.github.imsejin.wnliext.excel;

import com.github.javaxcel.factory.ExcelReaderFactory;
import com.github.javaxcel.factory.ExcelWriterFactory;
import io.github.imsejin.common.util.DateTimeUtils;
import io.github.imsejin.wnliext.file.FileFinder;
import io.github.imsejin.wnliext.file.model.Webtoon;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ExcelServiceTest {

    @Test
    public void getAllFontNames() {
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Arrays.stream(fontNames).forEach(System.out::println);
    }

    @Test
    @SneakyThrows
    public void write() {
        // given
        String pathname = "D:/Cartoons/Webtoons";
        List<Webtoon> webtoons = FileFinder.findWebtoons(pathname);

        // when
        @Cleanup HSSFWorkbook workbook = new HSSFWorkbook();
        File file = new File(pathname, "webtoonList-" + DateTimeUtils.now() + ".xls");
        @Cleanup FileOutputStream out = new FileOutputStream(file);

        ExcelWriterFactory.create(workbook, Webtoon.class)
                .sheetName("List")
                .autoResizeCols().hideExtraCols()
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
        @Cleanup HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(webtoonList));
        List<Webtoon> webtoons = ExcelReaderFactory.create(workbook, Webtoon.class).parallel().read();

        // then
        webtoons.forEach(System.out::println);
        System.out.printf("filename: %s\nnumber of webtoons: %,d\n", webtoonList, webtoons.size());
    }

}