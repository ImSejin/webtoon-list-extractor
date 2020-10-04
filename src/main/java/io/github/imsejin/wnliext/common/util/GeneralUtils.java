package io.github.imsejin.wnliext.common.util;

import io.github.imsejin.common.util.CollectionUtils;
import io.github.imsejin.wnliext.file.model.Webtoon;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.imsejin.wnliext.common.Constants.file.DELIMITER_AUTHOR;

/**
 * 공통 유틸리티<br>
 * General utilities
 * 
 * <p>
 * 
 * </p>
 * 
 * @author SEJIN
 */
public final class GeneralUtils {

    private GeneralUtils() {}

    /**
     * Converts from list of authors to string of authors.
     *
     * @param authors list of authors
     * @return string of authors
     */
    public static String convertAuthors(List<String> authors) {
        StringBuilder sb = new StringBuilder();

        for (var author : authors) {
            sb.append(author);
            sb.append(DELIMITER_AUTHOR);
        }
        sb.delete(sb.length() - DELIMITER_AUTHOR.length(), sb.length());

        return sb.toString();
    }

    /**
     * Converts from string of authors to list of authors.
     *
     * @param authors string of authors
     * @return list of authors
     */
    public static List<String> convertAuthors(String authors) {
        return Arrays.asList(authors.split(DELIMITER_AUTHOR));
    }

    public static String[] calculateMetadata(List<Webtoon> webtoons) {
        String[] metadataContents = new String[2];

        // Converts data format from `yyyy-MM-dd HH:mm:ss` to `yyyy-MM-dd`
        List<String> updateDateList = webtoons.stream()
                .map(webtoon -> webtoon.getCreationTime().substring(0, webtoon.getCreationTime().indexOf(" ")))
                .distinct()
                .collect(Collectors.toList());

        // Calculates count of importing webtoons.
        int updateCount = updateDateList.size();
        metadataContents[0] = calculateVersion(updateCount);

        // Sorts out the latest importation date.
        String latestUpdateDate = CollectionUtils.isNullOrEmpty(updateDateList)
                ? "-"
                : updateDateList.stream().max(Comparator.naturalOrder()).get();
        metadataContents[1] = latestUpdateDate;

        return metadataContents;
    }

    /**
     * Calculates version with count of updating.
     *
     * @param updateCount count of updating
     * @return version
     */
    public static String calculateVersion(int updateCount) {
        final int initialVersionCode = 100; // It means `1.0.0`.

        int count = Math.max(updateCount - 1, 0); // Why it subtracts 1 is starting point of version is `1.0.0`.
        String str = String.valueOf(initialVersionCode + count);
        String[] arr = str.split("");

        StringBuilder version = new StringBuilder();
        for (var i = 0; i < arr.length; i++) {
            version.append(arr[i]);
            if (i < arr.length - 1) version.append(".");
        }

        return version.toString();
    }

}
