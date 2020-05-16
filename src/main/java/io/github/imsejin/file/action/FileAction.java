package io.github.imsejin.file.action;

import java.io.File;
import java.util.List;

import io.github.imsejin.file.model.Webtoon;
import io.github.imsejin.file.service.FileService;
import lombok.Getter;
import lombok.NonNull;

/**
 * 파일 액션<br>
 * File action
 * 
 * <p>
 * 지정한 경로에 있는 웹툰 파일을 읽고 이미 작성된 웹툰 리스트가 있는지 확인한다.<br>
 * Reads webtoon files at the specified path and checks if there is a webtoon list already written.
 * </p>
 * 
 * @author SEJIN
 */
public class FileAction {

    private final FileService service = new FileService();

    /**
     * 웹툰 파일과 웹툰 리스트가 있는 경로<br>
     * The path with webtoon files and webtoon list
     */
    @Getter
    @NonNull
    private final String currentPathName;

    /**
     * 경로를 지정하지 않았을 경우, 애플리케이션의 현재 위치로 지정되어 할당된다.<br>
     * If the path is not specified, it is specified as the current location of the application and assigned.
     */
    public FileAction() {
        this.currentPathName = this.service.getCurrentAbsolutePath(); // "D:/Cartoons/Webtoons";
    }

    /**
     * 지정한 경로를 할당한다.<br>
     * Assigns the specified path.
     */
    public FileAction(@NonNull String pathName) {
        this.currentPathName = pathName;
    }

    /**
     * Returns list of webtoons.
     */
    public List<Webtoon> getWebtoonsList() {
        List<File> fileList = service.getFileList(currentPathName);
        List<Webtoon> webtoonList = service.convertToWebtoonList(fileList);

        return webtoonList;
    }

    /**
     * Returns latest file name.
     */
    public String getLatestWebtoonListName() {
        List<File> fileList = service.getFileList(currentPathName);

        return service.getLatestFileName(fileList);
    }

}
