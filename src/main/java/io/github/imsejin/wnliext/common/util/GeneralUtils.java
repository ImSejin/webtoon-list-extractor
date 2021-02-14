package io.github.imsejin.wnliext.common.util;

import io.github.imsejin.common.util.StringUtils;
import io.github.imsejin.wnliext.file.model.Webtoon;

import java.util.List;

/**
 * General utilities
 */
public final class GeneralUtils {

    private GeneralUtils() {
    }

    /**
     * Calculates version with number of updates.
     *
     * @param webtoons list of webtoons
     * @return version (e.g. "1.2.1")
     */
    public static String calcVersion(List<Webtoon> webtoons) {
        // Gets unique number of creation date.
        final int numOfUpdates = (int) webtoons.stream()
                .map(it -> it.getCreationTime().toLocalDate()).distinct().count();

        final int initVersion = 100; // It means `1.0.0`.
        final int addition = Math.max(numOfUpdates - 1, 0); // Why it subtracts 1 is starting point of version is `1.0.0`.

        StringBuilder version = new StringBuilder();
        for (String s : String.valueOf(initVersion + addition).split("")) {
            version.append(s).append('.');
        }

        return StringUtils.chop(version.toString());
    }

}
