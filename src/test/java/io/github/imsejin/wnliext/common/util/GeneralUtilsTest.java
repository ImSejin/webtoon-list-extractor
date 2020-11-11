package io.github.imsejin.wnliext.common.util;

import io.github.imsejin.wnliext.file.FileFinder;
import io.github.imsejin.wnliext.file.model.Webtoon;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GeneralUtilsTest {

    @Test
    void calcVersion() {
        // given
        String pathname = "D:/Cartoons/Webtoons";
        List<Webtoon> webtoons = FileFinder.findWebtoons(pathname);

        // when
        String version = GeneralUtils.calcVersion(webtoons);

        // then
        assertThat(version).matches("^\\d+\\.\\d+.\\d$");
        System.out.printf("version: %s\n", version);
    }

}
