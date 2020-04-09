package io.github.imsejin.excel.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import io.github.imsejin.excel.service.ExcelService;
import io.github.imsejin.file.model.Webtoon;

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

	public void createWebtoonList(String path, List<Webtoon> webtoonList)
			throws FileNotFoundException, IOException, ParseException {
		File file = null;
		XSSFWorkbook workbook = null;

		try {
			// Constitutes data in the excel file and returns instances to interact with next method.
			Object[] interactions = excelService.writer.create(path, webtoonList);
			file = (File) interactions[0];
			workbook = (XSSFWorkbook) interactions[1];

			// Receives instances from previous method, decorates each sheets and saves it.
			excelService.decorateWhenCreate(workbook, webtoonList);
			excelService.save(file, workbook);
		} finally {
			if (workbook != null) workbook.close();
		}
	}

	@SuppressWarnings("unchecked")
	public void updateWebtoonList(String path, String recentFileName, List<Webtoon> webtoonList)
			throws FileNotFoundException, IOException, InvalidFormatException, ParseException {
		FileInputStream fis = null;
		XSSFWorkbook workbook = null;
		List<Webtoon> previousList = null;
		String previousVersion = null;
		File file = null;

		try {
			// Reads data in the excel file and returns instances to interact with next method.
			Object[] interactions = excelService.reader.read(path, recentFileName);
			fis = (FileInputStream) interactions[0];
			workbook = (XSSFWorkbook) interactions[1];
			previousList = (List<Webtoon>) interactions[2];
			previousVersion = (String) interactions[3];

			// Receives instances from previous method, updates data in the excel file and returns instance to interact with next method.
			file = excelService.writer.update(path, previousList, webtoonList, previousVersion, fis, workbook);

			// Receives instances from previous method, decorates each sheets and saves it.
			excelService.decorateWhenUpdate(workbook, webtoonList);
			excelService.save(file, workbook);
		} finally {
			if (fis != null) fis.close();
			if (workbook != null) workbook.close();
		}
	}

}
