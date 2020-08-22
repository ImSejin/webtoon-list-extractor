package io.github.imsejin.common.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 엑셀 유틸리티<br>
 * Excel utilities
 * 
 * <p>
 * 엑셀 파일을 읽어 리스트를 반환하거나, 리스트를 엑셀 파일로 출력해주는 엑셀 유틸리티<br>
 * Excel utilities that returns the list by reading the excel file or writes the list as an excel file
 * </p>
 * 
 * @author SEJIN
 */
public final class ExcelUtils {

    private ExcelUtils() {}

    /**
     * ExcelUtils.reader
     * 
     * <pre>
     * 1. VO의 필드가 오직 `Wrapper Class` 또는 `String`이어야 하며, 기초형 필드가 있어서는 안된다.
     *    이외의 타입을 갖는 필드는 모두 null이 할당된다.
     * 
     * 2. 상속받은 필드는 제외된다, 즉 해당 VO에서 정의된 필드만 계산한다.
     * </pre>
     */
    public static final class reader {

        private reader() {}

        /**
         * 헤더를 제외한 모든 로우를 읽어 VO를 반환한다.
         */
        public static <T> List<T> read(Class<T> clazz, File file) {
            return read(clazz, file, null, null);
        }

        /**
         * 헤더는 제외되며, 지정된 로우부터 읽어 VO를 반환한다.
         */
        public static <T> List<T> readFromIndex(Class<T> clazz, File file, Integer startIndex) {
            return read(clazz, file, startIndex, null);
        }

        /**
         * 헤더는 제외되며, 지정된 로우까지 읽어 VO를 반환한다.
         */
        public static <T> List<T> readToIndex(Class<T> clazz, File file, Integer endIndex) {
            return read(clazz, file, null, endIndex);
        }

