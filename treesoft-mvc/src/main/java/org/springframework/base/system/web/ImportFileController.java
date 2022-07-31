package org.springframework.base.system.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.base.system.dbUtils.DBUtil2;
import org.springframework.base.system.service.LogService;
import org.springframework.base.system.service.PermissionService;
import org.springframework.base.system.utils.EncodingDetect;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.base.system.utils.NetworkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping({"system/importFile"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/ImportFileController.class */
public class ImportFileController extends BaseController {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private LogService logService;

    @RequestMapping(value = {"fileUpload"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> fileUpload(MultipartFile fileToUpload, String fileType, String spaceType, String importType, String updateRule, String databaseConfigId, String databaseName, String tableName, String demarcate, String haveTitle, HttpServletRequest request) throws Exception {
        String status;
        String mess;
        String filename = fileToUpload.getOriginalFilename();
        LogUtil.i("表 " + tableName + " 导入数据,上传文件 " + filename);
        File file1 = new File(request.getSession().getServletContext().getRealPath("/"));
        File file2 = new File(file1.getParent());
        File file3 = new File(file2.getParent());
        String realPathStr = String.valueOf(file3.getAbsolutePath()) + File.separator + "backup";
        Properties props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));
        String backupDatabasePath = (String) props.get("backupDatabasePath");
        if (!StringUtils.isEmpty(backupDatabasePath)) {
            realPathStr = backupDatabasePath;
        }
        File realPath = new File(String.valueOf(realPathStr) + File.separator + "temp");
        if (!realPath.exists()) {
            realPath.mkdirs();
        }
        File writeFile = new File(realPath + File.separator + filename);
        Map<String, Object> map = new HashMap<>();
        try {
            FileCopyUtils.copy(fileToUpload.getBytes(), writeFile);
            if (fileType.equals("txt")) {
                importTxtFile(writeFile, fileType, spaceType, importType, updateRule, demarcate, haveTitle, databaseConfigId, databaseName, tableName);
            } else if (fileType.equals("csv")) {
                importCsvFile(writeFile, fileType, spaceType, importType, updateRule, demarcate, haveTitle, databaseConfigId, databaseName, tableName);
            } else if (fileType.equals("xls")) {
                importXlsFile(writeFile, fileType, spaceType, importType, updateRule, demarcate, haveTitle, databaseConfigId, databaseName, tableName);
            } else if (fileType.equals("sql")) {
                importSqltFile(writeFile, fileType, spaceType, importType, updateRule, demarcate, haveTitle, databaseConfigId, databaseName, tableName);
            } else if (!fileType.equals("shp")) {
                fileType.equals("other");
            }
            HttpSession session = request.getSession(true);
            String username = (String) session.getAttribute("LOGIN_USER_NAME");
            String ip = NetworkUtil.getIpAddress(request);
            this.logService.saveLog("表 " + tableName + " 导入数据:" + filename, username, ip, databaseName, databaseConfigId);
            mess = "导入数据完成！";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("导入数据出错，" + e);
            mess = "导入出错， " + e.getMessage();
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    public void importTxtFile(File realFile, String fileType, String spaceType, String importType, String updateRule, String demarcate, String haveTitle, String databaseConfigId, String databaseName, String tableName) throws Exception {
        String insertSQL;
        String str;
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String demarcate2 = StringEscapeUtils.unescapeHtml4(demarcate);
        String encoding = EncodingDetect.getJavaEncode(realFile);
        InputStreamReader sourceFile = new InputStreamReader(new FileInputStream(realFile), encoding);
        BufferedReader read = new BufferedReader(sourceFile);
        List<String> insertSQLList = new ArrayList<>();
        List<String> updateSQLList = new ArrayList<>();
        int lineNum = 0;
        String lineRecord = read.readLine();
        String titleColumn = "";
        while (lineRecord != null) {
            if (lineNum == 0 && haveTitle.equals("true")) {
                titleColumn = lineRecord;
            } else {
                String[] lineRecordArr = lineRecord.split(spaceType, -1);
                if (importType.equals("insert")) {
                    if (haveTitle.equals("true")) {
                        insertSQL = "insert into " + databaseName + "." + tableName + " (" + titleColumn + " )  values (";
                    } else {
                        insertSQL = "insert into " + databaseName + "." + tableName + " values (";
                    }
                    for (String tempStr : lineRecordArr) {
                        String tempStr2 = tempStr.trim();
                        if (StringUtils.isEmpty(tempStr2)) {
                            str = String.valueOf(insertSQL) + " null,";
                        } else if (tempStr2.equals("\"\"")) {
                            str = String.valueOf(insertSQL) + " null,";
                        } else if (tempStr2.equals("''")) {
                            str = String.valueOf(insertSQL) + " null,";
                        } else if (tempStr2.equals("null") || tempStr2.equals("NULL")) {
                            str = String.valueOf(insertSQL) + " null,";
                        } else if (!StringUtils.isEmpty(demarcate2) && tempStr2.contains(demarcate2)) {
                            str = String.valueOf(insertSQL) + "'" + tempStr2.substring(1, tempStr2.length() - 1) + "',";
                        } else if (!tempStr2.contains(demarcate2)) {
                            str = String.valueOf(insertSQL) + tempStr2 + ",";
                        } else {
                            str = String.valueOf(insertSQL) + "'" + tempStr2 + "',";
                        }
                        insertSQL = str;
                    }
                    insertSQLList.add(String.valueOf(insertSQL.substring(0, insertSQL.length() - 1)) + ")");
                }
                importType.equals("update");
            }
            lineRecord = read.readLine();
            lineNum++;
        }
        if (insertSQLList.size() > 0) {
            db.updateExecuteBatch(insertSQLList);
            insertSQLList.clear();
        }
        if (updateSQLList.size() > 0) {
            db.updateExecuteBatch(updateSQLList);
            updateSQLList.clear();
        }
        read.close();
        sourceFile.close();
        Thread.sleep(1000L);
        realFile.delete();
    }

    public void importCsvFile(File realFile, String fileType, String spaceType, String importType, String updateRule, String demarcate, String haveTitle, String databaseConfigId, String databaseName, String tableName) throws Exception {
        String insertSQL;
        String str;
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String demarcate2 = StringEscapeUtils.unescapeHtml4(demarcate);
        String encoding = EncodingDetect.getJavaEncode(realFile);
        InputStreamReader sourceFile = new InputStreamReader(new FileInputStream(realFile), encoding);
        BufferedReader read = new BufferedReader(sourceFile);
        List<String> insertSQLList = new ArrayList<>();
        List<String> updateSQLList = new ArrayList<>();
        int lineNum = 0;
        String lineRecord = read.readLine();
        String titleColumn = "";
        while (lineRecord != null) {
            if (lineNum == 0 && haveTitle.equals("true")) {
                titleColumn = lineRecord;
            } else {
                String[] lineRecordArr = lineRecord.split(spaceType, -1);
                if (importType.equals("insert")) {
                    if (haveTitle.equals("true")) {
                        insertSQL = "insert into " + databaseName + "." + tableName + " (" + titleColumn + " )  values (";
                    } else {
                        insertSQL = "insert into " + databaseName + "." + tableName + " values (";
                    }
                    for (String tempStr : lineRecordArr) {
                        String tempStr2 = tempStr.trim();
                        if (StringUtils.isEmpty(tempStr2)) {
                            str = String.valueOf(insertSQL) + " null,";
                        } else if (tempStr2.equals("\"\"")) {
                            str = String.valueOf(insertSQL) + " null,";
                        } else if (tempStr2.equals("''")) {
                            str = String.valueOf(insertSQL) + " null,";
                        } else if (tempStr2.equals("null") || tempStr2.equals("NULL")) {
                            str = String.valueOf(insertSQL) + " null,";
                        } else if (!StringUtils.isEmpty(demarcate2) && tempStr2.contains(demarcate2)) {
                            str = String.valueOf(insertSQL) + "'" + tempStr2.substring(1, tempStr2.length() - 1) + "',";
                        } else if (!tempStr2.contains(demarcate2)) {
                            str = String.valueOf(insertSQL) + tempStr2 + ",";
                        } else {
                            str = String.valueOf(insertSQL) + "'" + tempStr2 + "',";
                        }
                        insertSQL = str;
                    }
                    String insertSQL2 = String.valueOf(insertSQL.substring(0, insertSQL.length() - 1)) + ")";
                    System.out.println(insertSQL2);
                    insertSQLList.add(insertSQL2);
                }
                importType.equals("update");
            }
            lineRecord = read.readLine();
            lineNum++;
        }
        if (insertSQLList.size() > 0) {
            db.updateExecuteBatch(insertSQLList);
            insertSQLList.clear();
        }
        if (updateSQLList.size() > 0) {
            db.updateExecuteBatch(updateSQLList);
            updateSQLList.clear();
        }
        read.close();
        sourceFile.close();
        Thread.sleep(1000L);
        realFile.delete();
    }

    public void importXlsFile(File realFile, String fileType, String spaceType, String importType, String updateRule, String demarcate, String haveTitle, String databaseConfigId, String databaseName, String tableName) throws Exception {
        String str;
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        Workbook book = Workbook.getWorkbook(realFile);
        Sheet sheet = book.getSheet(0);
        int rows = sheet.getRows();
        int columns = sheet.getColumns();
        List<String> insertSQLList = new ArrayList<>();
        List<String> updateSQLList = new ArrayList<>();
        String titleColumn = "";
        for (int i = 0; i < rows; i++) {
            if (i == 0) {
                for (int j = 0; j < columns; j++) {
                    titleColumn = String.valueOf(titleColumn) + sheet.getCell(j, i).getContents() + ",";
                }
                titleColumn = titleColumn.substring(0, titleColumn.length() - 1);
            } else {
                if (importType.equals("insert")) {
                    String insertSQL = "insert into " + databaseName + "." + tableName + "(" + titleColumn + ") values (";
                    for (int j2 = 0; j2 < columns; j2++) {
                        Cell cell = sheet.getCell(j2, i);
                        if (StringUtils.isEmpty(cell.getContents())) {
                            str = String.valueOf(insertSQL) + " null,";
                        } else {
                            str = String.valueOf(insertSQL) + "'" + cell.getContents() + "',";
                        }
                        insertSQL = str;
                    }
                    insertSQLList.add(String.valueOf(insertSQL.substring(0, insertSQL.length() - 1)) + ")");
                }
                importType.equals("update");
            }
        }
        if (insertSQLList.size() > 0) {
            db.updateExecuteBatch(insertSQLList);
            insertSQLList.clear();
        }
        if (updateSQLList.size() > 0) {
            db.updateExecuteBatch(updateSQLList);
            updateSQLList.clear();
        }
        book.close();
        Thread.sleep(1000L);
        realFile.delete();
    }

    public void importSqltFile(File realFile, String fileType, String spaceType, String importType, String updateRule, String demarcate, String haveTitle, String databaseConfigId, String databaseName, String tableName) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String encoding = EncodingDetect.getJavaEncode(realFile);
        BOMInputStream sourceFile0 = new BOMInputStream(new FileInputStream(realFile));
        InputStreamReader sourceFile = new InputStreamReader((InputStream) sourceFile0, encoding);
        BufferedReader read = new BufferedReader(sourceFile);
        List<String> insertSQLList = new ArrayList<>();
        List<String> updateSQLList = new ArrayList<>();
        int lineNum = 0;
        while (true) {
            String lineRecord = read.readLine();
            if (lineRecord != null) {
                if (importType.equals("insert")) {
                    if (StringUtils.isEmpty(lineRecord.trim())) {
                        continue;
                    } else if (lineRecord.trim().toLowerCase().indexOf("insert") != 0) {
                        insertSQLList.clear();
                        throw new Exception("第" + lineNum + "行格式不符合要求，需以insert开头");
                    } else if (lineRecord.toLowerCase().indexOf(tableName.toLowerCase()) == -1) {
                        insertSQLList.clear();
                        throw new Exception("第" + lineNum + "行格式不符合要求，需包含表名" + tableName);
                    } else {
                        insertSQLList.add(lineRecord);
                    }
                } else if (importType.equals("update")) {
                    throw new Exception("暂不支持更新操作");
                }
                lineNum++;
            } else {
                if (insertSQLList.size() > 0) {
                    db.updateExecuteBatch(insertSQLList);
                    insertSQLList.clear();
                }
                if (updateSQLList.size() > 0) {
                    db.updateExecuteBatch(updateSQLList);
                    updateSQLList.clear();
                }
                read.close();
                sourceFile.close();
                Thread.sleep(1000L);
                realFile.delete();
                return;
            }
        }
    }
}
