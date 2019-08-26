package io.github.imsejin.excel.action;

import java.io.IOException;

import io.github.imsejin.excel.service.ExcelService;

/**
 * ExcelAction
 * 
 * @author SEJIN
 */
public class ExcelAction {

	private final ExcelService excelService;

	public ExcelAction(ExcelService excelService) {
		this.excelService = excelService;
	}

	public void write(Object list, String path) throws ClassCastException, IOException {
		excelService.writeWebtoonsList(list, path);
	}

}
