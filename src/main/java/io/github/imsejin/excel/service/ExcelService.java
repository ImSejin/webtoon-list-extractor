package io.github.imsejin.excel.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import io.github.imsejin.common.Constants;
import io.github.imsejin.common.util.GeneralUtil;
import io.github.imsejin.excel.model.ListHeader;
import io.github.imsejin.excel.model.MetadataHeader;
import io.github.imsejin.excel.util.ExcelStyleUtil;
import io.github.imsejin.file.model.Webtoon;

/**
 * ExcelService
 * 
 * @author SEJIN
 */
public class ExcelService {

	private static final ListHeader[] listHeaders = ListHeader.values();
	private static final MetadataHeader[] metadataHeaders = MetadataHeader.values();

	public final ExcelService.reader reader;
	public final ExcelService.writer writer;

	private final String suffix;

	public ExcelService() {
		this.reader = new reader();
		this.writer = new writer();
		this.suffix = getSuffix();
	}
	
	public class reader {

		private reader() {}

		/**
		 * The `SXSSFWorkbook` constructor that takes the `XSSFWorkbook` as param.
		 * You cannot override or access the initial rows in the template file.
		 * You must not use `new SXSSFWorkbook(new XSSFWorkbook(FileInputStream))`.
		 * 
		 * @param path, recentFileName
		 * @return FileInputStream, XSSFWorkbook, List<Webtoon>, String
		 * @throws FileNotFoundException, IOException
		 */
		public Object[] read(String path, String recentFileName) throws FileNotFoundException, IOException {
			Object[] interactions = new Object[4];
			File file = new File(path + File.separator + recentFileName + "." + Constants.file.NEW_EXCEL_FILE_EXTENSION);
			
			// Do not close these yet.
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			
			interactions[0] = fis;
			interactions[1] = workbook;
			interactions[2] = getData(workbook);
			interactions[3] = getVersion(workbook);
			
			return interactions;
		}
		
		private List<Webtoon> getData(Workbook workbook) {
			// Reads the sheet for database.
			List<Webtoon> webtoonsList = new ArrayList<>();
			Sheet sheet = workbook.getSheet(Constants.excel.SHEET_NAME_DATABASE);
			Row row;
			
			for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
				row = sheet.getRow(i);

				// Reads value of the cells.
				String platform = row.getCell(0).getStringCellValue();
				String title = row.getCell(1).getStringCellValue();
				List<String> author = GeneralUtil.convertAuthor(row.getCell(2).getStringCellValue());
				boolean completed = row.getCell(3).getBooleanCellValue();
				String creationTime = row.getCell(4).getStringCellValue();

				// Sets the value into webtoon.
				Webtoon webtoon = Webtoon.builder()
				        .platform(platform)
				        .title(title)
				        .author(author)
				        .isCompleted(completed)
				        .creationTime(creationTime)
				        .build();

				// Puts the webtoon into list.
				webtoonsList.add(webtoon);
			}

			return webtoonsList;
		}

