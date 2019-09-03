package io.github.imsejin.excel.action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;

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

	public boolean doesWebtoonListExist(String path) {
		return excelService.doesExist(path);
	}

	public Object[] readWebtoonsList(String path) throws FileNotFoundException, IOException {
		return excelService.read(path);
	}

	public void writeWebtoonsList(Object list, String path)
			throws ClassCastException, IOException, FileNotFoundException, ParseException {
		excelService.write(list, path);
	}

	public void writeWebtoonsList(Object[] data, Object presentList, String path)
			throws ClassCastException, IOException, FileNotFoundException, ParseException, InvalidFormatException {
		Object previousList = data[0];
		String previousVersion = (String) data[1];
		FileInputStream fis = (FileInputStream) data[2];
		Workbook workbook = (Workbook) data[3];

		excelService.write(previousList, presentList, path, previousVersion, fis, workbook);
	}

}
