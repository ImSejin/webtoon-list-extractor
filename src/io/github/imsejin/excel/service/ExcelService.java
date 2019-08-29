package io.github.imsejin.excel.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import io.github.imsejin.excel.model.ExcelHeader;
import io.github.imsejin.file.model.Webtoon;

/**
 * ExcelService
 * 
 * @author SEJIN
 */
public class ExcelService {

	private static final String EXCEL_FILE_NAME = "webtoonListss";

	private static final String OLD_EXCEL_FILE_EXTENSION = "xls";

	private static final String NEW_EXCEL_FILE_EXTENSION = "xlsx";

	@SuppressWarnings("unchecked")
	public void writeWebtoonsList(Object list, String path) throws ClassCastException, IOException, FileNotFoundException {
		List<Webtoon> webtoonsList = (List<Webtoon>) list;
		File file = new File(path + File.separator + EXCEL_FILE_NAME + "." + NEW_EXCEL_FILE_EXTENSION);

		try (FileOutputStream fos = new FileOutputStream(file); SXSSFWorkbook workbook = new SXSSFWorkbook()) {
			// Constitutes excel file
			CellStyle style;
			CellStyle styleOfImportationDate;
			SXSSFSheet sheet = workbook.createSheet("Webtoons");
			SXSSFRow row = sheet.createRow(0);
			ExcelHeader[] headers = ExcelHeader.values();

			// Creates decorated head row
			ExcelStyleService.increaseRowHeight(sheet, row, 1.5);
			style = ExcelStyleService.getHeaderCellStyle(workbook);
			for (int i = 0; i < headers.length; i++) {
				ExcelStyleService.decorateCell(row.createCell(i), style).setCellValue(headers[i].name());
			}

			// Creates decorated rows of content
			Webtoon webtoon;
			style = ExcelStyleService.getContentCellStyle(workbook);
			styleOfImportationDate = ExcelStyleService.getImportationDateCellStyle(workbook);
			for (int i = 0; i < webtoonsList.size(); i++) {
				webtoon = webtoonsList.get(i);
				row = sheet.createRow(i + 1);

				// Stringizes author
				List<String> authors = webtoon.getAuthor();
				StringBuffer author = new StringBuffer();
				for (int j = 0; j < authors.size(); j++) {
					String person = authors.get(j);
					author.append(person);
					if (j < authors.size() - 1) {
						author.append(", ");
					}
				}

				// Sets the data of webtoon into cell
				ExcelStyleService.decorateCell(row.createCell(0), style).setCellValue(webtoon.getPlatform());
				ExcelStyleService.decorateCell(row.createCell(1), style).setCellValue(webtoon.getTitle());
				ExcelStyleService.decorateCell(row.createCell(2), style).setCellValue(author.toString());
				ExcelStyleService.decorateCell(row.createCell(3), style).setCellValue(webtoon.isCompleted());
				ExcelStyleService.decorateCell(row.createCell(4), styleOfImportationDate).setCellValue(webtoon.getCreationTime());
			}
			
			ExcelStyleService.makeColumnsFitContent(workbook);
			ExcelStyleService.hideExtraneousCells(workbook);
			
			// Writes excel file
			workbook.write(fos);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
