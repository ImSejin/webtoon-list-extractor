package io.github.imsejin.wnliext.file.model;

import com.github.javaxcel.annotation.ExcelColumn;
import com.github.javaxcel.annotation.ExcelDateTimeFormat;
import com.github.javaxcel.annotation.ExcelModel;
import com.github.javaxcel.annotation.ExcelReadExpression;
import com.github.javaxcel.annotation.ExcelWriteExpression;
import io.github.imsejin.common.util.FileUtils;
import io.github.imsejin.common.util.FilenameUtils;
import io.github.imsejin.common.util.StringUtils;
import io.github.imsejin.wnliext.excel.config.BodyStyleConfig;
import io.github.imsejin.wnliext.excel.config.CenterBodyStyleConfig;
import io.github.imsejin.wnliext.excel.config.HeaderStyleConfig;
import io.github.imsejin.wnliext.excel.config.RightBodyStyleConfig;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nonnull;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static io.github.imsejin.wnliext.file.constant.Delimiter.AUTHOR;
import static io.github.imsejin.wnliext.file.constant.Delimiter.COMPLETED;
import static io.github.imsejin.wnliext.file.constant.Delimiter.PLATFORM;
import static io.github.imsejin.wnliext.file.constant.Delimiter.TITLE;

/**
 * Webtoon
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"platform", "title", "authors"})
@NoArgsConstructor
@ExcelModel(headerStyle = HeaderStyleConfig.class, bodyStyle = BodyStyleConfig.class)
public class Webtoon {

    /**
     * Website that serves webtoon for subscribers.
     */
    @Nonnull
    @ExcelColumn(name = "PLATFORM")
    @ExcelWriteExpression("#platform.getCodeName()")
    @ExcelReadExpression("T(io.github.imsejin.wnliext.file.model.Platform).fromCodeName(#platform)")
    private Platform platform;

    /**
     * Title.
     */
    @Nonnull
    @ExcelColumn(name = "TITLE")
    private String title;

    /**
     * Authors.
     */
    @Nonnull
    @ExcelColumn(name = "AUTHORS")
    @ExcelWriteExpression("#authors.toString().replaceAll('[\\[\\]]', '')")
    @ExcelReadExpression("T(java.util.Arrays).stream(#authors.split(', '))" +
            ".collect(T(java.util.stream.Collectors).toList())")
    private List<String> authors;

    /**
     * Whether webtoon is completed or not.
     */
    @ExcelColumn(name = "COMPLETED", bodyStyle = CenterBodyStyleConfig.class)
    @ExcelWriteExpression("T(String).valueOf(#completed).toUpperCase()")
    @ExcelReadExpression("'true'.equalsIgnoreCase(#completed)")
    private boolean completed;

    /**
     * Creation time of webtoon file.
     */
    @Nonnull
    @ExcelColumn(name = "IMPORTATION_DATE", bodyStyle = CenterBodyStyleConfig.class)
    @ExcelDateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationTime;

    /**
     * File size of webtoon file
     */
    @ExcelColumn(name = "FILE_SIZE(byte)", bodyStyle = RightBodyStyleConfig.class)
    @ExcelWriteExpression("T(io.github.imsejin.common.util.StringUtils).formatComma(#size)")
    @ExcelReadExpression("T(Long).parseLong(#size.replace(',', ''))")
    private long size;

    @Builder
    public Webtoon(@Nonnull Platform platform, @Nonnull String title, @Nonnull List<String> authors,
                   boolean completed, @Nonnull LocalDateTime creationTime, long size) {
        this.platform = platform;
        this.title = title;
        this.authors = authors;
        this.completed = completed;
        this.creationTime = creationTime;
        this.size = size;
    }

    public static Webtoon from(File file) {
        String fileBaseName = FilenameUtils.getBaseName(file.getName());

        boolean completed = fileBaseName.endsWith(COMPLETED.getValue());
        String regex;
        if (completed) {
            regex = String.format("^(.+)%s(.+)%s(.+).{%d}?$", PLATFORM, TITLE, COMPLETED.getValue().length());
        } else {
            regex = String.format("^(.+)%s(.+)%s(.+)$", PLATFORM, TITLE);
        }

        Map<Integer, String> match = StringUtils.find(fileBaseName, regex, Pattern.MULTILINE, 1, 2, 3);

        Webtoon webtoon = new Webtoon();
        webtoon.platform = Platform.fromCode(match.get(1));
        webtoon.title = match.get(2);
        webtoon.authors = Arrays.asList(match.get(3).split(AUTHOR.getValue()));
        webtoon.completed = completed;
        // To compares written date time with this, removes nanoseconds.
        webtoon.creationTime = LocalDateTime.ofInstant(FileUtils.getFileAttributes(file)
                .creationTime().toInstant(), ZoneId.systemDefault()).withNano(0);
        webtoon.size = file.length();

        return webtoon;
    }

}
