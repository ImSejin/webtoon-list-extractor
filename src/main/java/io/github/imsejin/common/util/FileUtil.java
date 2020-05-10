package io.github.imsejin.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import io.github.imsejin.common.util.DateUtil.DateType;
import lombok.experimental.UtilityClass;

/**
 * File uilities
 */
@UtilityClass
public class FileUtil {

    private final List<String> EXTENSIONS = Arrays.asList("7z", "alz", "ace", "exe", "gz", "iso", "lzh", "rar", "tar", "tgz", "xz", "zip", "zipx");

    private final String ZIP_EXTENSION = "zip";

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
	 * 파일의 확장자를 가져온다.
	 * 
	 * <pre>
	 * FileUtil.getFileExtension("D:/Program Files/Java/jdk1.8.0_202/README.html"): "html"
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
     * FileUtil.getFilePathWithYearMonth("C:\Program Files"): new File("C:\Program Files\2019\12")
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
     * FileUtil.getFilePathWithYearMonth("C:\Program Files"): new File("C:\Program Files\2019\12\31")
     * </pre>
     */
    public File getFilePathWithYearMonthDay(String path) {
        return Paths.get(path, DateUtil.getToday(DateType.YEAR), DateUtil.getToday(DateType.MONTH), DateUtil.getToday(DateType.DAY)).toFile();
    }

    /**
     * 해당 경로에 있는 모든 파일을 압축하고, 압축된 파일을 삭제한다.
     * (폴더와 압축 파일은 압축 대상에서 제외)
     * 
     * <pre>
     * Path path = Paths.get("C:/Program Files/Java");
     * FileUtil.compress(path, "java.zip");
     * </pre>
     */
    public File compress(Path path, String zipFileName) {
        return compress(path, zipFileName, true);
    }

    /**
     * 해당 경로에 있는 모든 파일을 압축한다.
     * (압축된 파일을 삭제할지 결정할 수 있음, 폴더와 압축 파일은 압축 대상에서 제외)
     * 
     * <pre>
     * Path path = Paths.get("C:/Program Files/Java");
     * FileUtil.compress(path, "java.zip", false);
     * </pre>
     */
    public File compress(Path path, String zipFileName, boolean willDelete) {
        // 압축 파일을 제외한 파일 리스트
        File[] fileArray = path.toFile().listFiles((file, fileNm) -> !fileNm.endsWith(ZIP_EXTENSION));

        // 존재하지 않은 경로면, 압축을 중단한다
        if (fileArray == null) return null;

        // 폴더를 제외한 파일 리스트
        File[] files = Stream.of(fileArray).filter(File::isFile).toArray(File[]::new);

        // 해당 경로에 파일이 하나도 없으면, 압축을 중단한다
        if (files == null || files.length == 0) return null;

        // 압축파일명에 `.zip` 확장자가 붙어 있지 않으면, 붙여준다
        if (!zipFileName.endsWith(ZIP_EXTENSION)) zipFileName += ZIP_EXTENSION;

        // Create the ZIP file
        File zipFile = Paths.get(path.toString(), zipFileName).toFile();
        try (ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)))) {
            // Create a buffer for reading the files
            byte[] buffer = new byte[1024];

            for (File file : files) {
                String filePath = file.getPath();
                String fileName = file.getName();

                FileInputStream in = new FileInputStream(filePath);

                // Add ZIP entry to output stream.
                out.putNextEntry(new ZipEntry(fileName));

                // Transfer bytes from the file to the ZIP file
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }

                // Complete the entry
                out.closeEntry();
                in.close();
            }

            // 압축 대상의 파일을 삭제한다
            if (willDelete) Stream.of(files).forEach(File::delete);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return zipFile;
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
        BasicFileAttributes attributes;
        FileTime time = null;

        try {
            attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            time = attributes.creationTime();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String creationTime = LocalDateTime.ofInstant(time.toInstant(), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return creationTime;
    }
    
    /**
     * 압축 파일인지 확인한다.
     * 
     * <pre>
     * File file1 = new File("D:/Program Files/Java/jdk1.8.0_202/README.html");
     * File file2 = new File("D:/Program Files/Java/jdk1.8.0_202/src.zip");
     * 
     * FileUtil.isZip(file1): false
     * FileUtil.isZip(file2): true
     * </pre>
     */
    public boolean isZip(File file) {
        if (file == null || !file.isFile()) return false;

        for (String extension : EXTENSIONS) {
            if (extension.equalsIgnoreCase(getExtension(file))) return true;
        }

        return false;
    }

}
