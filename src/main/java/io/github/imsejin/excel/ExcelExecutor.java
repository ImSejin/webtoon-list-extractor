package io.github.imsejin.excel;

import java.io.File;
import java.util.List;

import io.github.imsejin.file.model.Webtoon;
import lombok.experimental.UtilityClass;

/**
 * ExcelExecutor
 * 
 * @author SEJIN
 */
@UtilityClass
public class ExcelExecutor {

    public void createWebtoonList(List<Webtoon> webtoons, String pathName) {
        ExcelService.forCreating(webtoons)
                .create()
                .decorate()
                .save(pathName);
    }

    public void updateWebtoonList(List<Webtoon> webtoons, String pathName, File file) {
        ExcelService.forUpdating(webtoons, file)
                .read()
                .update()
                .decorate()
                .save(pathName);
    }

}
