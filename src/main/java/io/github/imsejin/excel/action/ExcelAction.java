package io.github.imsejin.excel.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import io.github.imsejin.excel.service.ExcelService;
import io.github.imsejin.file.model.Webtoon;

/**
 * ExcelAction
 * 
 * @author SEJIN
 */
public class ExcelAction {

    private final ExcelService service = new ExcelService();

    public void createWebtoonList(String pathName, List<Webtoon> webtoonList) throws IOException {
        File file = null;
        XSSFWorkbook workbook = null;

        try {
            // Constitutes data in the excel file and returns instances to interact with next method.
            Object[] interactions = service.writer.create(pathName, webtoonList);
            file = (File) interactions[0];
            workbook = (XSSFWorkbook) interactions[1];

            // Receives instances from previous method, decorates each sheets and saves it.
            service.decorateWhenCreate(workbook, webtoonList);
            service.save(file, workbook);
        } finally {
            if (workbook != null) workbook.close();
        }
    }

    @SuppressWarnings("unchecked")
    public void updateWebtoonList(String pathName, List<Webtoon> webtoonList, String latestFileName) throws IOException {
        FileInputStream fis = null;
        XSSFWorkbook workbook = null;
        List<Webtoon> previousList = null;
        String previousVersion = null;
        File file = null;

        try {
            // Reads data in the excel file and returns instances to interact with next method.
            Object[] interactions = service.reader.read(pathName, latestFileName);
            fis = (FileInputStream) interactions[0];
            workbook = (XSSFWorkbook) interactions[1];
            previousList = (List<Webtoon>) interactions[2];
            previousVersion = (String) interactions[3];

            // Receives instances from previous method, updates data in the excel file and returns instance to interact with next method.
            file = service.writer.update(pathName, previousList, webtoonList, previousVersion, fis, workbook);

            // Receives instances from previous method, decorates each sheets and saves it.
            service.decorateWhenUpdate(workbook, webtoonList);
            service.save(file, workbook);
        } finally {
            if (fis != null) fis.close();
            if (workbook != null) workbook.close();
        }
    }

}
