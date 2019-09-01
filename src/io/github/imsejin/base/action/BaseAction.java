package io.github.imsejin.base.action;

import java.io.FileNotFoundException;
import java.io.IOException;

import io.github.imsejin.console.action.ConsoleAction;
import io.github.imsejin.excel.action.ExcelAction;
import io.github.imsejin.excel.service.ExcelService;
import io.github.imsejin.file.action.FileAction;
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

	public BaseAction() {
		this.fileAction = new FileAction(new FileService());
		this.excelAction = new ExcelAction(new ExcelService());
		this.consoleAction = new ConsoleAction();
	}

	public void execute() {
		String currentPath = fileAction.getCurrentPath();
		Object webtoonsList = fileAction.getWebtoonsList();

		try {
			String previousVersion = ""; // excelAction.getVersionOfWebtoonsList(currentPath);
			excelAction.writeWebtoonsList(webtoonsList, currentPath, previousVersion);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassCastException | IOException e) {
			e.printStackTrace();
		}

		System.out.println("WebtoonListExtractorApplication is successfully done.");
	}

}
