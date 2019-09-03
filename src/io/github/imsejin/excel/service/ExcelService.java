package io.github.imsejin.excel.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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

	private static final ExcelHeader[] headers = ExcelHeader.values();
	private static final String[] metadataTitles = { "CURRENT_VERSION", "RECENT_UPDATE_DATE" };

	private static final String EXCEL_FILE_NAME = "webtoonList";
	private static final String OLD_EXCEL_FILE_EXTENSION = "xls";
	private static final String NEW_EXCEL_FILE_EXTENSION = "xlsx";

	private static final String DELIMITER_AUTHOR = ", ";

	/**
	 * The `SXSSFWorkbook` constructor that takes the `XSSFWorkbook` as param
	 * You cannot override or access the initial rows in the template file
	 * You must not use `new SXSSFWorkbook(new XSSFWorkbook(FileInputStream))`
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Object[] read(String path, String recentFileName) throws FileNotFoundException, IOException {
		Object[] results = new Object[4];
		File file = new File(path + File.separator + recentFileName + "." + NEW_EXCEL_FILE_EXTENSION);

		FileInputStream fis = new FileInputStream(file);
		Workbook workbook = new XSSFWorkbook(fis);

		results[0] = getData(workbook);
		results[1] = getVersion(workbook);
		results[2] = fis;
		results[3] = workbook;

		return results;
	}

	@SuppressWarnings("unchecked")
	public void write(Object list, String path) throws ClassCastException, IOException, FileNotFoundException, ParseException {
		List<Webtoon> webtoonsList = (List<Webtoon>) list;
		File file = new File(path + File.separator + EXCEL_FILE_NAME + "." + NEW_EXCEL_FILE_EXTENSION);

		try (FileOutputStream fos = new FileOutputStream(file); SXSSFWorkbook workbook = new SXSSFWorkbook()) {
			SXSSFSheet listSheet = createListSheet(workbook, webtoonsList);
			SXSSFSheet metadataSheet = createMetadataSheet(workbook, webtoonsList);
			createDatabaseSheet(workbook, webtoonsList);

			// Cleans up cells
			ExcelStyleService.makeColumnsFitContent(listSheet, headers.length);
			ExcelStyleService.hideExtraneousCells(listSheet, headers.length);
			ExcelStyleService.makeColumnsFitContent(metadataSheet, 2);
			ExcelStyleService.hideExtraneousCells(metadataSheet, 2);

			// Writes excel file
			workbook.write(fos);
		}
	}

	@SuppressWarnings("unchecked")
	public void write(Object previousList, Object presentList, String path, String version, FileInputStream fis, Workbook workbook)
			throws ClassCastException, IOException, FileNotFoundException, ParseException, InvalidFormatException {
		SXSSFWorkbook swb = new SXSSFWorkbook((XSSFWorkbook) workbook);
		List<Webtoon> dataList = (List<Webtoon>) previousList;
		List<Webtoon> webtoonsList = (List<Webtoon>) presentList;
		String suffix = getSuffix();
		File file = new File(path + File.separator + EXCEL_FILE_NAME + suffix + "." + NEW_EXCEL_FILE_EXTENSION);
		
		// TODO: database sheet를 update할 것
		try (FileOutputStream fos = new FileOutputStream(file)) {
			SXSSFSheet listSheet = updateListSheet(swb, dataList, webtoonsList);
			SXSSFSheet metadataSheet = updateMetadataSheet(swb, webtoonsList);
			updateDatabaseSheet(swb, dataList, webtoonsList);

			// Cleans up cells
			ExcelStyleService.makeColumnsFitContent(listSheet, headers.length);
			ExcelStyleService.hideExtraneousCells(listSheet, headers.length);
			ExcelStyleService.makeColumnsFitContent(metadataSheet, 2);
			ExcelStyleService.hideExtraneousCells(metadataSheet, 2);

			// Writes excel file
			swb.write(fos);
		} finally {
			fis.close();
			workbook.close();
			swb.close();
		}
	}
	
	private SXSSFSheet createListSheet(SXSSFWorkbook workbook, List<Webtoon> webtoonsList) {
		// Constitutes excel sheet
		CellStyle style;
		CellStyle styleOfImportationDate;
		SXSSFSheet sheet = workbook.createSheet("List");
		SXSSFRow row = sheet.createRow(0);

		// Creates decorated head row
		ExcelStyleService.increaseRowHeight(sheet, row, 1.5);
		style = ExcelStyleService.getHeaderCellStyle(workbook);
		for (int i = 0; i < headers.length; i++) {
			ExcelStyleService.decorateCell(row.createCell(i), style).setCellValue(headers[i].name());
		}

		// Creates decorated rows of content
		Webtoon webtoon;
		style = ExcelStyleService.getContentCellStyle(workbook);
		styleOfImportationDate = ExcelStyleService.getContentCellStyleWithAlignment(workbook);
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

		return sheet;
	}

	private SXSSFSheet updateListSheet(SXSSFWorkbook workbook, List<Webtoon> dataList, List<Webtoon> webtoonsList) {
		// Constitutes excel sheet
		CellStyle style;
		CellStyle styleOfImportationDate;
		
		// Removes the sheet for list
		workbook.removeSheetAt(0);
		SXSSFSheet sheet = workbook.createSheet("List");
		workbook.setSheetOrder("List", 0);
		SXSSFRow row = sheet.createRow(0);
		
		// Creates decorated head row
		ExcelStyleService.increaseRowHeight(sheet, row, 1.5);
		style = ExcelStyleService.getHeaderCellStyle(workbook);
		for (int i = 0; i < headers.length; i++) {
			ExcelStyleService.decorateCell(row.createCell(i), style).setCellValue(headers[i].name());
		}
		
		// Overwrites importation time in present list to importation time in previous list
		dataList.forEach(previous -> {
			String date = previous.getCreationTime();
			
			webtoonsList.forEach(present -> {
				boolean isTitleEqual = previous.getTitle().equals(present.getTitle());
				boolean isAuthorEqual = previous.getAuthor().toString().equals(present.getAuthor().toString());
				boolean isPlatformEqual = previous.getPlatform().equals(present.getPlatform());
				boolean isCompletionEqual = previous.isCompleted() == present.isCompleted();
				
				if (isTitleEqual && isAuthorEqual && isPlatformEqual && isCompletionEqual) {
					present.setCreationTime(date);
				}
			});
		});
		
		// Creates decorated rows of content
		Webtoon webtoon;
		style = ExcelStyleService.getContentCellStyle(workbook);
		styleOfImportationDate = ExcelStyleService.getContentCellStyleWithAlignment(workbook);
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
		
		return sheet;
	}

	private SXSSFSheet createMetadataSheet(SXSSFWorkbook workbook, List<Webtoon> webtoonsList) throws ParseException {
		// Constitutes excel sheet
		CellStyle titleStyle;
		CellStyle contentStyle;
		SXSSFSheet sheet = workbook.createSheet("Metadata");
		SXSSFRow row;

		// Puts importation date into list
		String[] metadataContents = new String[2];
		List<String> updateDateList = new ArrayList<>();
		for (Webtoon webtoon : webtoonsList) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = simpleDateFormat.parse(webtoon.getCreationTime());
			String updateDate = simpleDateFormat.format(date);
			updateDateList.add(updateDate);
		}

		// Calculates count of updates
		Set<String> set = new HashSet<>(updateDateList);
		int updateCount = set.size();
		metadataContents[0] = convertToVersion(updateCount);

		// Sorts out the latest importation date
		updateDateList.sort((date1, date2) -> {
			return date1.compareTo(date2);
		});
		metadataContents[1] = updateDateList.get(updateDateList.size() - 1);

		// Creates decorated title cells and content cells
		titleStyle = ExcelStyleService.getHeaderCellStyle(workbook);
		contentStyle = ExcelStyleService.getContentCellStyleWithAlignment(workbook);
		for (int i = 0; i < 2; i++) {
			row = sheet.createRow(i);
			ExcelStyleService.increaseRowHeight(sheet, row, 1.5);

			ExcelStyleService.decorateCell(row.createCell(0), titleStyle).setCellValue(metadataTitles[i]);
			ExcelStyleService.decorateCell(row.createCell(1), contentStyle).setCellValue(metadataContents[i]);
		}

		return sheet;
	}
	
	private SXSSFSheet updateMetadataSheet(SXSSFWorkbook workbook, List<Webtoon> webtoonsList) throws ParseException {
		// Constitutes excel sheet
		CellStyle titleStyle;
		CellStyle contentStyle;
		
		// Removes the sheet for metadata
		workbook.removeSheetAt(1);
		SXSSFSheet sheet = workbook.createSheet("Metadata");
		workbook.setSheetOrder("Metadata", 1);
		SXSSFRow row;

		// Puts importation date into list
		String[] metadataContents = new String[2];
		List<String> updateDateList = new ArrayList<>();
		for (Webtoon webtoon : webtoonsList) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = simpleDateFormat.parse(webtoon.getCreationTime());
			String updateDate = simpleDateFormat.format(date);
			updateDateList.add(updateDate);
		}

		// Calculates count of updates
		Set<String> set = new HashSet<>(updateDateList);
		int updateCount = set.size();
		metadataContents[0] = convertToVersion(updateCount);

		// Sorts out the latest importation date
		updateDateList.sort((date1, date2) -> {
			return date1.compareTo(date2);
		});
		metadataContents[1] = updateDateList.get(updateDateList.size() - 1);

		// Creates decorated title cells and content cells
		titleStyle = ExcelStyleService.getHeaderCellStyle(workbook);
		contentStyle = ExcelStyleService.getContentCellStyleWithAlignment(workbook);
		for (int i = 0; i < 2; i++) {
			row = sheet.createRow(i);
			ExcelStyleService.increaseRowHeight(sheet, row, 1.5);

			ExcelStyleService.decorateCell(row.createCell(0), titleStyle).setCellValue(metadataTitles[i]);
			ExcelStyleService.decorateCell(row.createCell(1), contentStyle).setCellValue(metadataContents[i]);
		}

		return sheet;
	}
	
	/*
	private void updateMetadata(XSSFWorkbook workbook, List<Webtoon> webtoonsList) throws ParseException {
		// Constitutes excel sheet
		XSSFSheet sheet = workbook.getSheet("Metadata");
		XSSFRow row;

		// Puts importation date into list
		String[] metadataContents = new String[2];
		List<String> updateDateList = new ArrayList<>();
		for (Webtoon webtoon : webtoonsList) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = simpleDateFormat.parse(webtoon.getCreationTime());
			String updateDate = simpleDateFormat.format(date);
			updateDateList.add(updateDate);
		}

		// Calculates count of updates
		Set<String> set = new HashSet<>(updateDateList);
		int updateCount = set.size();
		metadataContents[0] = convertToVersion(updateCount);

		// Sorts out the latest importation date
		updateDateList.sort((date1, date2) -> {
			return date1.compareTo(date2);
		});
		metadataContents[1] = updateDateList.get(updateDateList.size() - 1);

		// Updates metadata
		for (int i = 0; i < 2; i++) {
			row = sheet.getRow(i);
			row.getCell(1).setCellValue(metadataContents[i]);
		}
	}
	*/

	/**
	 * Creates sheet for database
	 * Do not use `ExcelStyleService.hideExtraneousRows` because it causes `Sheet.getLastRowNum` not to work
	 * 
	 * @param workbook
	 * @param webtoonsList
	 * @return
	 */
	private SXSSFSheet createDatabaseSheet(SXSSFWorkbook workbook, List<Webtoon> webtoonsList) {
		// Constitutes excel sheet
		SXSSFSheet sheet = workbook.createSheet("Database");
		SXSSFRow row;

		// Creates decorated rows of content
		Webtoon webtoon;
		for (int i = 0; i < webtoonsList.size(); i++) {
			webtoon = webtoonsList.get(i);
			row = sheet.createRow(i);

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

		return sheet;
	}
	
	private SXSSFSheet updateDatabaseSheet(SXSSFWorkbook workbook, List<Webtoon> dataList, List<Webtoon> webtoonsList) {
		// Removes the sheet for database
		workbook.removeSheetAt(2);
		SXSSFSheet sheet = workbook.createSheet("Database");
		workbook.setSheetOrder("Database", 2);
		SXSSFRow row;
		
		// Removes duplicated webtoons in present list
		List<Webtoon> dummy = new ArrayList<>(webtoonsList);
		dataList.forEach(previous -> {
			dummy.removeIf(present -> {
				boolean isTitleEqual = previous.getTitle().equals(present.getTitle());
				boolean isAuthorEqual = previous.getAuthor().toString().equals(present.getAuthor().toString());
				boolean isPlatformEqual = previous.getPlatform().equals(present.getPlatform());
				boolean isCompletionEqual = previous.isCompleted() == present.isCompleted();
				
				return isTitleEqual && isAuthorEqual && isPlatformEqual && isCompletionEqual;
			});
		});
		
		// Merges previous list with present list
		dataList.addAll(dummy);
		
		// Sorts the previous list
		dataList.sort(Comparator.comparing(Webtoon::getPlatform).thenComparing(Webtoon::getTitle));

		// Creates decorated rows of content
		Webtoon webtoon;
		for (int i = 0; i < dataList.size(); i++) {
			webtoon = dataList.get(i);
			row = sheet.createRow(i);

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

		return sheet;
	}

	private String convertToVersion(int updateCount) {
		final int intialVersionCode = 100; // It means 1.0.0
		String version = "";

		int count = updateCount - 1; // Why it subtracts 1 is starting point of version is 1.0.0
		String str = (intialVersionCode + count) + "";
		String[] arr = str.split("");
		for (int i = 0; i < arr.length; i++) {
			version += arr[i];
			if (i < arr.length - 1) {
				version += ".";
			}
		}

		return version;
	}
	
	private List<Webtoon> getData(Workbook workbook) {
		// Reads the sheet for database
		List<Webtoon> webtoonsList = new ArrayList<>();
		Sheet sheet = workbook.getSheet("Database");
		Row row;
		
		for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);

			// Reads value of the cells
			String platform = row.getCell(0).getStringCellValue();
			String title = row.getCell(1).getStringCellValue();
			List<String> author = Arrays.asList(row.getCell(2).getStringCellValue().split(DELIMITER_AUTHOR));
			boolean completed = row.getCell(3).getBooleanCellValue();
			String creationTime = row.getCell(4).getStringCellValue();

			// Sets the value into webtoon
			Webtoon webtoon = new Webtoon();
			webtoon.setPlatform(platform);
			webtoon.setTitle(title);
			webtoon.setAuthor(author);
			webtoon.setCompleted(completed);
			webtoon.setCreationTime(creationTime);

			// Puts the webtoon into list
			webtoonsList.add(webtoon);
		}

		return webtoonsList;
	}

	private String getVersion(Workbook workbook) throws FileNotFoundException, IOException {
		String version = "";

		try {
			Sheet sheet = workbook.getSheet("Metadata");
			Row row = sheet.getRow(0);
			Cell cell = row.getCell(1);

			version = cell.getStringCellValue();
		} catch (NullPointerException | IllegalArgumentException e) {
			System.out.println();
			e.printStackTrace();
			System.out.println(": Metadata is corrupted.");
			System.out.println(e.getMessage());
		}

		return version;
	}

	private String getSuffix() {
		String now = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
		return "_" + now;
	}

}
