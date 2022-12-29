package io.github.imsejin.wnliext.file.model;

import com.github.javaxcel.core.annotation.ExcelColumn;
import com.github.javaxcel.core.annotation.ExcelDateTimeFormat;
import com.github.javaxcel.core.annotation.ExcelModel;
import com.github.javaxcel.core.annotation.ExcelModelCreator;
import com.github.javaxcel.core.annotation.ExcelReadExpression;
import com.github.javaxcel.core.annotation.ExcelWriteExpression;
import io.github.imsejin.common.util.FileUtils;
import io.github.imsejin.common.util.FilenameUtils;
import io.github.imsejin.common.util.StringUtils;
import io.github.imsejin.wnliext.excel.config.style.BodyStyleConfig;
import io.github.imsejin.wnliext.excel.config.style.CenterBodyStyleConfig;
import io.github.imsejin.wnliext.excel.config.style.HeaderStyleConfig;
import io.github.imsejin.wnliext.excel.config.style.RightBodyStyleConfig;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(onConstructor = @__(@ExcelModelCreator))
@ExcelModel(headerStyle = HeaderStyleConfig.class, bodyStyle = BodyStyleConfig.class)
public class Webtoon {

    /**
     * Website that serves webtoon for subscribers.
     */
    @NotNull
    @EqualsAndHashCode.Include
    @ExcelColumn(name = "PLATFORM")
    @ExcelWriteExpression("#platform.getCodeName()")
    @ExcelReadExpression("T(io.github.imsejin.wnliext.file.model.Platform).fromCodeName(#platform)")
    private Platform platform;

    /**
     * Title.
     */
    @NotNull
    @EqualsAndHashCode.Include
    @ExcelColumn(name = "TITLE")
    private String title;

    /**
     * Authors.
     */
    @NotNull
    @EqualsAndHashCode.Include
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
    @NotNull
    @ExcelColumn(name = "IMPORTATION_DATE", bodyStyle = CenterBodyStyleConfig.class)
    @ExcelDateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationTime;

    /**
     * File size of webtoon file
     */
    @ExcelColumn(name = "FILE_SIZE(byte)", bodyStyle = RightBodyStyleConfig.class)
    @ExcelWriteExpression("T(io.github.imsejin.common.util.StringUtils).formatComma(#size.doubleValue())")
    @ExcelReadExpression("T(Long).parseLong(#size.replace(',', ''))")
    private long size;

    @Builder
    public Webtoon(@NotNull Platform platform, @NotNull String title, @NotNull List<String> authors,
                   boolean completed, @NotNull LocalDateTime creationTime, long size) {
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
        webtoon.creationTime = LocalDateTime.ofInstant(FileUtils.getFileAttributes(file.toPath())
                .creationTime().toInstant(), ZoneId.systemDefault()).withNano(0);
        webtoon.size = file.length();

        return webtoon;
    }

}
