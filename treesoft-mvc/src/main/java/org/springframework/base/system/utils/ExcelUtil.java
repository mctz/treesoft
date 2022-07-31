package org.springframework.base.system.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.transaction.TransactionDefinition;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/ExcelUtil.class */
public class ExcelUtil {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /* JADX WARN: Multi-variable type inference failed */
    public static List<Map<String, String>> parseExcel(File file) throws Exception {
        if (file == null || !file.exists()) {
            throw new Exception("待解析的文件不存在或者为null");
        }
        List<Map<String, String>> list = new ArrayList<>();
        String fileExtendName = file.getName();
        if (fileExtendName.endsWith(".xls")) {
            list = parseXLSFile(file);
        } else if (fileExtendName.endsWith(".xlsx")) {
            list = parseXLSXFile(file);
        }
        return list;
    }

    public static void writeExcel(File toFile, String excelTitle, List<String> titleList, List<List<String>> data) throws Exception {
        if (toFile == null) {
            throw new Exception("导出excle不能为null");
        }
        String fileExtendName = toFile.getName();
        if (fileExtendName.endsWith(".xls")) {
            writeXLSFile(toFile, excelTitle, titleList, data);
        } else if (fileExtendName.endsWith(".xlsx")) {
            writeXLSXFile(toFile, excelTitle, titleList, data);
        }
    }

