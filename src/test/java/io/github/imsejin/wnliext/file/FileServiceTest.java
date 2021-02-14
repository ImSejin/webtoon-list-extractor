package io.github.imsejin.wnliext.file;

import io.github.imsejin.common.util.StringUtils;
import io.github.imsejin.wnliext.file.constant.Delimiter;
import io.github.imsejin.wnliext.file.model.Platform;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

import static io.github.imsejin.wnliext.file.constant.Delimiter.*;
import static org.assertj.core.api.Assertions.assertThat;

class FileServiceTest {

    private static Map<String, Object> origin(String filename) {
        StringBuilder sb = new StringBuilder(filename);

        // Platform
        int i = sb.indexOf(Delimiter.PLATFORM.getValue());
        String platform = sb.substring(0, i);
        sb.delete(0, i + Delimiter.PLATFORM.getValue().length());

        // Title
        int j = sb.lastIndexOf(Delimiter.TITLE.getValue());
        String title = sb.substring(0, j);
        sb.delete(0, j + Delimiter.TITLE.getValue().length());

        // Completed or uncompleted
        boolean completed = filename.endsWith(COMPLETED.getValue());

        // Authors
        String authors = completed
                ? sb.substring(0, sb.indexOf(COMPLETED.getValue()))
                : sb.toString();

        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("authors", Arrays.asList(authors.split(", ")));
        map.put("platform", Platform.ofKey(platform));
        map.put("completed", completed);

        return map;
    }

    @ParameterizedTest
    @ValueSource(strings = {"C:/usr/local", "C:/data/log", "D:/Cartoons/Webtoons"})
    @DisplayName("Get files")
    void getFiles(String pathname) {
        // given
        File dir = new File(pathname);

        // when
        File[] list = dir.listFiles();
        List<File> files = list == null ? new ArrayList<>() : Arrays.asList(list);

        // then
        assertThat(files).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "NT_일단 뜨겁게 청소하라？! - 앵고",
            "ST_총수 - 최종편 - 정기영, 백승훈 [完]",
            "N_팀 피닉스 - 엄재경, Ze-yAv [完]",
    })
    @DisplayName("Classify webtoon information")
    void classifyWebtoonInfo(String filename) {
        // when
        boolean completed = filename.endsWith(COMPLETED.getValue());
        String regex = completed
                ? String.format("^(.+)%s(.+)%s(.+).{%d}?$", PLATFORM, TITLE, COMPLETED.getValue().length())
                : String.format("^(.+)%s(.+)%s(.+)$", PLATFORM, TITLE);
        Map<Integer, String> match = StringUtils.find(filename, regex, Pattern.MULTILINE, 1, 2, 3);

        Platform platform = Platform.ofKey(match.get(1));
        String title = match.get(2);
        List<String> authors = Arrays.asList(match.get(3).split(", "));

        // then
        Map<String, Object> map = origin(filename);
        System.out.println(platform);
        assertThat(platform)
                .as("#1 platform")
                .isEqualTo(map.get("platform"));

        System.out.println(title);
        assertThat(title)
                .as("#2 title")
                .isEqualTo(map.get("title"));

        System.out.println(authors);
        assertThat(authors)
                .as("#3 authors")
                .isEqualTo(map.get("authors"));

        System.out.println(completed);
        assertThat(completed)
                .as("#4 completed")
                .isEqualTo(map.get("completed"));
    }

}
