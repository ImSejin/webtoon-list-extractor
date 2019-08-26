package io.github.imsejin.base.action;

import java.io.IOException;

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

	public BaseAction() {
		this.fileAction = new FileAction(new FileService());
		this.excelAction = new ExcelAction(new ExcelService());
	}

	public void execute() {
		String currentPath = fileAction.getCurrentPath();
		Object webtoonsList = fileAction.getWebtoonsList();

		try {
			excelAction.write(webtoonsList, currentPath);
		} catch (ClassCastException | IOException e) {
			e.printStackTrace();
		}
	}

}
