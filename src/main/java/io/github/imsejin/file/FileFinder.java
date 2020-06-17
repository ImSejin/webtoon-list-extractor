package io.github.imsejin.file;

import static io.github.imsejin.file.FileService.convertToWebtoons;
import static io.github.imsejin.file.FileService.getFiles;
import static io.github.imsejin.file.FileService.getLatestFileName;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import io.github.imsejin.file.model.Webtoon;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

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
@UtilityClass
public class FileFinder {

    /**
     * 애플리케이션이 있는 현재 경로를 반환한다.<br>
     * Returns the current path where the application is.
     */
    @SneakyThrows(IOException.class)
    public String currentPathName() {
        // 이 코드는 `System.getProperty("user.dir")`으로 대체할 수 있다.
        // This code can be replaced with `System.getProperty("user.dir")`.
        return Paths.get(".").toRealPath().toString();
    }

    /**
     * 지정한 경로에 있는 파일들을 찾아 웹툰으로 변환한다.<br>
     * Finds the files in the specified path and converts them into webtoons.
     */
    public List<Webtoon> findWebtoons(@NonNull String pathName) {
        List<File> files = getFiles(pathName);

        return convertToWebtoons(files);
    }

    /**
     * 최근에 기록된 웹툰 리스트의 파일명을 반환한다.<br>
     * Returns the filename of the latest webtoon list.
     */
    public String findLatestWebtoonListName(@NonNull String pathName) {
        List<File> files = getFiles(pathName);

        return getLatestFileName(files);
    }

}
