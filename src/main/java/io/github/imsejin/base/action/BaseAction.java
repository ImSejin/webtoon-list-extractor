package io.github.imsejin.base.action;

import java.util.List;

import io.github.imsejin.common.util.StringUtil;
import io.github.imsejin.console.action.ConsoleAction;
import io.github.imsejin.console.serivce.ConsoleService;
import io.github.imsejin.excel.action.ExcelAction;
import io.github.imsejin.excel.service.ExcelService;
import io.github.imsejin.file.action.FileAction;
import io.github.imsejin.file.model.Webtoon;
import io.github.imsejin.file.service.FileService;

/**
 * BaseAction
 * 
 * @author SEJIN
 */
public class BaseAction {

    private final FileAction fileAction;

    private final ExcelAction excelAction;

    private final ConsoleAction consoleAction;

    public BaseAction(String[] args) {
        this.fileAction = args == null || args.length == 0
                ? new FileAction(new FileService())
                : new FileAction(new FileService(), args[0]);
        this.excelAction = new ExcelAction(new ExcelService());
        this.consoleAction = new ConsoleAction();
    }

    public void execute() {
        String currentPath = fileAction.getCurrentPath();
        List<Webtoon> webtoonList = fileAction.getWebtoonsList();
        String recentFileName = fileAction.getRecentFileName();

        try {
            if (StringUtil.isBlank(recentFileName)) {
                excelAction.createWebtoonList(currentPath, webtoonList);
            } else {
                excelAction.updateWebtoonList(currentPath, recentFileName, webtoonList);
            }

            ConsoleService.clear();
            System.out.println("WebtoonListExtractor is successfully done.");
        } catch (Exception e) {
            ConsoleService.clear();
            System.out.println("WebtoonListExtractor failed.");
            e.printStackTrace();
        }
    }

}
