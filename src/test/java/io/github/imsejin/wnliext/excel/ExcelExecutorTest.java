package io.github.imsejin.wnliext.excel;

import com.github.javaxcel.core.Javaxcel;
import com.github.pjfanning.xlsx.StreamingReader;
import io.github.imsejin.wnliext.common.util.ZipUtils;
import io.github.imsejin.wnliext.file.FileFinder;
import io.github.imsejin.wnliext.file.model.Webtoon;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

class ExcelExecutorTest {

    @Test
    @SneakyThrows
    void overwriteCreationTime() {
        // given
        String pathname = "D:/Cartoons/Webtoons";
        List<Webtoon> webtoons = FileFinder.findWebtoons(pathname);
        File file = FileFinder.findLatestWebtoonList(pathname);

        if (file == null) return;

        // when
        Workbook oldWorkbook = StreamingReader.builder().open(file);
        List<Webtoon> oldList = Javaxcel.newInstance().reader(oldWorkbook, Webtoon.class).read();

        for (Webtoon oldThing : oldList) {
            Optional<Webtoon> maybeNewThing = webtoons.stream().filter(oldThing::equals).findFirst();
            maybeNewThing.ifPresent(it -> {
                if (oldThing.getCreationTime().equals(it.getCreationTime())) return;
                System.out.printf("old: %s / %s || new: %s / %s\n",
                        oldThing.getTitle(), oldThing.getCreationTime(), it.getTitle(), it.getCreationTime());
                it.setCreationTime(oldThing.getCreationTime());
            });
        }
    }

    @Test
    void convertFileToWebtoon() {
        // given
        String pathname = "D:/Cartoons/Webtoons";
        File[] arr = new File(pathname).listFiles();

        if (arr == null) return;

        // when
        List<File> files = Arrays.asList(arr);
        List<Webtoon> duplicates = files.stream()
                .filter(ZipUtils::isZip)
                .map(Webtoon::from)
                .distinct()
                .sorted(comparing((Webtoon it) -> it.getPlatform().getCodeName())
                        .thenComparing(Webtoon::getTitle)) // Sorts list of webtoons.
                .collect(toList());

        // then
        duplicates.forEach(System.out::println);
        System.out.printf("\nTotal %,d webtoon%s\n", duplicates.size(), duplicates.isEmpty() ? "" : "s");

        // Checks duplicated webtoons.
        duplicates.stream().filter(it -> Collections.frequency(duplicates, it) > 1)
                .forEach(System.out::println);
    }

}
