package io.github.imsejin.excel.service;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import io.github.imsejin.console.action.ConsoleAction;
import io.github.imsejin.console.model.WorkingProcess;

/**
 * ExcelStyleService
 * 
 * @author SEJIN
 */
public final class ExcelStyleService {

	private final static String HEADER_FONT_NAME = "NanumGothic";
	private final static String CONTENT_FONT_NAME = "NanumGothic Light";

	private final static int OLD_MAX_COUNT_OF_ROWS = 65536;
	private final static int OLD_MAX_COUNT_OF_COLUMNS = 256;
	private final static int NEW_MAX_COUNT_OF_ROWS = 1048576;
	private final static int NEW_MAX_COUNT_OF_COLUMNS = 16384;
	
	private ExcelStyleService() {}

	/**
	 * Adjusts column width to fit the content
	 * 
	 * @param workbook
	 * @param Count of columns
	 */
	static void makeColumnsFitContent(SXSSFWorkbook workbook) {
		SXSSFSheet sheet;
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			sheet = workbook.getSheetAt(i);
			int columnsCount = sheet.getRow(0).getLastCellNum();
			for (int j = 0; j < columnsCount; j++) {
				sheet.trackColumnForAutoSizing(j);
				sheet.autoSizeColumn(j);
			}
		}
	}

	static void hideExtraneousCells(SXSSFWorkbook workbook) {
		SXSSFSheet sheet;
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			sheet = workbook.getSheetAt(i);
			int rowsCount = sheet.getPhysicalNumberOfRows();
			int columnsCount = sheet.getRow(0).getLastCellNum();
			
			hideExtraneousRows(sheet, rowsCount);
			hideExtraneousColumns(sheet, columnsCount);
		}
	}
	
	static void hideExtraneousRows(SXSSFSheet sheet, int rowsCount) {
		WorkingProcess process = new WorkingProcess();
		process.setMessage("Hiding rows");
		process.setTotalProcess(NEW_MAX_COUNT_OF_ROWS);
		ConsoleAction.print(process);

		for (int i = rowsCount; i < NEW_MAX_COUNT_OF_ROWS; i++) {
			process.setCurrentProcess(i);
			
			Row row = sheet.createRow(i);
			row.setZeroHeight(true);
		}
	}
	
	static void hideExtraneousColumns(SXSSFSheet sheet, int columnsCount) {
		WorkingProcess process = new WorkingProcess();
		process.setMessage("Hiding columns");
		process.setTotalProcess(NEW_MAX_COUNT_OF_COLUMNS);
		ConsoleAction.print(process);

		for (int i = columnsCount; i < NEW_MAX_COUNT_OF_COLUMNS; i++) {
			process.setCurrentProcess(i);
			
			sheet.setColumnHidden(i, true);
		}
	}
	
	/**
	 * Increases row height to accommodate one and a half line of text
	 * 
	 * @param sheet
	 * @param row
	 * @param multiples
	 */
	static void increaseRowHeight(SXSSFSheet sheet, SXSSFRow row, double multiples) {
		row.setHeightInPoints((float) (sheet.getDefaultRowHeightInPoints() * multiples));
	}

	/**
	 * Applies the style into cell
	 * 
	 * @param cell
	 * @param style
	 * @return the decorated cell
	 */
	static SXSSFCell decorateCell(SXSSFCell cell, CellStyle style) {
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

	static CellStyle getHeaderCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		align(style);
		applyFont(workbook, style, HEADER_FONT_NAME);
		drawBorder(style);
		dyeForegroundColor(style);

		return style;
	}

	static CellStyle getContentCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		applyFont(workbook, style, CONTENT_FONT_NAME);
		drawBorder(style);

		return style;
	}

	static CellStyle getImportationDateCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		align(style);
		applyFont(workbook, style, CONTENT_FONT_NAME);
		drawBorder(style);

		return style;
	}

}
