package io.github.imsejin.base.action;

import static io.github.imsejin.common.ApplicationMetadata.*;

import java.util.List;

import io.github.imsejin.common.util.StringUtil;
import io.github.imsejin.console.ConsolePrinter;
import io.github.imsejin.excel.action.ExcelAction;
import io.github.imsejin.file.action.FileAction;
import io.github.imsejin.file.model.Webtoon;

/**
 * 기본 액션<br>
 * Base action
 * 
 * <p>
 * 
 * </p>
 * 
 * @author SEJIN
 */
public class BaseAction {

    private final FileAction fileAction;

    private final ExcelAction excelAction = new ExcelAction();

    public BaseAction(String[] args) {
        this.fileAction = args == null || args.length == 0
                ? new FileAction()
                : new FileAction(args[0]);
    }

    public void execute() {
        String pathName = fileAction.getPathName();
        List<Webtoon> webtoonList = fileAction.getWebtoonsList();
        String latestFileName = fileAction.getLatestWebtoonListName();

        try {
            if (StringUtil.isBlank(latestFileName)) {
                excelAction.createWebtoonList(pathName, webtoonList);
            } else {
                excelAction.updateWebtoonList(pathName, webtoonList, latestFileName);
            }

            ConsolePrinter.clear();
            System.out.println(APPLICATION_NAME + " is successfully done.");
        } catch (Exception ex) {
            ConsolePrinter.clear();
            System.out.println(APPLICATION_NAME + " is failed.");
        }
    }

}
