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

import org.apache.commons.io.FilenameUtils;

import io.github.imsejin.common.util.ObjectUtil;
import io.github.imsejin.file.model.Platform;
import io.github.imsejin.file.model.Webtoon;

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
	public List<File> getFileList(String pathName) {
        File file = new File(pathName);
        return Arrays.asList(file.listFiles());
	}

	/**
	 * Converts list of files and directories to list of webtoons.
	 */
	public List<Webtoon> convertToWebtoonList(List<File> fileList) {
        if (fileList == null) return new ArrayList<>();

        List<Webtoon> webtoonList = new ArrayList<>();

        for (File file : fileList) {
            if (!isZip(file)) continue;
            
            String fileName = FilenameUtils.getBaseName(file.getName());
            String fileExtension = FilenameUtils.getExtension(file.getName());

            Map<String, Object> webtoonInfo = classifyWebtoonInfo(fileName);

            Object title = webtoonInfo.get("title");
            Object author = webtoonInfo.get("author");
            Object platform = webtoonInfo.get("platform");
            Object completed = webtoonInfo.get("completed");
            Object creationTime = getCreationTime(file);
            Object size = file.length();

            Webtoon webtoon = createWebtoon(title, author, platform, completed, creationTime, fileExtension, size);
            webtoonList.add(webtoon);

            // Prints console logs.
            System.out.println(webtoon.toString());
        }

		// Prints console logs.
		System.out.println("\r\nTotal " + webtoonList.size() + " webtoon" + (webtoonList.size() > 1 ? "s" : ""));

		// Sorts list of webtoons.
		webtoonList.sort(Comparator.comparing(Webtoon::getPlatform).thenComparing(Webtoon::getTitle));

		return webtoonList;
    }

    public String getLatestFileName(List<File> fileList) {
        String latestFileName = null;
        List<File> dummy = new ArrayList<>(fileList);

        dummy.removeIf(file -> {
            String fileName = FilenameUtils.getBaseName(file.getName());
            String fileExtension = FilenameUtils.getExtension(file.getName());

            return !(file.isFile() && fileName.startsWith(EXCEL_FILE_NAME) && fileExtension.equals(NEW_EXCEL_FILE_EXTENSION));
        });

        // Sorts out the latest file.
        if (ObjectUtil.isNotEmpty(dummy)) {
            String fileName = dummy.stream().map(file -> file.getName()).sorted(Comparator.reverseOrder()).findFirst().get();
            latestFileName = FilenameUtils.getBaseName(fileName);
        }

        return latestFileName;
    }
	
	/**
	 * Creates webtoon instance.
	 */
    @SuppressWarnings("unchecked")
    private Webtoon createWebtoon(Object... attributes) {
        // Not allow webtoon information to be null.
        for (Object attr : attributes) {
            if (attr == null) return null;
        }

        Webtoon webtoon = Webtoon.builder()
		        .title((String) attributes[0])
		        .author((List<String>) attributes[1])
		        .platform((String) attributes[2])
		        .isCompleted((boolean) attributes[3])
		        .creationTime((String) attributes[4])
		        .fileExtension((String) attributes[5])
		        .size((long) attributes[6])
		        .build();

        return webtoon;
	}

	/**
	 * Analyzes the file name and classifies it as platform, title, author and completed.
	 */
	private Map<String, Object> classifyWebtoonInfo(String fileName) {
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
		boolean completed = fileName.contains(DELIMITER_COMPLETED);
		
		// Author
		List<String> author;
		if (completed) {
			int k = sb.indexOf(DELIMITER_COMPLETED);
			author = Arrays.asList(sb.substring(0, k).split(DELIMITER_AUTHOR));
			sb.delete(0, k + DELIMITER_COMPLETED.length());
		} else {
			author = Arrays.asList(sb.toString().split(DELIMITER_AUTHOR));
		}

		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("author", author);
		map.put("platform", platform);
		map.put("completed", completed);

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
