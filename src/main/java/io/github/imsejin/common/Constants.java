package io.github.imsejin.common;


import lombok.experimental.UtilityClass;

/**
 * Constants
 * 
 * @author SEJIN
 */
@UtilityClass
public class Constants {

    @UtilityClass
    public class console {
        public final int PERCENT_MULTIPLES = 100;
        public final int PROGRESS_BAR_LENGTH = 50;
    }

    @UtilityClass
    public class excel {
        public final String SHEET_NAME_LIST = "List";
        public final String SHEET_NAME_METADATA = "Metadata";
        public final String SHEET_NAME_DATABASE = "Database";

        public final String HEADER_FONT_NAME = "NanumBarunGothic";
        public final String CONTENT_FONT_NAME = "NanumBarunGothic Light";

        public final int XLS_MAX_ROWS = 65536;
        public final int XLS_MAX_COLUMNS = 256;
        public final int XLSX_MAX_ROWS = 1048576;
        public final int XLSX_MAX_COLUMNS = 16384;
    }

    @UtilityClass
    public class file {
        public final String DELIMITER_PLATFORM = "_";
        public final String DELIMITER_TITLE = " - ";
        public final String DELIMITER_AUTHOR = ", ";
        public final String DELIMITER_COMPLETED = " [\u5B8C]"; // " [完]"

        public final String EXCEL_FILE_PREFIX = "webtoonList-";
        public final String XLS_FILE_EXTENSION = "xls";
        public final String XLSX_FILE_EXTENSION = "xlsx";
    }

}
