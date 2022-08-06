package org.springframework.base.system.utils;

import java.io.File;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;


public class ExcelUtil {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static List<Map<String, String>> parseExcel(File file) throws Exception {
        return null;
    }

    public static void writeExcel(File toFile, String excelTitle, List<String> titleList, List<List<String>> data) throws Exception {

    }

    private static void writeXLSFile(File toFile, String excelTitle, List<String> titleList, List<List<String>> dataList) throws Exception {

    }

    private static void writeXLSXFile(File toFile, String excelTitle, List<String> titleList, List<List<String>> dataList) throws Exception {

    }

    public static void writeXLSXFileTreeSoft(HttpServletResponse response, List<Map<String, Object>> dataList) throws Exception {

    }

    private static List<Map<String, String>> parseXLSFile(File file) throws Exception {
        return null;
    }

    private static List<Map<String, String>> parseXLSXFile(File file) throws Exception {
        return null;
    }



    public static <T> void listToExcel(List<T> list, LinkedHashMap<String, String> fieldMap, String sheetName, int sheetSize, OutputStream out) throws Exception {

    }

    public static <T> void listToExcel(List<T> list, LinkedHashMap<String, String> fieldMap, String sheetName, OutputStream out) throws Exception {
    }

    public static <T> void listToExcel(List<T> list, LinkedHashMap<String, String> fieldMap, String sheetName, int sheetSize, HttpServletResponse response) throws Exception {

    }

    public static <T> void listToExcel(List<T> list, LinkedHashMap<String, String> fieldMap, String sheetName, HttpServletResponse response) throws Exception {
    }

    public static <T> List<T> excelToList(InputStream in, String sheetName, Class<T> entityClass, LinkedHashMap<String, String> fieldMap, String[] uniqueFields) throws Exception {
        return null;
    }

    private static Object getFieldValueByName(String fieldName, Object o) throws Exception {
        return null;
    }

    private static Field getFieldByName(String fieldName, Class<?> clazz) {
        return null;
    }

    private static Object getFieldValueByNameSequence(String fieldNameSequence, Object o) throws Exception {
        return null;
    }

    private static void setFieldValueByName(String fieldName, Object fieldValue, Object o) throws Exception {
    }

}
