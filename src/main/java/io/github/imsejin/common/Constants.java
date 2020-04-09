package io.github.imsejin.common;

/**
 * Constants
 * 
 * @author SEJIN
 */
public final class Constants {

	public static class console {
		public static final int PERCENT_MULTIPLES = 100;
		public static final int PROGRESS_BAR_LENGTH = 50;
	}

	public static class excel {
		public static final String SHEET_NAME_LIST = "List";
		public static final String SHEET_NAME_METADATA = "Metadata";
		public static final String SHEET_NAME_DATABASE = "Database";

		public static final String HEADER_FONT_NAME = "NanumBarunGothic";
		public static final String CONTENT_FONT_NAME = "NanumBarunGothic Light";

		public static final int OLD_MAX_COUNT_OF_ROWS = 65536;
		public static final int OLD_MAX_COUNT_OF_COLUMNS = 256;
		public static final int NEW_MAX_COUNT_OF_ROWS = 1048576;
		public static final int NEW_MAX_COUNT_OF_COLUMNS = 16384;
	}

	public static class file {
		public static final String DELIMITER_PLATFORM = "_";
		public static final String DELIMITER_TITLE = " - ";
		public static final String DELIMITER_AUTHOR = ", ";
		public static final String DELIMITER_COMPLETED = " [\u5B8C]"; // " [完]"

		public static final String EXCEL_FILE_NAME = "webtoonList";
		public static final String OLD_EXCEL_FILE_EXTENSION = "xls";
		public static final String NEW_EXCEL_FILE_EXTENSION = "xlsx";
	}

}