		private String getVersion(Workbook workbook) throws FileNotFoundException, IOException {
			String version = "";

			try {
				Sheet sheet = workbook.getSheet(Constants.excel.SHEET_NAME_METADATA);
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
		
	}
	
	public class writer {

		private writer() {}

		public Object[] create(String path, List<Webtoon> webtoonList) throws FileNotFoundException, IOException, ParseException {
			Object[] interactions = new Object[2];
			File file = new File(path + File.separator + Constants.file.EXCEL_FILE_NAME + "." + Constants.file.NEW_EXCEL_FILE_EXTENSION);
			XSSFWorkbook workbook = new XSSFWorkbook();

			createListSheet(workbook, webtoonList);
			createMetadataSheet(workbook, webtoonList);
			createDatabaseSheet(workbook, webtoonList);

			interactions[0] = file;
			interactions[1] = workbook;

			return interactions;
		}

		private void createListSheet(XSSFWorkbook workbook, List<Webtoon> webtoonList) {
			// Constitutes excel sheet.
			XSSFSheet sheet = workbook.createSheet(Constants.excel.SHEET_NAME_LIST);
			XSSFRow row = sheet.createRow(0);

			// Creates decorated head row.
			ExcelStyleUtil.increaseRowHeight(sheet, row, 1.5);
			CellStyle styleOfHeader = ExcelStyleUtil.getHeaderCellStyle(workbook);
			for (int i = 0; i < listHeaders.length; i++) {
				ExcelStyleUtil.decorateCell(row.createCell(i), styleOfHeader).setCellValue(listHeaders[i].name());
			}

			// Creates decorated rows of content.
			Webtoon webtoon;
			CellStyle styleOfContent = ExcelStyleUtil.getContentCellStyle(workbook);
			CellStyle styleOfImportationDate = ExcelStyleUtil.getContentCellStyleWithAlignment(workbook);
			for (int i = 0; i < webtoonList.size(); i++) {
				webtoon = webtoonList.get(i);
				row = sheet.createRow(i + 1);

				// Sets the data of webtoon into cell.
				ExcelStyleUtil.decorateCell(row.createCell(0), styleOfContent).setCellValue(webtoon.getPlatform());
				ExcelStyleUtil.decorateCell(row.createCell(1), styleOfContent).setCellValue(webtoon.getTitle());
				ExcelStyleUtil.decorateCell(row.createCell(2), styleOfContent).setCellValue(GeneralUtil.convertAuthor(webtoon.getAuthor()));
				ExcelStyleUtil.decorateCell(row.createCell(3), styleOfContent).setCellValue(webtoon.isCompleted());
				ExcelStyleUtil.decorateCell(row.createCell(4), styleOfImportationDate).setCellValue(webtoon.getCreationTime());
			}
		}
		
		private void createMetadataSheet(XSSFWorkbook workbook, List<Webtoon> webtoonList) throws ParseException {
			// Constitutes excel sheet.
			XSSFSheet sheet = workbook.createSheet(Constants.excel.SHEET_NAME_METADATA);
			XSSFRow row;
			
			// Gets calculated metadata.
			String[] metadataContents = calculateMetadata(webtoonList);
						
			// Creates decorated title cells and content cells.
			CellStyle titleStyle = ExcelStyleUtil.getHeaderCellStyle(workbook);
			CellStyle contentStyle = ExcelStyleUtil.getContentCellStyleWithAlignment(workbook);
			for (int i = 0; i < 2; i++) {
				row = sheet.createRow(i);
				ExcelStyleUtil.increaseRowHeight(sheet, row, 1.5);

				ExcelStyleUtil.decorateCell(row.createCell(0), titleStyle).setCellValue(metadataHeaders[i].name());
				ExcelStyleUtil.decorateCell(row.createCell(1), contentStyle).setCellValue(metadataContents[i]);
			}
		}
		
		/**
		 * Creates sheet for database.
		 * Do not use `ExcelStyleUtil.hideExtraneousRows`
		 * because it causes `Sheet.getLastRowNum` not to work.
		 * 
		 * @param workbook
		 * @param webtoonsList
		 * @return
		 */
		private void createDatabaseSheet(XSSFWorkbook workbook, List<Webtoon> webtoonList) {
			// Constitutes excel sheet.
			XSSFSheet sheet = workbook.createSheet(Constants.excel.SHEET_NAME_DATABASE);
			XSSFRow row;

			// Creates decorated rows of content.
			Webtoon webtoon;
			for (int i = 0; i < webtoonList.size(); i++) {
				webtoon = webtoonList.get(i);
				row = sheet.createRow(i);

				// Sets the data of webtoon into cell.
				row.createCell(0).setCellValue(webtoon.getPlatform());
				row.createCell(1).setCellValue(webtoon.getTitle());
				row.createCell(2).setCellValue(GeneralUtil.convertAuthor(webtoon.getAuthor()));
				row.createCell(3).setCellValue(webtoon.isCompleted());
				row.createCell(4).setCellValue(webtoon.getCreationTime());
			}
		}
		
		public File update(String path, List<Webtoon> previousList, List<Webtoon> presentList,
				String version, FileInputStream fis, XSSFWorkbook workbook)
				throws IOException, FileNotFoundException, ParseException, InvalidFormatException {
			File file = new File(path + File.separator + Constants.file.EXCEL_FILE_NAME + suffix + "." + Constants.file.NEW_EXCEL_FILE_EXTENSION);
			
			updateListSheet(workbook, previousList, presentList);
			updateMetadataSheet(workbook, presentList);
			updateDatabaseSheet(workbook, previousList, presentList);
			
			return file;
		}
		
		private void updateListSheet(XSSFWorkbook workbook, List<Webtoon> previousList, List<Webtoon> presentList) {
			XSSFSheet sheet = workbook.getSheet(Constants.excel.SHEET_NAME_LIST);
			XSSFRow row;

			// Overwrites importation time in present list to importation time in previous list.
			overwriteCreationTime(previousList, presentList);
			
			// Removes all rows except for header.
			ExcelStyleUtil.removeAllRows(sheet);
			
			// Creates decorated rows of content.
			Webtoon webtoon;
			CellStyle styleOfContent = ExcelStyleUtil.getContentCellStyle(workbook);
			CellStyle styleOfImportationDate = ExcelStyleUtil.getContentCellStyleWithAlignment(workbook);
			for (int i = 0; i < presentList.size(); i++) {
				webtoon = presentList.get(i);
				row = sheet.createRow(i + 1);

				// Sets the data of webtoon into cell.
				ExcelStyleUtil.decorateCell(row.createCell(0), styleOfContent).setCellValue(webtoon.getPlatform());
				ExcelStyleUtil.decorateCell(row.createCell(1), styleOfContent).setCellValue(webtoon.getTitle());
				ExcelStyleUtil.decorateCell(row.createCell(2), styleOfContent).setCellValue(GeneralUtil.convertAuthor(webtoon.getAuthor()));
				ExcelStyleUtil.decorateCell(row.createCell(3), styleOfContent).setCellValue(webtoon.isCompleted());
				ExcelStyleUtil.decorateCell(row.createCell(4), styleOfImportationDate).setCellValue(webtoon.getCreationTime());
			}
		}

		private void updateMetadataSheet(XSSFWorkbook workbook, List<Webtoon> presentList) throws ParseException {
			XSSFSheet sheet = workbook.getSheet(Constants.excel.SHEET_NAME_METADATA);
			XSSFRow row;
			XSSFCell cell;
			
			// Gets calculated metadata.
			String[] metadataContents = calculateMetadata(presentList);
			
			// Updates metadata.
			for (int i = 0; i < 2; i++) {
				row = sheet.getRow(i);
				cell = row.getCell(1);
				cell.setCellValue(metadataContents[i]);
			}
		}
		
		private void updateDatabaseSheet(XSSFWorkbook workbook, List<Webtoon> previousList, List<Webtoon> presentList) {
			// Removes the sheet for database.
			workbook.removeSheetAt(2);
			XSSFSheet sheet = workbook.createSheet(Constants.excel.SHEET_NAME_DATABASE);
			workbook.setSheetOrder(Constants.excel.SHEET_NAME_DATABASE, 2);
			XSSFRow row;
			
			// Removes duplicated webtoons in present list.
			List<Webtoon> dummy = new ArrayList<>(presentList);
			previousList.forEach(previous -> {
				dummy.removeIf(present -> {
					boolean isTitleEqual = previous.getTitle().equals(present.getTitle());
					boolean isAuthorEqual = GeneralUtil.convertAuthor(previous.getAuthor()).equals(GeneralUtil.convertAuthor(present.getAuthor()));
					boolean isPlatformEqual = previous.getPlatform().equals(present.getPlatform());
					boolean isCompletionEqual = previous.isCompleted() == present.isCompleted();
					
					return isTitleEqual && isAuthorEqual && isPlatformEqual && isCompletionEqual;
				});
			});
			
			// Merges previous list with new webtoons in present list.
			previousList.addAll(dummy);
			
			// Sorts the previous list.
			previousList.sort(Comparator.comparing(Webtoon::getPlatform).thenComparing(Webtoon::getTitle));

			// Creates decorated rows of content.
			Webtoon webtoon;
			for (int i = 0; i < previousList.size(); i++) {
				webtoon = previousList.get(i);
				row = sheet.createRow(i);

				// Sets the data of webtoon into cell.
				row.createCell(0).setCellValue(webtoon.getPlatform());
				row.createCell(1).setCellValue(webtoon.getTitle());
				row.createCell(2).setCellValue(GeneralUtil.convertAuthor(webtoon.getAuthor()));
				row.createCell(3).setCellValue(webtoon.isCompleted());
				row.createCell(4).setCellValue(webtoon.getCreationTime());
			}
		}

		private void overwriteCreationTime(List<Webtoon> fromList, List<Webtoon> toList) {
			fromList.forEach(from -> {
				String time = from.getCreationTime();

				toList.forEach(to -> {
					boolean isTitleEqual = from.getTitle().equals(to.getTitle());
					boolean isAuthorEqual = GeneralUtil.convertAuthor(from.getAuthor()).equals(GeneralUtil.convertAuthor(to.getAuthor()));
					boolean isPlatformEqual = from.getPlatform().equals(to.getPlatform());
					boolean isCompletionEqual = from.isCompleted() == to.isCompleted();

					if (isTitleEqual && isAuthorEqual && isPlatformEqual && isCompletionEqual) {
						to.setCreationTime(time);
					}
				});
			});
		}

		private String[] calculateMetadata(List<Webtoon> webtoonList) throws ParseException {
			String[] metadataContents = new String[2];
			
			// Puts importation date into list.
			List<String> updateDateList = new ArrayList<>();
			for (Webtoon webtoon : webtoonList) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = simpleDateFormat.parse(webtoon.getCreationTime());
				String updateDate = simpleDateFormat.format(date);
				updateDateList.add(updateDate);
			}

			// Calculates count of importing webtoons.
			Set<String> set = new HashSet<>(updateDateList);
			int updateCount = set.size();
			metadataContents[0] = calculateVersion(updateCount);

			// Sorts out the latest importation date.
			if (updateDateList.size() > 1) {
				// The number of webtoons is greater than or equal to 2.
				updateDateList.sort((date1, date2) -> {
					return date1.compareTo(date2);
				});
				metadataContents[1] = updateDateList.get(updateDateList.size() - 1);
			} else if (updateDateList.size() > 0) {
				// The number of webtoon is 1.
				metadataContents[1] = updateDateList.get(0);
			} else {
				// No webtoon exists in the path.
				metadataContents[1] = "-";
			}
			
			return metadataContents;
		}
		
		private String calculateVersion(int updateCount) {
			final int intialVersionCode = 100; // It means `1.0.0`.
			String version = "";

			int count = (updateCount > 1 ? updateCount - 1 : 0); // Why it subtracts 1 is starting point of version is `1.0.0`.
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

	}
	
	public void decorateWhenCreate(XSSFWorkbook workbook, List<Webtoon> webtoonList) {
		XSSFSheet listSheet = workbook.getSheet(Constants.excel.SHEET_NAME_LIST);
		XSSFSheet metadataSheet = workbook.getSheet(Constants.excel.SHEET_NAME_METADATA);

		ExcelStyleUtil.makeColumnsFitContent(listSheet, webtoonList);
		ExcelStyleUtil.makeColumnsFitContent(metadataSheet);
		ExcelStyleUtil.hideExtraneousRows(listSheet);
		ExcelStyleUtil.hideExtraneousRows(metadataSheet);
		ExcelStyleUtil.hideExtraneousColumns(listSheet);
		ExcelStyleUtil.hideExtraneousColumns(metadataSheet);
	}

	public void decorateWhenUpdate(XSSFWorkbook workbook, List<Webtoon> webtoonList) {
		XSSFSheet listSheet = workbook.getSheet(Constants.excel.SHEET_NAME_LIST);
		XSSFSheet metadataSheet = workbook.getSheet(Constants.excel.SHEET_NAME_METADATA);

		ExcelStyleUtil.makeColumnsFitContent(listSheet, webtoonList);
		ExcelStyleUtil.makeColumnsFitContent(metadataSheet);

		// Should execute this before hiding extraneous rows.
//		ExcelStyleUtil.initializeRowHeight(listSheet);

		ExcelStyleUtil.hideExtraneousRows(listSheet);
		ExcelStyleUtil.hideExtraneousRows(metadataSheet);
	}

	public void save(File file, XSSFWorkbook workbook) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(file)) {
			workbook.write(fos);
			workbook.close();
		}
	}
	
	private String getSuffix() {
		String now = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
		return "_" + now;
	}

}
