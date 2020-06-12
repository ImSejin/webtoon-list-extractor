package io.github.imsejin.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import io.github.imsejin.common.util.DateUtil.DateType;
import lombok.experimental.UtilityClass;

/**
 * 파일 유틸리티<br>
 * File uilities
 * 
 * <p>
 * 
 * </p>
 * 
 * @author SEJIN
 */
@UtilityClass
public class FileUtil {

    public List<File> getFilePathListForFolder(String folderPath) {
        final File folder = new File(folderPath);
        return getFilePathListForFolder(folder);
    }

    public List<File> getFilePathListForFolder(final File folder) {
        List<File> fileList = new ArrayList<File>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                fileList.addAll(getFilePathListForFolder(fileEntry));
            } else {
                fileList.add(fileEntry);
            }
        }
        return fileList;
    }

	/**
	 * <p>
	 * 파일을 읽는다.
	 * </p>
	 *
	 * @param file
	 *             <code>File</code>
	 * @return 결과 값
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String readFile(File file) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		return readFileContent(in);
	}

	/**
	 * <p>
	 * String 형으로 파일의 내용을 읽는다.
	 * </p>
	 *
	 * @param in the in
	 * @return 파일 내용
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String readFileContent(InputStream in) throws IOException {
		StringBuffer buf = new StringBuffer();

		for (int i = in.read(); i != -1; i = in.read()) {
			buf.append((char) i);
		}

		return buf.toString();
	}

	/**
	 * <p>
	 * String 영으로 파일의 내용을 읽는다.
	 * </p>
	 *
	 * @param file
	 *            <code>File</code>
	 * @param encoding
	 *            <code>String</code>
	 * @return 파일 내용
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String readFile(File file, String encoding) throws IOException {
		StringBuffer sb = new StringBuffer();

		List<String> lines = FileUtils.readLines(file, encoding);

		for (Iterator<String> it = lines.iterator();;) {
			sb.append(it.next());

			if (it.hasNext()) {
				sb.append("");
			} else {
				break;
			}
		}

		return sb.toString();
    }

    /**
     * 확장자를 제외한 파일명을 반환한다.
     * 
     * <pre>
     * File file = new File("D:/Program Files/Java/jdk1.8.0_202/README.html");
     * 
     * FileUtil.getBaseName(file): "README"
     * </pre>
     */
    public String getBaseName(File file) {
        return FilenameUtils.getBaseName(file.getName());
    }

    /**
     * 파일의 확장자를 반환한다.
     * 
     * <pre>
     * File file = new File("D:/Program Files/Java/jdk1.8.0_202/README.html");
     * 
     * FileUtil.getFileExtension(file): "html"
     * </pre>
     */
    public String getExtension(File file) {
        return FilenameUtils.getExtension(file.getName());
    }

	/**
	 * <p>
	 * 파일의 존재여부를 확인한다.
	 * </p>
	 * 
	 * @param filename
	 *            <code>String</code>
	 * @return 존재여부
	 */
	public boolean isExistsFile(String filename) {
		File file = new File(filename);
		return file.exists();
	}

	/**
	 * <p>
	 * 디렉토리명을 제외한 파일명을 가져온다.
	 * </p>
	 *
	 * @param filename
	 *            <code>String</code>
	 * @return the string
	 */
	public String stripFilename(String filename) {
		return FilenameUtils.getBaseName(filename);
	}

	/**
	 * <p>
	 * 파일의 크기를 가져온다.
	 * </p>
	 * 
	 * @param filename
	 *            <code>String</code>
	 * @return 존재여부
	 */
	public long getFileSize(String filename) throws Exception {
		File file = new File(filename);
		if (!file.exists()) {
			return Long.valueOf("0");
		}
		return file.length();
	}

    /**
     * 디렉터리 경로 끝에 현재의 연/월(yyyy/MM) 폴더를 추가한다.
     * 
     * <pre>
     * DateUtil.getToday(): "20191231"
     * 
     * FileUtil.getFilePathWithYearMonth("C:/Program Files"): new File("C:/Program Files/2019/12")
     * </pre>
     */
    public File getFilePathWithYearMonth(String path) {
        return Paths.get(path, DateUtil.getToday(DateType.YEAR), DateUtil.getToday(DateType.MONTH)).toFile();
    }

    /**
     * 디렉터리 경로 끝에 현재의 연/월/일(yyyy/MM/dd) 폴더를 추가한다.
     * 
     * <pre>
     * DateUtil.getToday(): "20191231"
     * 
     * FileUtil.getFilePathWithYearMonth("C:/Program Files"): new File("C:/Program Files/2019/12/31")
     * </pre>
     */
    public File getFilePathWithYearMonthDay(String path) {
        return Paths.get(path, DateUtil.getToday(DateType.YEAR), DateUtil.getToday(DateType.MONTH), DateUtil.getToday(DateType.DAY)).toFile();
    }

    /**
     * 파일의 생성시간을 반환한다.
     * 
     * <pre>
     * File file = new File("D:/Program Files/Java/jdk1.8.0_202/README.html");
     * 
     * FileUtil.getCreationTime(file): "2020-02-29 23:06:34"
     * </pre>
     */
    public String getCreationTime(File file) {
        String creationTime = null;

        try {
            BasicFileAttributes attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            FileTime time = attributes.creationTime();
            creationTime = LocalDateTime.ofInstant(time.toInstant(), ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return creationTime;
    }
    
    /**
     * 같은 경로에 해당 파일명과 같은 이름의 폴더를 생성한다.
     * 
     * <pre>
     * File file = new File("C:/Program Files/list_20191231.xlsx");
     * 
     * FileUtil.mkdirAsOwnName(file): new File("C:/Program Files/list_20191231")
     * </pre>
     */
    public static File mkdirAsOwnName(File file) {
        String dirName = stripFilename(file.getName());

        File dir = new File(file.getParentFile(), dirName);
        dir.mkdir();

        return dir;
    }

}
