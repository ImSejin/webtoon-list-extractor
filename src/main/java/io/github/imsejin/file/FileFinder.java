package io.github.imsejin.file;

import io.github.imsejin.file.model.Webtoon;
import lombok.NonNull;

import java.io.File;
import java.util.List;

import static io.github.imsejin.file.FileService.*;

/**
 * 파일 파인더<br>
 * File finder
 * 
 * <p>
 * 지정한 경로에 있는 웹툰 파일을 읽고 이미 작성된 웹툰 리스트가 있는지 확인한다.<br>
 * Reads webtoon files at the specified path and checks if there is a webtoon list already written.
 * </p>
 * 
 * @author SEJIN
 */
public final class FileFinder {

    private FileFinder() {}

    /**
     * 지정한 경로에 있는 파일들을 찾아 웹툰으로 변환한다.<br>
     * Finds the files in the specified path and converts them into webtoons.
     */
    public static List<Webtoon> findWebtoons(@NonNull String pathname) {
        List<File> files = getFiles(pathname);

        return convertToWebtoons(files);
    }

    /**
     * 최근에 기록된 웹툰 리스트의 파일명을 반환한다.<br>
     * Returns the filename of the latest webtoon list.
     */
    public static String findLatestWebtoonListName(@NonNull String pathname) {
        List<File> files = getFiles(pathname);

        return getLatestFilename(files);
    }

}