    private static void writeXLSFile(File toFile, String excelTitle, List<String> titleList, List<List<String>> dataList) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment((short) 2);
        HSSFFont font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 16);
        cellStyle.setBorderTop((short) 1);
        cellStyle.setBorderLeft((short) 1);
        cellStyle.setBorderRight((short) 1);
        cellStyle.setBorderBottom((short) 1);
        cellStyle.setFont(font);
        HSSFSheet hssfSheet = workbook.createSheet(excelTitle);
        HSSFCellStyle titleCellStyle = workbook.createCellStyle();
        titleCellStyle.setAlignment((short) 2);
        HSSFFont titleFont = workbook.createFont();
        titleFont.setFontName("黑体");
        titleFont.setFontHeightInPoints((short) 14);
        titleCellStyle.setBorderTop((short) 1);
        titleCellStyle.setBorderLeft((short) 1);
        titleCellStyle.setBorderRight((short) 1);
        titleCellStyle.setBorderBottom((short) 1);
        titleCellStyle.setFont(titleFont);
        HSSFCellStyle contentCellStyle = workbook.createCellStyle();
        HSSFFont contentFont = workbook.createFont();
        contentFont.setFontName("黑体");
        contentFont.setFontHeightInPoints((short) 10);
        contentCellStyle.setBorderTop((short) 1);
        contentCellStyle.setBorderLeft((short) 1);
        contentCellStyle.setBorderRight((short) 1);
        contentCellStyle.setBorderBottom((short) 1);
        contentCellStyle.setFont(contentFont);
        int columnLength = titleList.size();
        hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnLength - 1));
        HSSFRow hssfRow = hssfSheet.createRow(0);
        HSSFCell hssfCell = hssfRow.createCell(0);
        hssfCell.setCellValue(excelTitle);
        hssfCell.setCellStyle(cellStyle);
        Map<Integer, Object> maxValueLengt = new HashMap<>();
        for (int i = 0; i < titleList.size(); i++) {
            maxValueLengt.put(Integer.valueOf(i), Integer.valueOf(titleList.get(i).toString().length()));
            for (int j = 0; j < dataList.size(); j++) {
                if (dataList.get(j).get(i) != null && dataList.get(j).get(i).toString().length() > Integer.parseInt((String) maxValueLengt.get(Integer.valueOf(i)))) {
                    maxValueLengt.remove(Integer.valueOf(i));
                    maxValueLengt.put(Integer.valueOf(i), Integer.valueOf(dataList.get(j).get(i).toString().length()));
                }
            }
        }
        for (int i2 = 0; i2 < titleList.size(); i2++) {
            hssfSheet.setColumnWidth(i2, Integer.parseInt(String.valueOf((String) maxValueLengt.get(Integer.valueOf(i2))) + 1) * 512);
        }
        HSSFRow hssfRow2 = hssfSheet.createRow(1);
        for (int i3 = 0; i3 < columnLength; i3++) {
            HSSFCell hssfCell2 = hssfRow2.createCell(i3);
            hssfCell2.setCellValue(titleList.get(i3));
            hssfCell2.setCellStyle(titleCellStyle);
        }
        for (int i4 = 0; i4 < dataList.size(); i4++) {
            int rowIndex = 2 + i4;
            HSSFRow hssfRow3 = hssfSheet.createRow(rowIndex);
            List<String> rowDataList = dataList.get(i4);
            for (int i1 = 0; i1 < columnLength; i1++) {
                HSSFCell hssfCell3 = hssfRow3.createCell(i1);
                hssfCell3.setCellValue(rowDataList.get(i1));
                hssfCell3.setCellStyle(contentCellStyle);
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(toFile);
        workbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    private static void writeXLSXFile(File toFile, String excelTitle, List<String> titleList, List<List<String>> dataList) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(excelTitle);
        int columnLength = titleList.size();
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment((short) 2);
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 20);
        cellStyle.setBorderTop((short) 1);
        cellStyle.setBorderLeft((short) 1);
        cellStyle.setBorderRight((short) 1);
        cellStyle.setBorderBottom((short) 1);
        cellStyle.setFont(font);
        XSSFCellStyle titleCellStyle = workbook.createCellStyle();
        titleCellStyle.setAlignment((short) 2);
        XSSFFont titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 14);
        titleCellStyle.setBorderTop((short) 1);
        titleCellStyle.setBorderLeft((short) 1);
        titleCellStyle.setBorderRight((short) 1);
        titleCellStyle.setBorderBottom((short) 1);
        titleCellStyle.setFont(titleFont);
        XSSFCellStyle contentCellStyle = workbook.createCellStyle();
        XSSFFont contentFont = workbook.createFont();
        contentFont.setFontHeightInPoints((short) 12);
        contentCellStyle.setBorderTop((short) 1);
        contentCellStyle.setBorderLeft((short) 1);
        contentCellStyle.setBorderRight((short) 1);
        contentCellStyle.setBorderBottom((short) 1);
        contentCellStyle.setFont(contentFont);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnLength - 1));
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = row.createCell(0);
        cell.setCellValue(excelTitle);
        cell.setCellStyle(cellStyle);
        Map<Integer, Object> maxValueLengt = new HashMap<>();
        for (int i = 0; i < titleList.size(); i++) {
            maxValueLengt.put(Integer.valueOf(i), Integer.valueOf(titleList.get(i).toString().length()));
            for (int j = 0; j < dataList.size(); j++) {
                if (dataList.get(j).get(i) != null && dataList.get(j).get(i).toString().length() > Integer.parseInt((String) maxValueLengt.get(Integer.valueOf(i)))) {
                    maxValueLengt.remove(Integer.valueOf(i));
                    maxValueLengt.put(Integer.valueOf(i), Integer.valueOf(dataList.get(j).get(i).toString().length()));
                }
            }
        }
        for (int i2 = 0; i2 < titleList.size(); i2++) {
            sheet.setColumnWidth(i2, (Integer.parseInt((String) maxValueLengt.get(Integer.valueOf(i2))) + 1) * 512);
        }
        XSSFRow row2 = sheet.createRow(1);
        for (int i3 = 0; i3 < columnLength; i3++) {
            XSSFCell cell2 = row2.createCell(i3);
            cell2.setCellValue(titleList.get(i3));
            cell2.setCellStyle(titleCellStyle);
        }
        for (int i4 = 0; i4 < dataList.size(); i4++) {
            int rowIndex = 2 + i4;
            XSSFRow row3 = sheet.createRow(rowIndex);
            List<String> rowDataList = dataList.get(i4);
            for (int i1 = 0; i1 < columnLength; i1++) {
                XSSFCell cell3 = row3.createCell(i1);
                cell3.setCellValue(rowDataList.get(i1));
                cell3.setCellStyle(contentCellStyle);
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(toFile);
        workbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public static void writeXLSXFileTreeSoft(HttpServletResponse response, List<Map<String, Object>> dataList) throws Exception {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(true);
        Sheet sheet = workbook.createSheet();
        Map<String, Object> firstMap = dataList.get(0);
        int columnLength = firstMap.size();
        Row row = sheet.createRow(0);
        row.createCell(0);
        String[] titleArray = new String[columnLength];
        int z = 0;
        Iterator<Map.Entry<String, Object>> it = firstMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> vo = it.next();
            String key = vo.getKey();
            titleArray[z] = key;
            row.createCell(z).setCellValue(key);
            z++;
        }
        new HashMap();
        for (int i = 0; i < dataList.size(); i++) {
            int rowIndex = 1 + i;
            Row row2 = sheet.createRow(rowIndex);
            Map<String, Object> rowDataOne = dataList.get(i);
            for (int k = 0; k < columnLength; k++) {
                Cell cell = row2.createCell(k);
                if (rowDataOne.get(titleArray[k]) == null) {
                    cell.setCellValue("");
                } else {
                    cell.setCellValue(rowDataOne.get(titleArray[k]).toString());
                }
            }
        }
        response.setContentType("application/octet-stream;charset=UTF-8");
        try {
            response.addHeader("Content-Disposition", "attachment;filename=data.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            if (workbook != null) {
                workbook.dispose();
            }
        } catch (Exception e2) {
            LogUtil.e("查询导出EXCEL数据失败 " + e2);
        }
    }

    private static List<Map<String, String>> parseXLSFile(File file) throws Exception {
        List<Map<String, String>> rowList = new ArrayList<>();
        InputStream inputStream = new FileInputStream(file);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
        Map<Integer, String> titleKeys = new HashMap<>();
        HSSFRow hssfRow = hssfSheet.getRow(0);
        if (hssfRow != null) {
            for (int i = hssfSheet.getFirstRowNum(); i < hssfRow.getPhysicalNumberOfCells(); i++) {
                HSSFCell cell = hssfRow.getCell(i);
                if (cell != null) {
                    String value = cell.toString();
                    if (value != null && value.trim().length() != 0) {
                        titleKeys.put(Integer.valueOf(i), value);
                    } else {
                        titleKeys.put(Integer.valueOf(i), "COL" + i);
                    }
                }
            }
        }
        for (int i2 = hssfSheet.getFirstRowNum() + 1; i2 < hssfSheet.getPhysicalNumberOfRows(); i2++) {
            HSSFRow hssfRow2 = hssfSheet.getRow(i2);
            if (hssfRow2 != null) {
                boolean isRowValid = false;
                Map<String, String> columnMap = new HashMap<>();
                for (int j = hssfRow2.getFirstCellNum(); j < hssfRow2.getPhysicalNumberOfCells(); j++) {
                    HSSFCell hssfCell = hssfRow2.getCell(j);
                    if (hssfCell != null) {
                        String cellValue = getCellValue(hssfCell);
                        if (cellValue != null && cellValue.trim().length() == 0) {
                            cellValue = null;
                        }
                        columnMap.put(titleKeys.get(Integer.valueOf(j)), cellValue);
                        if (!isRowValid && cellValue != null && cellValue.trim().length() > 0) {
                            isRowValid = true;
                        }
                    }
                }
                if (isRowValid) {
                    rowList.add(columnMap);
                }
            }
        }
        return rowList;
    }

    private static List<Map<String, String>> parseXLSXFile(File file) throws Exception {
        List<Map<String, String>> rowList = new ArrayList<>();
        InputStream inputStream = new FileInputStream(file);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
        Map<Integer, String> titleKeys = new HashMap<>();
        XSSFRow xssfRow = xssfSheet.getRow(0);
        if (xssfRow != null) {
            for (int i = xssfSheet.getFirstRowNum(); i < xssfRow.getPhysicalNumberOfCells(); i++) {
                XSSFCell cell = xssfRow.getCell(i);
                if (cell != null) {
                    String value = cell.toString();
                    if (value != null && value.trim().length() != 0) {
                        titleKeys.put(Integer.valueOf(i), value);
                    } else {
                        titleKeys.put(Integer.valueOf(i), "COL" + i);
                    }
                }
            }
        }
        for (int i2 = xssfSheet.getFirstRowNum() + 1; i2 < xssfSheet.getPhysicalNumberOfRows(); i2++) {
            XSSFRow xssfRow2 = xssfSheet.getRow(i2);
            if (xssfRow2 != null) {
                boolean isRowValid = false;
                Map<String, String> columnMap = new HashMap<>();
                for (int j = xssfRow2.getFirstCellNum(); j < xssfRow2.getPhysicalNumberOfCells(); j++) {
                    XSSFCell xssfCell = xssfRow2.getCell(j);
                    if (xssfCell != null) {
                        String cellValue = getCellValue(xssfCell);
                        if (cellValue != null && cellValue.trim().length() == 0) {
                            cellValue = null;
                        }
                        columnMap.put(titleKeys.get(Integer.valueOf(j)), cellValue);
                        if (!isRowValid && cellValue != null && cellValue.trim().length() > 0) {
                            isRowValid = true;
                        }
                    }
                }
                if (isRowValid) {
                    rowList.add(columnMap);
                }
            }
        }
        return rowList;
    }

    private static String getCellValue(HSSFCell cell) {
        DecimalFormat df = new DecimalFormat("#");
        String cellValue = null;
        if (cell != null) {
            switch (cell.getCellType()) {
                case 0:
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                        cellValue = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                        break;
                    } else {
                        cellValue = df.format(cell.getNumericCellValue());
                        break;
                    }
                case 1:
                    cellValue = String.valueOf(cell.getStringCellValue());
                    break;
                case 2:
                    cellValue = String.valueOf(cell.getCellFormula());
                    break;
                case 3:
                    cellValue = null;
                    break;
                case 4:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case TransactionDefinition.PROPAGATION_NEVER /* 5 */:
                    cellValue = String.valueOf((int) cell.getErrorCellValue());
                    break;
            }
            if (cellValue != null && cellValue.trim().length() == 0) {
                cellValue = null;
            }
        }
        return cellValue;
    }

    private static String getCellValue(XSSFCell cell) {
        DecimalFormat df = new DecimalFormat("#");
        String cellValue = null;
        if (cell != null) {
            switch (cell.getCellType()) {
                case 0:
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                        cellValue = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                        break;
                    } else {
                        cellValue = df.format(cell.getNumericCellValue());
                        break;
                    }
                case 1:
                    cellValue = String.valueOf(cell.getStringCellValue());
                    break;
                case 2:
                    cellValue = String.valueOf(cell.getCellFormula());
                    break;
                case 3:
                    cellValue = null;
                    break;
                case 4:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case TransactionDefinition.PROPAGATION_NEVER /* 5 */:
                    cellValue = String.valueOf((int) cell.getErrorCellValue());
                    break;
            }
            if (cellValue != null && cellValue.trim().length() <= 0) {
                cellValue = null;
            }
        }
        return cellValue;
    }

    public static <T> void listToExcel(List<T> list, LinkedHashMap<String, String> fieldMap, String sheetName, int sheetSize, OutputStream out) throws Exception {
        if (list.size() == 0 || list == null) {
            throw new Exception("数据源中没有任何数据");
        }
        if (sheetSize > 65535 || sheetSize < 1) {
            sheetSize = 65535;
        }
        try {
            WritableWorkbook wwb = Workbook.createWorkbook(out);
            double sheetNum = Math.ceil(list.size() / new Integer(sheetSize).doubleValue());
            for (int i = 0; i < sheetNum; i++) {
                if (1.0d == sheetNum) {
                    WritableSheet sheet = wwb.createSheet(sheetName, i);
                    fillSheet(sheet, list, fieldMap, 0, list.size() - 1);
                } else {
                    WritableSheet sheet2 = wwb.createSheet(String.valueOf(sheetName) + (i + 1), i);
                    int firstIndex = i * sheetSize;
                    int lastIndex = ((i + 1) * sheetSize) - 1 > list.size() - 1 ? list.size() - 1 : ((i + 1) * sheetSize) - 1;
                    fillSheet(sheet2, list, fieldMap, firstIndex, lastIndex);
                }
            }
            wwb.write();
            wwb.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof Exception) {
                throw e;
            }
            throw new Exception("导出Excel失败");
        }
    }

    public static <T> void listToExcel(List<T> list, LinkedHashMap<String, String> fieldMap, String sheetName, OutputStream out) throws Exception {
        listToExcel(list, fieldMap, sheetName, 65535, out);
    }

    public static <T> void listToExcel(List<T> list, LinkedHashMap<String, String> fieldMap, String sheetName, int sheetSize, HttpServletResponse response) throws Exception {
        String fileName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();
        response.reset();
        response.setContentType(ServletUtils.EXCEL_TYPE);
        response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
        try {
            listToExcel((List) list, fieldMap, sheetName, sheetSize, (OutputStream) response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof Exception) {
                throw e;
            }
            throw new Exception("导出Excel失败");
        }
    }

    public static <T> void listToExcel(List<T> list, LinkedHashMap<String, String> fieldMap, String sheetName, HttpServletResponse response) throws Exception {
        listToExcel(list, fieldMap, sheetName, 65535, response);
    }

    public static <T> List<T> excelToList(InputStream in, String sheetName, Class<T> entityClass, LinkedHashMap<String, String> fieldMap, String[] uniqueFields) throws Exception {
        List<T> resultList = new ArrayList<>(0);
        try {
            Workbook wb = Workbook.getWorkbook(in);
            jxl.Sheet sheet = wb.getSheet(sheetName);
            int realRows = 0;
            for (int i = 0; i < sheet.getRows(); i++) {
                int nullCols = 0;
                for (int j = 0; j < sheet.getColumns(); j++) {
                    jxl.Cell currentCell = sheet.getCell(j, i);
                    if (currentCell == null || "".equals(currentCell.getContents().toString())) {
                        nullCols++;
                    }
                }
                if (nullCols == sheet.getColumns()) {
                    break;
                }
                realRows++;
            }
            if (realRows <= 1) {
                throw new Exception("Excel文件中没有任何数据");
            }
            jxl.Cell[] firstRow = sheet.getRow(0);
            String[] excelFieldNames = new String[firstRow.length];
            for (int i2 = 0; i2 < firstRow.length; i2++) {
                excelFieldNames[i2] = firstRow[i2].getContents().toString().trim();
            }
            boolean isExist = true;
            List<String> excelFieldList = Arrays.asList(excelFieldNames);
            Iterator<String> it = fieldMap.keySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String cnName = it.next();
                if (cnName.contains("*") && !excelFieldList.contains(cnName)) {
                    isExist = false;
                    break;
                }
            }
            if (!isExist) {
                throw new Exception("Excel中缺少必要的字段，或字段名称有误");
            }
            LinkedHashMap<String, Integer> colMap = new LinkedHashMap<>();
            for (int i3 = 0; i3 < excelFieldNames.length; i3++) {
                colMap.put(excelFieldNames[i3], Integer.valueOf(firstRow[i3].getColumn()));
            }
            for (int i4 = 1; i4 < realRows; i4++) {
                T entity = entityClass.newInstance();
                Iterator<Map.Entry<String, String>> it2 = fieldMap.entrySet().iterator();
                while (it2.hasNext()) {
                    Map.Entry<String, String> entry = it2.next();
                    String cnNormalName = entry.getKey();
                    String enNormalName = entry.getValue();
                    int col = colMap.get(cnNormalName).intValue();
                    String content = sheet.getCell(col, i4).getContents().toString().trim();
                    setFieldValueByName(enNormalName, content, entity);
                }
                resultList.add(entity);
            }
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof Exception) {
                throw e;
            }
            e.printStackTrace();
            throw new Exception("导入Excel失败");
        }
    }

    private static Object getFieldValueByName(String fieldName, Object o) throws Exception {
        Field field = getFieldByName(fieldName, o.getClass());
        if (field != null) {
            field.setAccessible(true);
            Object value = field.get(o);
            return value;
        }
        throw new Exception(String.valueOf(o.getClass().getSimpleName()) + "类不存在字段名 " + fieldName);
    }

    private static Field getFieldByName(String fieldName, Class<?> clazz) {
        Field[] selfFields = clazz.getDeclaredFields();
        for (Field field : selfFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz != Object.class) {
            return getFieldByName(fieldName, superClazz);
        }
        return null;
    }

    private static Object getFieldValueByNameSequence(String fieldNameSequence, Object o) throws Exception {
        Object value;
        String[] attributes = fieldNameSequence.split("\\.");
        if (attributes.length == 1) {
            value = getFieldValueByName(fieldNameSequence, o);
        } else {
            Object fieldObj = getFieldValueByName(attributes[0], o);
            String subFieldNameSequence = fieldNameSequence.substring(fieldNameSequence.indexOf(".") + 1);
            value = getFieldValueByNameSequence(subFieldNameSequence, fieldObj);
        }
        return value;
    }

    private static void setFieldValueByName(String fieldName, Object fieldValue, Object o) throws Exception {
        Field field = getFieldByName(fieldName, o.getClass());
        if (field != null) {
            field.setAccessible(true);
            Class<?> fieldType = field.getType();
            if (String.class == fieldType) {
                field.set(o, String.valueOf(fieldValue));
                return;
            } else if (Integer.TYPE == fieldType || Integer.class == fieldType) {
                field.set(o, Integer.valueOf(Integer.parseInt(fieldValue.toString())));
                return;
            } else if (Long.TYPE == fieldType || Long.class == fieldType) {
                field.set(o, Long.valueOf(fieldValue.toString()));
                return;
            } else if (Float.TYPE == fieldType || Float.class == fieldType) {
                field.set(o, Float.valueOf(fieldValue.toString()));
                return;
            } else if (Short.TYPE == fieldType || Short.class == fieldType) {
                field.set(o, Short.valueOf(fieldValue.toString()));
                return;
            } else if (Double.TYPE == fieldType || Double.class == fieldType) {
                field.set(o, Double.valueOf(fieldValue.toString()));
                return;
            } else if (Character.TYPE == fieldType) {
                if (fieldValue != null && fieldValue.toString().length() > 0) {
                    field.set(o, Character.valueOf(fieldValue.toString().charAt(0)));
                    return;
                }
                return;
            } else if (Date.class == fieldType) {
                field.set(o, new SimpleDateFormat(DATE_FORMAT).parse(fieldValue.toString()));
                return;
            } else if (java.sql.Date.class == fieldType) {
                field.set(o, new SimpleDateFormat("yyyy-MM-dd").parse(fieldValue.toString()));
                return;
            } else {
                field.set(o, fieldValue);
                return;
            }
        }
        throw new Exception(String.valueOf(o.getClass().getSimpleName()) + "类不存在字段名 " + fieldName);
    }

    private static void setColumnAutoSize(WritableSheet ws, int extraWith) {
        for (int i = 0; i < ws.getColumns(); i++) {
            int colWith = 0;
            for (int j = 0; j < ws.getRows(); j++) {
                String content = ws.getCell(i, j).getContents().toString();
                int cellWith = content.length();
                if (colWith < cellWith) {
                    colWith = cellWith;
                }
            }
            ws.setColumnView(i, colWith + extraWith);
        }
    }

    private static <T> void fillSheet(WritableSheet sheet, List<T> list, LinkedHashMap<String, String> fieldMap, int firstIndex, int lastIndex) throws Exception {
        String[] enFields = new String[fieldMap.size()];
        String[] cnFields = new String[fieldMap.size()];
        int count = 0;
        Iterator<Map.Entry<String, String>> it = fieldMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            enFields[count] = entry.getKey();
            cnFields[count] = entry.getValue();
            count++;
        }
        for (int i = 0; i < cnFields.length; i++) {
            Label label = new Label(i, 0, cnFields[i]);
            sheet.addCell(label);
        }
        int rowNo = 1;
        for (int index = firstIndex; index <= lastIndex; index++) {
            T item = list.get(index);
            for (int i2 = 0; i2 < enFields.length; i2++) {
                Object objValue = getFieldValueByNameSequence(enFields[i2], item);
                String fieldValue = objValue == null ? "" : objValue.toString();
                Label label2 = new Label(i2, rowNo, fieldValue);
                sheet.addCell(label2);
            }
            rowNo++;
        }
        setColumnAutoSize(sheet, 5);
    }
}
