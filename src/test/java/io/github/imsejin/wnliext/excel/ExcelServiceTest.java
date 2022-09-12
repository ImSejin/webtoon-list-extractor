package io.github.imsejin.wnliext.excel;

import com.github.javaxcel.Javaxcel;
import com.github.javaxcel.in.strategy.impl.Parallel;
import com.github.javaxcel.out.strategy.impl.AutoResizedColumns;
import com.github.javaxcel.out.strategy.impl.HiddenExtraColumns;
import com.github.javaxcel.out.strategy.impl.SheetName;
import io.github.imsejin.common.constant.DateType;
import io.github.imsejin.wnliext.file.FileFinder;
import io.github.imsejin.wnliext.file.model.Webtoon;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExcelServiceTest {

    @Test
    void getAllFontNames() {
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Arrays.stream(fontNames).forEach(System.out::println);
    }

    @Test
    @SneakyThrows
    void write(@TempDir Path path) {
        // given
        String pathname = "F:/Dropbox/cartoons/webtoons";
        List<Webtoon> webtoons = FileFinder.findWebtoons(pathname);

        // when
        @Cleanup Workbook workbook = new SXSSFWorkbook();
        String now = LocalDateTime.now().format(DateType.DATE_TIME.getFormatter());
        File file = new File(path.toFile(), "webtoonList-" + now + ".xlsx");
        @Cleanup FileOutputStream out = new FileOutputStream(file);

        Javaxcel.newInstance().writer(workbook, Webtoon.class)
                .options(new SheetName("List"), new AutoResizedColumns(), new HiddenExtraColumns())
                .write(out, webtoons);

        // then
        System.out.println(webtoons);
    }

    @Test
    @SneakyThrows
    void read() {
        // given
        String pathname = "F:/Dropbox/cartoons/webtoons";

        // when & then
        File webtoonList = FileFinder.findLatestWebtoonList(pathname);
        assertThat(webtoonList).isNotNull();

        // when
        @Cleanup Workbook workbook = new XSSFWorkbook(new FileInputStream(webtoonList));
        List<Webtoon> webtoons = Javaxcel.newInstance()
                .reader(workbook, Webtoon.class)
                .options(new Parallel())
                .read();

        // then
        webtoons.forEach(System.out::println);
        System.out.printf("filename: %s\nnumber of webtoons: %,d\n", webtoonList, webtoons.size());
    }

}