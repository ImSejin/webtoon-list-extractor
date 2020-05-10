package io.github.imsejin.file.action;

import java.io.File;
import java.util.List;

import io.github.imsejin.file.model.Webtoon;
import io.github.imsejin.file.service.FileService;
import lombok.Getter;
import lombok.NonNull;

/**
 * FileAction
 * 
 * @author SEJIN
 */
public class FileAction {

    private final FileService service = new FileService();

    @Getter
    @NonNull
    private final String currentPathName;

    /**
     * 경로를 지정하지 않았을 경우
     */
    public FileAction() {
        this.currentPathName = this.service.getCurrentAbsolutePath(); // "D:/Cartoons/Webtoons";
    }

    /**
     * 경로를 지정한 경우
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
