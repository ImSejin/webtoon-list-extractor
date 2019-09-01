package io.github.imsejin.excel.action;

import java.io.FileNotFoundException;
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

	public String getVersionOfWebtoonsList(String path) throws IOException, FileNotFoundException {
		return excelService.read(path);
	}

	public void writeWebtoonsList(Object list, String path, String version) throws ClassCastException, IOException, FileNotFoundException {
		excelService.write(list, path, version);
	}

}
