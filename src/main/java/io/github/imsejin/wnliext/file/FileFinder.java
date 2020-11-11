package io.github.imsejin.wnliext.file;

import io.github.imsejin.wnliext.file.model.Webtoon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.List;

import static io.github.imsejin.wnliext.file.FileService.*;

/**
 * File finder
 *
 * <p> Reads webtoon files at the specified path
 * and checks if there is a webtoon list already written.
 */
public final class FileFinder {

    private FileFinder() {
    }

    /**
     * Finds the files in the specified path and converts them into webtoons.
     */
    public static List<Webtoon> findWebtoons(@Nonnull String pathname) {
        List<File> files = getFiles(pathname);
        return convert(files);
    }

    /**
     * Returns the latest webtoon list.
     */
    @Nullable
    public static File findLatestWebtoonList(@Nonnull String pathname) {
        List<File> files = getFiles(pathname);
        return getLatestFile(files);
    }

}
