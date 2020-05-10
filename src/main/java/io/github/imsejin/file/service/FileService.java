package io.github.imsejin.file.service;

import static io.github.imsejin.common.Constants.file.*;
import static io.github.imsejin.common.util.FileUtil.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;

import io.github.imsejin.common.util.FileUtil;
import io.github.imsejin.common.util.ObjectUtil;
import io.github.imsejin.file.model.Platform;
import io.github.imsejin.file.model.Webtoon;
import lombok.NonNull;

/**
 * FileService
 * 
 * @author SEJIN
 */
public class FileService {

	/**
	 * Returns current working absolute directory.
	 */
	public String getCurrentAbsolutePath() {
	    // It equals `System.getProperty("user.dir")`
		return Paths.get("").toAbsolutePath().toString();
	}

	/**
	 * Returns list of files and directories in the path.
	 */
	public List<File> getFileList(@NonNull String pathName) {
        File file = new File(pathName);
        return Arrays.asList(file.listFiles());
	}

	/**
	 * Converts list of files and directories to list of webtoons.
	 */
	public List<Webtoon> convertToWebtoonList(List<File> fileList) {
        if (fileList == null) fileList = new ArrayList<>();

        List<Webtoon> webtoonList = fileList.stream()
                .filter(FileUtil::isZip)
                .map(this::convertFileToWebtoon)
                .sorted(Comparator.comparing(Webtoon::getPlatform)
                        .thenComparing(Webtoon::getTitle)) // Sorts list of webtoons.
                .peek(System.out::println) // Prints console logs.
                .collect(Collectors.toList());

        // Prints console logs.
        System.out.println("\r\nTotal " + webtoonList.size() + " webtoon" + (webtoonList.size() > 1 ? "s" : ""));

        return webtoonList;
    }

    public String getLatestFileName(List<File> fileList) {
        String latestFileName = null;

        // Shallow copy.
        List<File> dummy = new ArrayList<>(fileList);

        // Removes non-webtoon-list from list.
        dummy.removeIf(file -> {
            String fileName = FilenameUtils.getBaseName(file.getName());
            String fileExtension = FilenameUtils.getExtension(file.getName());

            return !file.isFile() || !fileName.startsWith(EXCEL_FILE_NAME) || !fileExtension.equals(NEW_EXCEL_FILE_EXTENSION);
        });

        // Sorts out the latest file.
        if (ObjectUtil.isNotEmpty(dummy)) {
            String fileName = dummy.stream().map(File::getName).sorted(Comparator.reverseOrder()).findFirst().get();
            latestFileName = FilenameUtils.getBaseName(fileName);
        }

        return latestFileName;
    }

    /**
     * converts file to webtoon.
     */
    private Webtoon convertFileToWebtoon(File file) {
        String fileName = FilenameUtils.getBaseName(file.getName());
        Map<String, String> webtoonInfo = classifyWebtoonInfo(fileName);

        String title = webtoonInfo.get("title");
        String authors = webtoonInfo.get("authors");
        String platform = webtoonInfo.get("platform");
        String completed = webtoonInfo.get("completed");
        String creationTime = getCreationTime(file);
        String fileExtension = FilenameUtils.getExtension(file.getName());
        long size = file.length();

        return Webtoon.builder()
                .title(title)
                .authors(authors)
                .platform(platform)
                .isCompleted(Boolean.valueOf(completed))
                .creationTime(creationTime)
                .fileExtension(fileExtension)
                .size(size)
                .build();
    }

    /**
     * Analyzes the file name and classifies it as platform, title, author and completed.
     */
	private Map<String, String> classifyWebtoonInfo(String fileName) {
		StringBuffer sb = new StringBuffer(fileName);

		// Platform
		int i = sb.indexOf(DELIMITER_PLATFORM);
		String platform = convertAcronym(sb.substring(0, i));
		sb.delete(0, i + DELIMITER_PLATFORM.length());

		// Title
		int j = sb.lastIndexOf(DELIMITER_TITLE);
		String title = sb.substring(0, j);
		sb.delete(0, j + DELIMITER_TITLE.length());

		// Completed or uncompleted
		boolean completed = fileName.endsWith(DELIMITER_COMPLETED);
		
		// Authors
		String authors = completed
		        ? sb.substring(0, sb.indexOf(DELIMITER_COMPLETED))
		        : sb.toString();

		Map<String, String> map = new HashMap<>();
		map.put("title", title);
		map.put("authors", authors);
		map.put("platform", platform);
		map.put("completed", String.valueOf(completed));

		return map;
	}

	/**
	 * Converts acronym of platform to full text.
	 */
	private String convertAcronym(String acronym) {
		List<Platform> platformList = Arrays.asList(Platform.values());
		String result = acronym;

		for (Platform platform : platformList) {
			if (platform.name().equals(acronym)) {
				result = platform.getFullText();
				break;
			}
		}

		return result;
	}

}
