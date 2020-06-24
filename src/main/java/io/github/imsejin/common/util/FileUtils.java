package io.github.imsejin.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
public class FileUtils {
    
    /**
     * 파일의 생성시간을 반환한다.
     * 
     * <pre>
     * File file = new File("D:\\Program Files\\Java\\jdk1.8.0_202\\README.html");
     * 
     * FileUtils.getCreationTime(file): "2020-02-29 23:06:34"
     * </pre>
     */
    public String creationTime(File file) {
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
     * File file = new File("C:\\Program Files\\list_20191231.xlsx");
     * 
     * FileUtils.mkdirAsOwnName(file): new File("C:\\Program Files\\list_20191231")
     * </pre>
     */
    public File mkdirAsOwnName(File file) {
        String dirName = FilenameUtils.baseName(file);

        File dir = new File(file.getParentFile(), dirName);
        dir.mkdir();

        return dir;
    }

}
