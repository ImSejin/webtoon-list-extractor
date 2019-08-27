package io.github.imsejin.excel.service;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * ExcelStyleService
 * 
 * @author SEJIN
 */
public final class ExcelStyleService {

	/**
	 * Adjusts column width to fit the content
	 * 
	 * @param sheet
	 * @param columnCount
	 */
	static void fitContent(Sheet sheet, int columnCount) {
		for (int i = 0; i < columnCount; i++) {
			sheet.autoSizeColumn(i);
		}
	}
	
	/**
	 * Increases row height to accommodate one and a half line of text
	 * 
	 * @param sheet
	 * @param row
	 * @param multiple
	 */
	static void increaseRowHeight(XSSFSheet sheet, XSSFRow row, double multiple) {
		row.setHeightInPoints((float) (sheet.getDefaultRowHeightInPoints() * multiple));
	}

	static XSSFCell decorateCell(XSSFCell cell, CellStyle style) {
		cell.setCellStyle(style);
		return cell;
	}

	static CellStyle align(CellStyle style) {
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);

		return style;
	}

	static CellStyle applyFont(Workbook workbook, CellStyle style, String fontName) {
		// Creates a new font and alter it.
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setFontName(fontName);
		font.setItalic(false);
		font.setStrikeout(false);

		// Sets the font
		style.setFont(font);

		return style;
	}

	static CellStyle drawBorder(CellStyle style) {
		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());

		return style;
	}

	static CellStyle dyeForegroundColor(CellStyle style) {
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return style;
	}

}
