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
        ExcelService.forCreating(webtoons, pathName)
                .create()
                .decorate()
                .save();
    }

    public void updateWebtoonList(List<Webtoon> webtoons, String pathName, File file) {
        ExcelService.forUpdating(webtoons, pathName, file)
                .read()
                .update()
                .decorate()
                .save();
    }

}
