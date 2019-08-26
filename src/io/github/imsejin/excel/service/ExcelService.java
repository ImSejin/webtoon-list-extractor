package io.github.imsejin.excel.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import io.github.imsejin.excel.model.ExcelHeader;

/**
 * ExcelService
 * 
 * @author SEJIN
 */
public class ExcelService {

	private static final String OLD_EXCEL_FILE_EXTENSION = "xls";

	private static final String NEW_EXCEL_FILE_EXTENSION = "xlsx";

	@SuppressWarnings("unchecked")
	public <T> void writeWebtoonsList(Object list, String path, Class<T> clazz) throws ClassCastException, IOException {
		List<T> webtoonsList = (List<T>) list;
		File file = new File(path);

		try (FileOutputStream fos = new FileOutputStream(file); XSSFWorkbook workbook = new XSSFWorkbook()) {
			// Constitutes excel file
			XSSFSheet sheet = workbook.createSheet();
			XSSFRow row = sheet.createRow(0);
			XSSFCell cell;

			// Constitutes header
			ExcelHeader[] headers = ExcelHeader.values();
			for (int i = 0; i < headers.length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(headers[i].name());
			}
			
			// Creates rows of webtoons information
			T webtoon;
			for (int i = 0; i < webtoonsList.size(); i++) {
				webtoon = webtoonsList.get(i);
				
				row = sheet.createRow(i);
			}

		} catch (IOException e) {

		}
	}

}
