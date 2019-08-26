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

	public <T> void write(Object list, String path, Class<T> clazz) throws ClassCastException, IOException {
		excelService.writeWebtoonsList(list, path, clazz);
	}

}
