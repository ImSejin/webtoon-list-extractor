package io.github.imsejin.wnliext.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constants
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class console {
        public static final int PERCENT_MULTIPLES = 100;
        public static final int PROGRESS_BAR_LENGTH = 50;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class file {
        public static final String EXCEL_FILE_PREFIX = "webtoonList";
    }

}
