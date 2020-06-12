package io.github.imsejin.file;

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
     * Returns list of webtoons.
     */
    public List<Webtoon> findWebtoons(@NonNull String pathName) {
        List<File> files = FileService.getFiles(pathName);
        List<Webtoon> webtoons = FileService.convertToWebtoons(files);

        return webtoons;
    }

    /**
     * Returns latest file name.
     */
    public String findLatestWebtoonListName(@NonNull String pathName) {
        List<File> files = FileService.getFiles(pathName);

        return FileService.getLatestFileName(files);
    }

}
