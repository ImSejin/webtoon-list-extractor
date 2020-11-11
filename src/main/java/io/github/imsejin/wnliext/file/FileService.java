package io.github.imsejin.wnliext.file;

import io.github.imsejin.common.util.CollectionUtils;
import io.github.imsejin.common.util.FilenameUtils;
import io.github.imsejin.wnliext.common.util.ZipUtils;
import io.github.imsejin.wnliext.file.model.Webtoon;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.imsejin.wnliext.common.Constants.file.EXCEL_FILE_PREFIX;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

/**
 * File service
 *
 * <p> Extracts the information of webtoon from archive file
 * and returns it in the form of a list.
 */
public final class FileService {

    private FileService() {
    }

    /**
     * Returns a list of files and directories in the path.
     */
    static List<File> getFiles(String pathname) {
        File[] files = new File(pathname).listFiles();
        return files == null ? new ArrayList<>() : Arrays.asList(files);
    }

    /**
     * Converts list of files and directories to list of webtoons.
     */
    static List<Webtoon> convert(List<File> files) {
        if (files == null) files = new ArrayList<>();

        return files.stream()
                .filter(ZipUtils::isZip)
                .map(Webtoon::from)
                .distinct() // Removes duplicated webtoons.
                .sorted(comparing((Webtoon it) -> it.getPlatform().value())
                        .thenComparing(Webtoon::getTitle)) // Sorts list of webtoons.
                .collect(toList());
    }

    @Nullable
    static File getLatestFile(List<File> files) {
        File latestFile = null;

        // Shallow copy.
        List<File> clone = new ArrayList<>(files);

        // Removes non-webtoon-list from list.
        clone.removeIf(it ->!it.isFile()
                || !FilenameUtils.baseName(it).startsWith(EXCEL_FILE_PREFIX)
                || !FilenameUtils.extension(it).equals("xlsx"));

        // Sorts out the latest file.
        if (CollectionUtils.exists(clone)) {
            latestFile = clone.stream().max(comparing(File::getName)).orElse(null);
        }

        return latestFile;
    }

}
