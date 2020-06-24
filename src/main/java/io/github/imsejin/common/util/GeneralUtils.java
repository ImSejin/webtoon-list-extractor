package io.github.imsejin.common.util;

import static io.github.imsejin.common.Constants.file.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import io.github.imsejin.file.model.Webtoon;
import lombok.experimental.UtilityClass;

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
@UtilityClass
public class GeneralUtils {

    /**
     * Converts from list of authors to string of authors.
     * 
     * @param list of authors
     * @return string of authors
     */
    public String convertAuthors(List<String> authors) {
        StringBuffer sb = new StringBuffer();

        int size = authors.size();
        for (int i = 0; i < size; i++) {
            String author = authors.get(i);
            sb.append(author);
            sb.append(DELIMITER_AUTHOR);
        }
        sb.delete(sb.length() - DELIMITER_AUTHOR.length(), sb.length());

        return sb.toString();
    }

    /**
     * Converts from string of authors to list of authors.
     * 
     * @param string of authors
     * @return list of authors
     */
    public List<String> convertAuthors(String authors) {
        return Arrays.asList(authors.split(DELIMITER_AUTHOR));
    }

    public String[] calculateMetadata(List<Webtoon> webtoons) {
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
                : updateDateList.stream().sorted(Comparator.reverseOrder()).findFirst().get();
        metadataContents[1] = latestUpdateDate;

        return metadataContents;
    }

    public String calculateVersion(int updateCount) {
        final int initialVersionCode = 100; // It means `1.0.0`.
        String version = "";

        int count = (updateCount > 1 ? updateCount - 1 : 0); // Why it subtracts 1 is starting point of version is `1.0.0`.
        String str = String.valueOf(initialVersionCode + count);
        String[] arr = str.split("");
        for (int i = 0; i < arr.length; i++) {
            version += arr[i];
            if (i < arr.length - 1) version += ".";
        }

        return version;
    }

}
