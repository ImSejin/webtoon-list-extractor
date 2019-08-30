package io.github.imsejin.base.action;

import java.io.IOException;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi.Attribute;
import com.diogonunes.jcdp.color.api.Ansi.BColor;
import com.diogonunes.jcdp.color.api.Ansi.FColor;

import io.github.imsejin.console.action.ConsoleAction;
import io.github.imsejin.console.serivce.ConsoleService;
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
			excelAction.write(webtoonsList, currentPath);
		} catch (ClassCastException | IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("WebtoonListExtractorApplication is successfully done.");
	}

}