        /**
         * 헤더는 제외되며, 지정된 로우부터 또 달리 지정된 로우까지 읽어 VO를 반환한다.
         */
        public static <T> List<T> read(Class<T> clazz, File file, Integer startIndex, Integer endIndex) {
            List<T> result = new ArrayList<>();

            try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {
                Sheet sheet = workbook.getSheetAt(0);
                Row row = null;

                // 헤더를 제외한 전체 로우 개수
                final int NUMBER_OF_ROWS = sheet.getPhysicalNumberOfRows() - 1;

                // 인덱스 유효성을 체크한다
                if (startIndex == null || startIndex < 0) {
                    startIndex = 0;
                }
                if (endIndex == null || endIndex > NUMBER_OF_ROWS) {
                    endIndex = NUMBER_OF_ROWS;
                }

                // 엑셀 파일을 읽는다
                for (int i = startIndex; i < endIndex; i++) {
                    row = sheet.getRow(i + 1);
                    T vo = clazz.newInstance();
                    setDataByFields(result, vo, clazz, row);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return result;
        }

        /**
         * 상속받은 필드는 포함하지 않으나, 필드의 순서가 일정하다.
         */
        private static <T> void setDataByFields(List<T> result, T vo, Class<T> clazz, Row row) {
            Cell cell = null;

            int i = 0;
            for (Field field : clazz.getDeclaredFields()) {
                // NPE를 방지하고 모든 데이터를 문자열로 취급한다
                cell = row.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(CellType.STRING);
                String val = cell.getStringCellValue();

                // private 접근자라도 접근하게 한다
                field.setAccessible(true);

                // Object value = field.getType().cast(convertToOwnDataType(field, val));
                Object value = convertToOwnDataType(field, val);
                try {
                    field.set(vo, value);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }

                i++;
            }

            result.add(vo);
        }

        /**
         * VO에 정의된 자료형으로 변환한다.
         */
        private static Object convertToOwnDataType(Field field, String val) {
            Class<?> type = field.getType();
            Object result = null;

            if (type.equals(String.class)) {
                result = val;
            } else if (type.equals(Byte.class)) {
                result = Byte.parseByte(val);
            } else if (type.equals(Short.class)) {
                result = Short.parseShort(val);
            } else if (type.equals(Integer.class)) {
                result = Integer.parseInt(val);
            } else if (type.equals(Long.class)) {
                result = Long.parseLong(val);
            } else if (type.equals(Float.class)) {
                result = Float.parseFloat(val);
            } else if (type.equals(Double.class)) {
                result = Double.parseDouble(val);
            } else if (type.equals(Character.class)) {
                result = val.charAt(0);
            } else if (type.equals(Boolean.class)) {
                result = val.equalsIgnoreCase("T") || val.equalsIgnoreCase("TRUE")
                        || val.equalsIgnoreCase("Y") || val.equalsIgnoreCase("YES")
                        || val.equals("1");
            }

            return result;
        }

    }

    /**
     * ExcelUtils.writer
     * 
     * <pre>
     * 1. VO의 필드가 오직 `기초형`, `Wrapper Class` 또는 `String`이어야 한다.
     *    이외의 타입을 갖는 필드(컬럼)는 계산하지 않는다, 즉 해당 필드는 순서(엑셀 파일)에서 제외된다.
     * 
     * 2. 상속받은 필드는 제외된다, 즉 해당 VO에서 정의된 필드만 계산한다.
     * 
     * 3. `headerNames`와 VO의 필드 순서가 일치해야 한다.
     * </pre>
     */
    public static final class writer {

        private writer() {}

        /**
         * 엑셀 파일을 생성한다, 값이 null이거나 empty string인 경우 empty string으로 치환한다.
         */
        public static <T> void write(List<T> list, File file, String[] headerNames) {
            write(list, file, headerNames, "");
        }

        /**
         * 엑셀 파일을 생성한다, 값이 null이거나 empty string인 경우 지정된 문자열로 치환한다.
         */
        public static <T> void write(List<T> list, File file, String[] headerNames, final String defaultValue) {
            try (FileOutputStream fos = new FileOutputStream(file); Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("PRD");
                Row row = sheet.createRow(0);
                Cell cell = null;

                // 헤더 스타일을 설정한다
                CellStyle style = workbook.createCellStyle();
                style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);

                if (headerNames != null && headerNames.length > 0) {
                    // 헤더를 만든다
                    int i = 0;
                    for (String name : headerNames) {
                        cell = row.createCell(i);
                        cell.setCellStyle(style);
                        cell.setCellValue(name);
                        i++;
                    }

                    // 데이터를 기록한다
                    if (list != null && !list.isEmpty()) {
                        setDataByFields(list, defaultValue, sheet, row, cell);
                        workbook.write(fos);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        /**
         * 상속받은 메서드까지 포함하나, 메서드의 순서가 일정하지 않다.
         */
        @Deprecated
        private static <T> void setDataByMethods(List<T> list, Class<T> clazz, String defaultValue, Sheet sheet, Row row, Cell cell)
                throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            List<Method> methods = new ArrayList<>(Arrays.asList(clazz.getMethods()));
            methods.removeIf(method -> (!method.getName().startsWith("get") && method.getParameterCount() != 0)
                    || method.getName().equals("getClass") && method.getParameterCount() == 0);

            int j = 0;
            for (T vo : list) {
                row = sheet.createRow(j + 1);

                int k = 0;
                for (Method method : methods) {
                    cell = row.createCell(k);
                    cell.setCellValue(StringUtils.ifBlank(method.invoke(vo).toString(), defaultValue));
                    k++;
                }

                j++;
            }
        }

        /**
         * 상속받은 필드는 포함하지 않으나, 필드의 순서가 일정하다.
         */
        @SuppressWarnings("unchecked")
        private static <T> void setDataByFields(List<T> list, String defaultValue, Sheet sheet, Row row, Cell cell) {
            Class<T> clazz = (Class<T>) list.get(0).getClass();
            List<Field> fields = Stream.of(clazz.getDeclaredFields())
                    .filter(field -> isStringClass(field) || isPrimitive(field) || isWrapperClass(field))
                    .collect(Collectors.toList());

            int j = 0;
            for (T vo : list) {
                row = sheet.createRow(j + 1);

                int k = 0;
                for (Field field : fields) {
                    cell = row.createCell(k);
                    cell.setCellValue(StringUtils.ifBlank(convertToString(field, vo), defaultValue));
                    k++;
                }

                j++;
            }
        }

        private static <T> String convertToString(Field field, T vo) {
            // private 접근자라도 접근하게 한다
            field.setAccessible(true);

            // 필드값을 가져온다
            Object val = null;
            try {
                val = field.get(vo);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                ex.printStackTrace();
            }

            // 기초형 자료는 문자열로, 그 외의 자료형은 null로 변환한다
            String converted = null;
            if (val != null) {
                if (isStringClass(field)) {
                    converted = val.toString();
                } else if (isPrimitive(field) || isWrapperClass(field)) {
                    converted = String.valueOf(val);
                }
            }

            return converted;
        }

        /**
         * 자료형이 문자열(java.lang.String)인지 확인한다.
         */
        private static boolean isStringClass(Field field) {
            Class<?> type = field.getType();
            return type.equals(String.class);
        }

        /**
         * 자료형이 기초형인지 확인한다.
         */
        private static boolean isPrimitive(Field field) {
            Class<?> type = field.getType();
            return type.equals(byte.class) || type.equals(short.class) || type.equals(int.class) || type.equals(long.class)
                    || type.equals(float.class) || type.equals(double.class) || type.equals(char.class) || type.equals(boolean.class);
        }

        /**
         * 자료형이 래퍼클래스인지 확인한다.
         */
        private static boolean isWrapperClass(Field field) {
            Class<?> type = field.getType();
            return type.equals(Byte.class) || type.equals(Short.class) || type.equals(Integer.class) || type.equals(Long.class)
                    || type.equals(Float.class) || type.equals(Double.class) || type.equals(Character.class) || type.equals(Boolean.class);
        }

    }

}
