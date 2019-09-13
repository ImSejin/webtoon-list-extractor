package io.github.imsejin.excel.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import io.github.imsejin.base.model.Constants;
import io.github.imsejin.console.action.ConsoleAction;
import io.github.imsejin.console.model.WorkingProcess;
import io.github.imsejin.excel.model.ListHeader;
import io.github.imsejin.excel.model.MetadataHeader;
import io.github.imsejin.file.model.Webtoon;
import io.github.imsejin.file.service.FileService;

/**
 * ExcelStyleUtil
 * 
 * @author SEJIN
 */
public final class ExcelStyleUtil {

	private ExcelStyleUtil() {}

	/**
	 * Adjusts width of columns in sheet for list to fit the content.
	 * 
	 * @param sheet, webtoonList
	 */
	public static void makeColumnsFitContent(Sheet sheet, List<Webtoon> webtoonList) {
		// Gets lengths of the header string in sheet for list.
		ListHeader[] listHeaders = ListHeader.values();
		List<Integer> lengthsOfListHeader = new ArrayList<>();
		for (ListHeader header : listHeaders) {
			int length = header.name().getBytes().length;
			lengthsOfListHeader.add(length);
		}

		// Gets lengths of the content string.
		List<Integer> maxLengthsInContent = new ArrayList<>();
		int maxLengthInFirstColumn = lengthsOfListHeader.get(0);
		int maxLengthInSecondColumn = lengthsOfListHeader.get(1);
		int maxLengthInThirdColumn = lengthsOfListHeader.get(2);
		int maxLengthInFourthColumn = lengthsOfListHeader.get(3); // Fourth column type is boolean.
		int maxLengthInFifthColumn = lengthsOfListHeader.get(4);
		for (Webtoon webtoon : webtoonList) {
			int temp1 = webtoon.getPlatform().getBytes().length;
			if (maxLengthInFirstColumn < temp1) {
				maxLengthInFirstColumn = temp1;
			}

			int temp2 = webtoon.getTitle().getBytes().length;
			if (maxLengthInSecondColumn < temp2) {
				maxLengthInSecondColumn = temp2;
			}

			int temp3 = FileService.convertAuthor(webtoon.getAuthor()).getBytes().length;
			if (maxLengthInThirdColumn < temp3) {
				maxLengthInThirdColumn = temp3;
			}

			// The content in fourth column is fixed.
			maxLengthInFourthColumn = maxLengthInFourthColumn < 5 ? 5 : maxLengthInFourthColumn;

			int temp5 = webtoon.getCreationTime().getBytes().length;
			if (maxLengthInFifthColumn < temp5) {
				maxLengthInFifthColumn = temp5;
			}
		}

		maxLengthsInContent.add(maxLengthInFirstColumn);
		maxLengthsInContent.add(maxLengthInSecondColumn);
		maxLengthsInContent.add(maxLengthInThirdColumn);
		maxLengthsInContent.add(maxLengthInFourthColumn);
		maxLengthsInContent.add(maxLengthInFifthColumn);

		// Sets the width of columns.
		// One character needs 256 bytes.
		final int padding = 256 * 6;
		final double compensatory = 0.7;
		sheet.setColumnWidth(0, (int) (maxLengthsInContent.get(0) * 256 + padding));
		sheet.setColumnWidth(1, (int) (maxLengthsInContent.get(1) * 256 * compensatory));
		sheet.setColumnWidth(2, (int) (maxLengthsInContent.get(2) * 256 * compensatory));
		sheet.setColumnWidth(3, (int) (maxLengthsInContent.get(3) * 256 + padding));
		sheet.setColumnWidth(4, (int) (maxLengthsInContent.get(4) * 256 + padding));
	}

	/**
	 * Adjusts width of columns in sheet for metadata to fit the content.
	 * 
	 * @param sheet
	 */
	public static void makeColumnsFitContent(Sheet sheet) {
		// Gets lengths of the header string in sheet for metadata.
		MetadataHeader[] metadataHeaders = MetadataHeader.values();
		int maxLengthInMetadataHeader = 0;
		for (MetadataHeader header : metadataHeaders) {
			int length = header.name().getBytes().length;
			if (maxLengthInMetadataHeader < length) {
				maxLengthInMetadataHeader = length;
			}
		}

		// Sets the width of columns.
		final int padding = 256 * 8;
		sheet.setColumnWidth(0, (int) (maxLengthInMetadataHeader * 256 + padding));
		sheet.setColumnWidth(1, (int) (maxLengthInMetadataHeader * 256 + padding));
	}

