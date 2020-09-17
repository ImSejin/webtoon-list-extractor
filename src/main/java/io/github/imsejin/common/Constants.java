package io.github.imsejin.common;

/**
 * Constants
 *
 * @author SEJIN
 */
public final class Constants {

    private Constants() {
    }

    public static final class console {
        public static final int PERCENT_MULTIPLES = 100;
        public static final int PROGRESS_BAR_LENGTH = 50;

        private console() {
        }
    }

    public static final class excel {
        public static final String SHEET_NAME_LIST = "List";
        public static final String SHEET_NAME_METADATA = "Metadata";
        public static final String SHEET_NAME_DATABASE = "Database";
        public static final String HEADER_FONT_NAME = "NanumBarunGothic";
        public static final String CONTENT_FONT_NAME = "NanumBarunGothic Light";
        public static final int XLS_MAX_ROWS = 65536;
        public static final int XLS_MAX_COLUMNS = 256;
        public static final int XLSX_MAX_ROWS = 1048576;
        public static final int XLSX_MAX_COLUMNS = 16384;

        private excel() {
        }
    }

    public static final class file {
        public static final String DELIMITER_PLATFORM = "_";
        public static final String DELIMITER_TITLE = " - ";
        public static final String DELIMITER_AUTHOR = ", ";
        public static final String DELIMITER_COMPLETED = " [\u5B8C]"; // " [完]"
        public static final String EXCEL_FILE_PREFIX = "webtoonList-";
        public static final String XLS_FILE_EXTENSION = "xls";
        public static final String XLSX_FILE_EXTENSION = "xlsx";

        private file() {
        }
    }

}
