package io.github.imsejin.wnliext.file;

import io.github.imsejin.wnliext.file.model.Webtoon;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

import static io.github.imsejin.wnliext.file.FileService.convert;
import static io.github.imsejin.wnliext.file.FileService.getFiles;
import static io.github.imsejin.wnliext.file.FileService.getLatestFile;

/**
 * File finder
 *
 * <p> Reads webtoon files at the specified path
 * and checks if there is a webtoon list already written.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileFinder {

    /**
     * Finds the files in the specified path and converts them into webtoons.
     */
    public static List<Webtoon> findWebtoons(@NotNull String pathname) {
        List<File> files = getFiles(pathname);
        return convert(files);
    }

    /**
     * Returns the latest webtoon list.
     */
    @Nullable
    public static File findLatestWebtoonList(@NotNull String pathname) {
        List<File> files = getFiles(pathname);
        return getLatestFile(files);
    }

}
