package io.github.imsejin.excel.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

		try (FileOutputStream fos = new FileOutputStream(file); XSSFWorkbook workbook = new XSSFWorkbook()) {
			// Constitutes excel file
			CellStyle style = workbook.createCellStyle();
			XSSFSheet sheet = workbook.createSheet("Webtoons");
			XSSFRow row = sheet.createRow(0);

			// Constitutes header
			ExcelHeader[] headers = ExcelHeader.values();
			for (int i = 0; i < headers.length; i++) {
				dyeCell(workbook, alignCell(workbook, row.createCell(i), style), style).setCellValue(headers[i].name());
			}

			// Creates rows of webtoons information
			Webtoon webtoon;
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
				row.createCell(0).setCellValue(webtoon.getPlatform());
				row.createCell(1).setCellValue(webtoon.getTitle());
				row.createCell(2).setCellValue(author.toString());
				row.createCell(3).setCellValue(webtoon.isCompleted());
				row.createCell(4).setCellValue(webtoon.getCreationTime());
			}

			// Writes excel file
			workbook.write(fos);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private XSSFCell alignCell(XSSFWorkbook workbook, XSSFCell cell, CellStyle style) {
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		cell.setCellStyle(style);

		return cell;
	}

	private XSSFCell dyeCell(XSSFWorkbook workbook, XSSFCell cell, CellStyle style) {
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cell.setCellStyle(style);

		return cell;
	}

}
