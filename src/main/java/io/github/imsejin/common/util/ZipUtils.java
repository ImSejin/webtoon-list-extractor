package io.github.imsejin.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 압축 유틸리티<br>
 * Archive utilities
 * 
 * <p>
 * 
 * </p>
 * 
 * @author SEJIN
 */
public final class ZipUtils {

    private ZipUtils() {}

    private static final List<String> EXTENSIONS = Arrays.asList("7z", "alz", "ace", "exe", "gz", "iso", "lzh", "rar", "tar", "tgz", "xz", "zip", "zipx");

    private static final String ZIP_FILE_EXTENSION = ".zip";

    /**
     * 해당 경로에 있는 모든 파일을 압축하고, 압축된 파일을 삭제한다.
     * (폴더와 압축 파일은 압축 대상에서 제외)
     * 
     * <pre>
     * Path path = Paths.get("C:/Program Files/Java");
     * ZipUtil.compress(path, "java.zip");
     * </pre>
     */
    public static File compress(Path path, String zipFileName) {
        return compress(path, zipFileName, true);
    }

    /**
     * 해당 경로에 있는 모든 파일을 압축한다.
     * (압축된 파일을 삭제할지 결정할 수 있음, 폴더와 압축 파일은 압축 대상에서 제외)
     * 
     * <pre>
     * Path path = Paths.get("C:/Program Files/Java");
     * ZipUtil.compress(path, "java.zip", false);
     * </pre>
     */
    public static File compress(Path path, String zipFileName, boolean willDelete) {
        // 압축 파일을 제외한 파일 리스트
        File[] fileArray = path.toFile().listFiles((file, fileNm) -> !fileNm.endsWith(ZIP_FILE_EXTENSION));

        // 존재하지 않은 경로면, 압축을 중단한다
        if (fileArray == null) return null;

        // 폴더를 제외한 파일 리스트
        File[] files = Stream.of(fileArray).filter(File::isFile).toArray(File[]::new);

        // 해당 경로에 파일이 하나도 없으면, 압축을 중단한다
        if (files == null || files.length == 0) return null;

        // 압축파일명에 `.zip` 확장자가 붙어 있지 않으면, 붙여준다
        if (!zipFileName.endsWith(ZIP_FILE_EXTENSION)) zipFileName += ZIP_FILE_EXTENSION;

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
     * 압축파일을 같은 경로에 폴더를 만들어 해제한다.
     * (압축파일을 삭제할지 결정할 수 있음)
     * 
     * <pre>
     * File zipFile = new File("C:/Program Files/Java/jdk-8.zip");
     * 
     * ZipUtil.decompress(zipFile, false);
     * </pre>
     */
    public static void decompress(File zipFile, boolean willDelete) {
        File dir = FileUtils.mkdirAsOwnName(zipFile);
        _decompress(zipFile, dir.toPath(), willDelete, false);
    }

    /**
     * 압축파일을 지정한 경로에 해제한다.
     * (압축파일을 삭제할지 결정할 수 있음)
     * 
     * <pre>
     * File zipFile = new File("C:/Program Files/Java/jdk-8.zip");
     * Path destination = Paths.get("E:/Program Files/Java/jdk");
     * 
     * ZipUtil.decompress(zipFile, destination, false);
     * </pre>
     */
    public static void decompress(File zipFile, Path dest, boolean willDelete) {
        _decompress(zipFile, dest, willDelete, false);
    }

    /**
     * 압축파일을 같은 경로에 폴더를 만들어 해제한다.
     * (압축파일을 삭제할지 결정할 수 있음)
     * (중첩 압축파일을 모두 해제함)
     * 
     * <pre>
     * File zipFile = new File("C:/Program Files/Java/jdk-8.zip");
     * 
     * ZipUtil.decompressAll(zipFile, true);
     * </pre>
     */
    @Deprecated
    public static void decompressAll(File zipFile, boolean willDelete) {
        File dir = FileUtils.mkdirAsOwnName(zipFile);
        _decompress(zipFile, dir.toPath(), willDelete, true);
    }

    /**
     * 압축파일을 지정한 경로에 해제한다.
     * (압축파일을 삭제할지 결정할 수 있음)
     * (중첩 압축파일을 모두 해제함)
     * 
     * <pre>
     * File zipFile = new File("C:/Program Files/Java/jdk-8.zip");
     * Path destination = Paths.get("E:/Program Files/Java/jdk");
     * 
     * ZipUtil.decompressAll(zipFile, destination, true);
     * </pre>
     */
    @Deprecated
    public static void decompressAll(File zipFile, Path dest, boolean willDelete) {
        _decompress(zipFile, dest, willDelete, true);
    }

    private static void _decompress(File zipFile, Path dest, boolean willDelete, boolean nested) {
        // 유효한 압축파일인지 확인한다
        if (zipFile == null || !zipFile.getName().endsWith(ZIP_FILE_EXTENSION)) return;

        // 코드페이지를 `확장 완성형 한글`로 설정한다
        final Charset cp949 = Charset.forName("CP949");

        try (ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)), cp949)) {
            // 존재하지 않는 디렉터리면 생성한다
            if (Files.notExists(dest)) dest.toAbsolutePath().toFile().mkdirs();

            final String destPathName = dest.toRealPath().toString();
            final byte[] buffer = new byte[1024];

            // 엔트리가 없을 때까지 압축을 해제한다
            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                System.out.println("entry: " + entry);

                // 엔트리가 폴더면 폴더를 생성한다
                if (entry.isDirectory()) {
                    File folder = Paths.get(destPathName, entry.getName()).toFile();
                    folder.mkdirs();
                    System.out.println("folder: " + folder);
                }

                // 해당 엔트리를 파일로 생성한다
                File file = new File(destPathName, entry.getName());
                try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                // 중첩 압축파일을 해제한다
                if (nested && file.getName().endsWith(ZIP_FILE_EXTENSION)) {
                    _decompress(file, file.getParentFile().toPath(), willDelete, nested);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // 압축파일을 삭제한다
        if (willDelete) zipFile.delete();
    }

    /**
     * 압축파일 내 파일명 리스트를 반환한다.
     * 
     * <pre>
     * File zipFile = new File("C:/Program Files/Java/jdk-8.zip");
     * 
     * ZipUtil.getEntryNames(zipFile): ["query.sql", "data.zip", "catalina.out", "dir/", "eclipse-log.log"]
     * </pre>
     */
    public static List<String> getEntryNames(File zipFile) {
        return getEntryNames(zipFile, false);
    }

    /**
     * 압축파일 내 파일명 리스트를 반환한다.
     * (폴더를 제외할지 선택할 수 있음)
     * 
     * <pre>
     * File zipFile = new File("C:/Program Files/Java/jdk-8.zip");
     * 
     * ZipUtil.getEntryNames(zipFile, true): ["query.sql", "data.zip", "catalina.out", "eclipse-log.log"]
     * </pre>
     */
    public static List<String> getEntryNames(File zipFile, boolean exceptDir) {
        List<String> entryNames = null;

        // 유효한 압축파일인지 확인한다
        if (zipFile == null || !zipFile.getName().endsWith(ZIP_FILE_EXTENSION)) return entryNames;

        // 코드페이지를 `확장 완성형 한글`로 설정한다
        final Charset cp949 = Charset.forName("CP949");

        try (ZipFile zip = new ZipFile(zipFile, cp949)) {
            Stream<? extends ZipEntry> stream = zip.stream();
            Function<? super ZipEntry, String> mapper = ZipEntry::getName;

            // 디렉터리를 제외한다
            if (exceptDir) {
                stream = stream.filter(entry -> !entry.isDirectory());
                mapper = ZipUtils::getFileName;
            }

            entryNames = stream.map(mapper).collect(Collectors.toList());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return entryNames;
    }

    private static String getFileName(ZipEntry entry) {
        String fileName = entry.getName();
        int lastIndexOfSeparator = lastIndexDirectory(fileName);

        return lastIndexOfSeparator != -1 ? fileName.substring(lastIndexOfSeparator + 1) : fileName;
    }

    private static int lastIndexDirectory(String pathName) {
        int index = -1;

        // 기본 `경로 구분자`의 경우
        index = pathName.lastIndexOf("/");

        // "/" 문자열이 `경로 구분자`로 쓰이지 않았을 경우
        if (index == -1) index = pathName.lastIndexOf(File.separator);

        return index;
    }

    /**
     * 압축 파일인지 확인한다.
     * 
     * <pre>
     * File file1 = new File("D:/Program Files/Java/jdk1.8.0_202/README.html");
     * File file2 = new File("D:/Program Files/Java/jdk1.8.0_202/src.zip");
     * 
     * ZipUtil.isZip(file1): false
     * ZipUtil.isZip(file2): true
     * </pre>
     */
    public static boolean isZip(File file) {
        if (file == null || !file.isFile()) return false;

        for (String extension : EXTENSIONS) {
            if (extension.equalsIgnoreCase(FilenameUtils.extension(file))) return true;
        }

        return false;
    }

}
