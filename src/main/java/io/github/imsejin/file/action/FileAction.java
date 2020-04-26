package io.github.imsejin.file.action;

import java.io.File;
import java.util.List;

import io.github.imsejin.file.model.Webtoon;
import io.github.imsejin.file.service.FileService;
import lombok.Getter;

/**
 * FileAction
 * 
 * @author SEJIN
 */
public class FileAction {

    private final FileService service = new FileService();

    @Getter
    private final String currentPathName;

    public FileAction() {
        this.currentPathName = this.service.getCurrentAbsolutePath(); // "D:\\Cartoons\\Webtoons\\";
    }

    public FileAction(String path) {
        this.currentPathName = path;
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