	/*
	/// DISCOVERED BUG THAT IT DOES NOT ADJUST WIDTH OF COLUMNS ENOUGH. ///
	public static void makeColumnsFitContent(SXSSFSheet sheet, int columnsCount) {
		for (int i = 0; i < columnsCount; i++) {
			sheet.trackColumnForAutoSizing(i);
			sheet.autoSizeColumn(i);
		}
	}
	*/

	/**
	 * Hides extraneous rows and columns in the sheet.
	 * 
	 * @param sheet
	 */
	public static void hideExtraneousCells(Sheet sheet) {
		hideExtraneousRows(sheet);
		hideExtraneousColumns(sheet);
	}

	public static void hideExtraneousRows(Sheet sheet) {
		int numberOfRows = sheet.getPhysicalNumberOfRows();

		// Sets up printing console logs.
		WorkingProcess process = new WorkingProcess();
		process.setMessage("Hiding rows");
		process.setTotalProcess(Constants.excel.NEW_MAX_COUNT_OF_ROWS - numberOfRows);
		ConsoleAction.print(process);

		for (int i = numberOfRows; i < Constants.excel.NEW_MAX_COUNT_OF_ROWS; i++) {
			process.setCurrentProcess(i);

			Row row = sheet.createRow(i);
			row.setZeroHeight(true);
		}
	}

	public static void hideExtraneousColumns(Sheet sheet) {
		int numberOfColumns = sheet.getRow(0).getLastCellNum();

		// Sets up printing console logs.
		WorkingProcess process = new WorkingProcess();
		process.setMessage("Hiding columns");
		process.setTotalProcess(Constants.excel.NEW_MAX_COUNT_OF_COLUMNS - numberOfColumns);
		ConsoleAction.print(process);

		for (int i = numberOfColumns; i < Constants.excel.NEW_MAX_COUNT_OF_COLUMNS; i++) {
			process.setCurrentProcess(i);

			sheet.setColumnHidden(i, true);
		}
	}
	
	/**
	 * Removes all rows in the sheet except for header before updating the sheet for list.
	 * 
	 * @param sheet
	 */
	public static void removeAllRows(XSSFSheet sheet) {
		int numberOfRows = sheet.getPhysicalNumberOfRows();
		
		// Sets up printing console logs.
		WorkingProcess process = new WorkingProcess();
		process.setMessage("Removing rows");
		process.setTotalProcess(numberOfRows - 1);
		ConsoleAction.print(process);

		for (int i = 1; i < numberOfRows; i++) {
			process.setCurrentProcess(i);

			XSSFRow row = sheet.getRow(i);
			sheet.removeRow(row);
		}
	}

	/**
	 * Increases row height to accommodate one and a half line of text
	 * 
	 * @param sheet, row, multiples
	 */
	public static void increaseRowHeight(XSSFSheet sheet, XSSFRow row, double multiples) {
		row.setHeightInPoints((float) (sheet.getDefaultRowHeightInPoints() * multiples));
	}

	/**
	 * Resets row height to default value
	 * 
	 * @param sheet
	 */
	public static void initializeRowHeight(XSSFSheet sheet) {
		XSSFRow row;
		for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			row.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
		}
	}

	/**
	 * Returns the decorated cell.
	 * 
	 * @param cell, style
	 * @return the decorated cell
	 */
	public static XSSFCell decorateCell(XSSFCell cell, CellStyle style) {
		cell.setCellStyle(style);
		return cell;
	}

	private static CellStyle alignHorizontally(CellStyle style) {
		style.setAlignment(HorizontalAlignment.CENTER);

		return style;
	}

	private static CellStyle alignVertically(CellStyle style) {
		style.setVerticalAlignment(VerticalAlignment.CENTER);

		return style;
	}

	private static CellStyle applyFont(Workbook workbook, CellStyle style, String fontName) {
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

	private static CellStyle drawBorder(CellStyle style) {
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

	private static CellStyle dyeForegroundColor(CellStyle style) {
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return style;
	}

	public static CellStyle getHeaderCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		alignHorizontally(style);
		alignVertically(style);
		applyFont(workbook, style, Constants.excel.HEADER_FONT_NAME);
		drawBorder(style);
		dyeForegroundColor(style);

		return style;
	}

	public static CellStyle getContentCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		alignVertically(style);
		applyFont(workbook, style, Constants.excel.CONTENT_FONT_NAME);
		drawBorder(style);

		return style;
	}

	public static CellStyle getContentCellStyleWithAlignment(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		alignHorizontally(style);
		alignVertically(style);
		applyFont(workbook, style, Constants.excel.CONTENT_FONT_NAME);
		drawBorder(style);

		return style;
	}

}
