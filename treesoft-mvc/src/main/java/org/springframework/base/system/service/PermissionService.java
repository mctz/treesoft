package org.springframework.base.system.service;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.base.common.bean.Page;
import org.springframework.base.system.dao.PermissionDao;
import org.springframework.base.system.entity.Config;
import org.springframework.base.system.entity.DataSynchronize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;

@Service
@Transactional(readOnly = true)
public class PermissionService
{
    static final Logger logger = LoggerFactory.getLogger(PermissionService.class);
    
    @Autowired
    private PermissionDao permissionDao;
    
    public List<Map<String, Object>> getAllDataBase(String databaseConfigId)
        throws Exception
    {
        return permissionDao.getAllDataBase(databaseConfigId);
    }
    
    public List<Map<String, Object>> getConfigAllDataBase()
    {
        return permissionDao.getConfigAllDataBase();
    }
    
    public List<Map<String, Object>> getAllDataBaseById(String datascope)
    {
        return permissionDao.getAllDataBaseById(datascope);
    }
    
    public List<Map<String, Object>> getAllTables(String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getAllTables(dbName, databaseConfigId);
    }
    
    public List<Map<String, Object>> getAllViews(String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getAllViews(dbName, databaseConfigId);
    }
    
    public List<Map<String, Object>> getAllFuntion(String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getAllFuntion(dbName, databaseConfigId);
    }
    
    public List<Map<String, Object>> getTableColumns(String dbName, String tableName, String databaseConfigId)
    {
        return permissionDao.getTableColumns(dbName, tableName, databaseConfigId);
    }
    
    public Page<Map<String, Object>> getData(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getData(page, tableName, dbName, databaseConfigId);
    }
    
    public Page<Map<String, Object>> executeSql(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.executeSql(page, sql, dbName, databaseConfigId);
    }
    
    public List<Map<String, Object>> executeSqlForColumns(String sql, String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.executeSqlForColumns(sql, dbName, databaseConfigId);
    }
    
    public List<Map<String, Object>> selectAllDataFromSQLForMysql(String databaseName, String databaseConfigId, String sql)
        throws Exception
    {
        return permissionDao.selectAllDataFromSQLForMysql(databaseName, databaseConfigId, sql);
    }
    
    public List<Map<String, Object>> selectAllDataFromSQLForOracle(String databaseName, String databaseConfigId, String sql)
        throws Exception
    {
        return permissionDao.selectAllDataFromSQLForOracle(databaseName, databaseConfigId, sql);
    }
    
    public List<Map<String, Object>> selectAllDataFromSQLForPostgreSQL(String databaseName, String databaseConfigId, String sql)
        throws Exception
    {
        return permissionDao.selectAllDataFromSQLForPostgreSQL(databaseName, databaseConfigId, sql);
    }
    
    public List<Map<String, Object>> selectAllDataFromSQLForMSSQL(String databaseName, String databaseConfigId, String sql)
        throws Exception
    {
        return permissionDao.selectAllDataFromSQLForMSSQL(databaseName, databaseConfigId, sql);
    }
    
    public List<Map<String, Object>> selectAllDataFromSQLForHive2(String databaseName, String databaseConfigId, String sql)
        throws Exception
    {
        return permissionDao.selectAllDataFromSQLForHive2(databaseName, databaseConfigId, sql);
    }
    
    public List<Map<String, Object>> selectAllDataFromSQLForMongoDB(String databaseName, String databaseConfigId, String sql)
        throws Exception
    {
        return permissionDao.selectAllDataFromSQLForMongoDB(databaseName, databaseConfigId, sql);
    }
    
    public List<Map<String, Object>> selectAllDataFromSQLForMongoDBForExport(String databaseName, String databaseConfigId, String sql)
        throws Exception
    {
        return permissionDao.selectAllDataFromSQLForMongoDBForExport(databaseName, databaseConfigId, sql);
    }
    
    public boolean saveSearchHistory(String name, String sql, String dbName, String userId)
    {
        return permissionDao.saveSearchHistory(name, sql, dbName, userId);
    }
    
    public boolean updateSearchHistory(String id, String name, String sql, String dbName)
    {
        return permissionDao.updateSearchHistory(id, name, sql, dbName);
    }
    
    public boolean deleteSearchHistory(String id)
    {
        return permissionDao.deleteSearchHistory(id);
    }
    
    public List<Map<String, Object>> selectSearchHistory()
    {
        return permissionDao.selectSearchHistory();
    }
    
    public boolean configUpdate(Config config)
    {
        return permissionDao.configUpdate(config);
    }
    
    public List<Map<String, Object>> selectUserByName(String userName)
    {
        return permissionDao.selectUserByName(userName);
    }
    
    public String changePassUpdate(String userId, String newPass)
    {
        permissionDao.updateUserPass(userId, newPass);
        return "success";
    }
    
    public int executeSqlNotRes(String sql, String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.executeSqlNotRes(sql, dbName, databaseConfigId);
    }
    
    public int deleteRows(String databaseName, String tableName, String primaryKey, String[] ids, String databaseConfigId)
        throws Exception
    {
        return permissionDao.deleteRows(databaseName, tableName, primaryKey, ids, databaseConfigId);
    }
    
    public int deleteRowsNew(String databaseName, String tableName, String primaryKey, List<String> condition, String databaseConfigId)
        throws Exception
    {
        return permissionDao.deleteRowsNew(databaseName, tableName, primaryKey, condition, databaseConfigId);
    }
    
    public int saveRows(Map<String, Object> map, String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.saveRows(map, databaseName, tableName, databaseConfigId);
    }
    
    public List<Map<String, Object>> getOneRowById(String databaseName, String tableName, String id, String idValues, String databaseConfigId)
    {
        return permissionDao.getOneRowById(databaseName, tableName, id, idValues, databaseConfigId);
    }
    
    public int updateRows(Map<String, Object> map, String databaseName, String tableName, String id, String idValues, String databaseConfigId)
        throws Exception
    {
        return permissionDao.updateRows(map, databaseName, tableName, id, idValues, databaseConfigId);
    }
    
    public int updateRowsNew(String databaseName, String tableName, List<String> strList, String databaseConfigId)
        throws Exception
    {
        String sql = "";
        for (String str1 : strList)
        {
            if (StringUtils.isEmpty(str1))
            {
                throw new Exception("数据不完整,保存失败!");
            }
            sql = " update  " + databaseName + "." + tableName + str1;
            permissionDao.executeSqlNotRes(sql, databaseName, databaseConfigId);
        }
        return 0;
    }
    
    public String getViewSql(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getViewSql(databaseName, tableName, databaseConfigId);
    }
    
    public List<Map<String, Object>> getTableColumns2(String databaseName, String tableName, String databaseConfigId)
    {
        return permissionDao.getTableColumns2(databaseName, tableName, databaseConfigId);
    }
    
    public List<Map<String, Object>> getTableColumns3(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getTableColumns3(databaseName, tableName, databaseConfigId);
    }
    
    public String getPrimaryKeys(String databaseName, String tableName, String databaseConfigId)
    {
        return permissionDao.getPrimaryKeys(databaseName, tableName, databaseConfigId);
    }
    
    public boolean testConn(String databaseType, String databaseName, String ip, String port, String user, String pass)
    {
        return permissionDao.testConn(databaseType, databaseName, ip, port, user, pass);
    }
    
    public List<Map<String, Object>> selectSqlStudy()
    {
        return permissionDao.selectSqlStudy();
    }
    
    public int saveDesginColumn(Map<String, String> map, String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.saveDesginColumn(map, databaseName, tableName, databaseConfigId);
    }
    
    public int deleteTableColumn(String databaseName, String tableName, String[] ids, String databaseConfigId)
        throws Exception
    {
        return permissionDao.deleteTableColumn(databaseName, tableName, ids, databaseConfigId);
    }
    
    public int updateTableColumn(String updated, String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        if (updated != null)
        {
            JSONArray updateArray = JSONArray.parseArray(updated);
            for (int i = 0; i < updateArray.size(); i++)
            {
                Map<String, Object> map1 = (Map)updateArray.get(i);
                Map<String, Object> maps = new HashMap<>();
                for (String key : map1.keySet())
                {
                    maps.put(key, map1.get(key));
                }
                String idValues = (String)maps.get("TREESOFTPRIMARYKEY");
                permissionDao.updateTableColumn(maps, databaseName, tableName, "columnName", idValues, databaseConfigId);
            }
        }
        return 0;
    }
    
    public int savePrimaryKey(String databaseName, String tableName, String columnName, String columnKey, String databaseConfigId)
        throws Exception
    {
        return permissionDao.savePrimaryKey(databaseName, tableName, columnName, columnKey, databaseConfigId);
    }
    
    public int updateTableNullAble(String databaseName, String tableName, String columnName, String isNullable, String databaseConfigId)
        throws Exception
    {
        return permissionDao.updateTableNullAble(databaseName, tableName, columnName, isNullable, databaseConfigId);
    }
    
    public int upDownColumn(String databaseName, String tableName, String columnName, String columnName2, String databaseConfigId)
        throws Exception
    {
        return permissionDao.upDownColumn(databaseName, tableName, columnName, columnName2, databaseConfigId);
    }
    
    public List<Map<String, Object>> getAllDataBaseForOracle(String databaseConfigId)
    {
        return permissionDao.getAllDataBaseForOracle(databaseConfigId);
    }
    
    public List<Map<String, Object>> getAllTablesForOracle(String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getAllTablesForOracle(dbName, databaseConfigId);
    }
    
    public List<Map<String, Object>> getTableColumns3ForOracle(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getTableColumns3ForOracle(databaseName, tableName, databaseConfigId);
    }
    
    public String getViewSqlForOracle(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getViewSqlForOracle(databaseName, tableName, databaseConfigId);
    }
    
    public List<Map<String, Object>> getAllViewsForOracle(String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getAllViewsForOracle(dbName, databaseConfigId);
    }
    
    public List<Map<String, Object>> getAllFuntionForOracle(String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getAllFuntionForOracle(dbName, databaseConfigId);
    }
    
    public Page<Map<String, Object>> getDataForOracle(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getDataForOracle(page, tableName, dbName, databaseConfigId);
    }
    
    public Page<Map<String, Object>> executeSqlHaveResForOracle(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.executeSqlHaveResForOracle(page, sql, dbName, databaseConfigId);
    }
    
    public int updateRowsNewForOracle(String databaseName, String tableName, List<String> strList, String databaseConfigId)
        throws Exception
    {
        String sql = "";
        for (String str1 : strList)
        {
            if ((str1 == null) || ("".equals(str1)))
            {
                throw new IllegalArgumentException("数据不完整,保存失败!");
            }
            sql = " update  " + tableName + str1;
            permissionDao.executeSqlNotRes(sql, databaseName, databaseConfigId);
        }
        return 0;
    }
    
    public int updateTableNullAbleForOracle(String databaseName, String tableName, String columnName, String isNullable, String databaseConfigId)
        throws Exception
    {
        return permissionDao.updateTableNullAbleForOracle(databaseName, tableName, columnName, isNullable, databaseConfigId);
    }
    
    public int savePrimaryKeyForOracle(String databaseName, String tableName, String columnName, String columnKey, String databaseConfigId)
        throws Exception
    {
        return permissionDao.savePrimaryKeyForOracle(databaseName, tableName, columnName, columnKey, databaseConfigId);
    }
    
    public int saveDesginColumnForOracle(Map<String, String> map, String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.saveDesginColumnForOracle(map, databaseName, tableName, databaseConfigId);
    }
    
    @Transactional
    public int updateTableColumnForOracle(String updated, String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        if (updated != null)
        {
            JSONArray updateArray = JSONArray.parseArray(updated);
            for (int i = 0; i < updateArray.size(); i++)
            {
                Map<String, Object> map1 = (Map)updateArray.get(i);
                Map<String, Object> maps = new HashMap<>();
                for (String key : map1.keySet())
                {
                    maps.put(key, map1.get(key));
                }
                String idValues = (String)maps.get("TREESOFTPRIMARYKEY");
                permissionDao.updateTableColumnForOracle(maps, databaseName, tableName, "columnName", idValues, databaseConfigId);
            }
        }
        return 0;
    }
    
    public int deleteTableColumnForOracle(String databaseName, String tableName, String[] ids, String databaseConfigId)
        throws Exception
    {
        return permissionDao.deleteTableColumnForOracle(databaseName, tableName, ids, databaseConfigId);
    }
    
    public int saveRowsForOracle(Map<String, String> map, String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.saveRowsForOracle(map, databaseName, tableName, databaseConfigId);
    }
    
    public String selectColumnTypeForMySql(String databaseName, String tableName, String column, String databaseConfigId)
        throws Exception
    {
        return permissionDao.selectOneColumnType(databaseName, tableName, column, databaseConfigId);
    }
    
    public String selectColumnTypeForOracle(String databaseName, String tableName, String column, String databaseConfigId)
        throws Exception
    {
        return permissionDao.selectColumnTypeForOracle(databaseName, tableName, column, databaseConfigId);
    }
    
    public String selectColumnTypeForPostgreSQL(String databaseName, String tableName, String column, String databaseConfigId)
        throws Exception
    {
        return permissionDao.selectColumnTypeForPostgreSQL(databaseName, tableName, column, databaseConfigId);
    }
    
    public String selectColumnTypeForMSSQL(String databaseName, String tableName, String column, String databaseConfigId)
        throws Exception
    {
        return permissionDao.selectOneColumnTypeForMSSQL(databaseName, tableName, column, databaseConfigId);
    }
    
    public int deleteRowsNewForOracle(String databaseName, String tableName, String primaryKey, List<String> condition, String databaseConfigId)
        throws Exception
    {
        return permissionDao.deleteRowsNewForOracle(databaseName, tableName, primaryKey, condition, databaseConfigId);
    }
    
    public List<Map<String, Object>> getAllDataBaseForPostgreSQL(String databaseConfigId)
        throws Exception
    {
        return permissionDao.getAllDataBaseForPostgreSQL(databaseConfigId);
    }
    
    public List<Map<String, Object>> getAllTablesForPostgreSQL(String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getAllTablesForPostgreSQL(dbName, databaseConfigId);
    }
    
    public List<Map<String, Object>> getTableColumns3ForPostgreSQL(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getTableColumns3ForPostgreSQL(databaseName, tableName, databaseConfigId);
    }
    
    public List<Map<String, Object>> getAllViewsForPostgreSQL(String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getAllViewsForPostgreSQL(dbName, databaseConfigId);
    }
    
    public List<Map<String, Object>> getAllFuntionForPostgreSQL(String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getAllFuntionForPostgreSQL(dbName, databaseConfigId);
    }
    
    public Page<Map<String, Object>> getDataForPostgreSQL(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getDataForPostgreSQL(page, tableName, dbName, databaseConfigId);
    }
    
    public Page<Map<String, Object>> executeSqlHaveResForPostgreSQL(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.executeSqlHaveResForPostgreSQL(page, sql, dbName, databaseConfigId);
    }
    
    public int deleteRowsNewForPostgreSQL(String databaseName, String tableName, String primaryKey, List<String> condition, String databaseConfigId)
        throws Exception
    {
        return permissionDao.deleteRowsNewForPostgreSQL(databaseName, tableName, primaryKey, condition, databaseConfigId);
    }
    
    public int saveRowsForPostgreSQL(Map<String, String> map, String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.saveRowsForPostgreSQL(map, databaseName, tableName, databaseConfigId);
    }
    
    public int updateRowsNewForPostgreSQL(String databaseName, String tableName, List<String> strList, String databaseConfigId)
        throws Exception
    {
        String sql = "";
        tableName = "\"" + tableName + "\"";
        for (String str1 : strList)
        {
            if ((str1 == null) || ("".equals(str1)))
            {
                throw new Exception("数据不完整,保存失败!");
            }
            sql = " update  " + tableName + str1;
            permissionDao.executeSqlNotRes(sql, databaseName, databaseConfigId);
        }
        return 0;
    }
    
    public int saveDesginColumnForPostgreSQL(Map<String, String> map, String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.saveDesginColumnForPostgreSQL(map, databaseName, tableName, databaseConfigId);
    }
    
    @Transactional
    public int updateTableColumnForPostgreSQL(String updated, String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        if (updated != null)
        {
            JSONArray updateArray = JSONArray.parseArray(updated);
            for (int i = 0; i < updateArray.size(); i++)
            {
                Map<String, Object> map1 = (Map)updateArray.get(i);
                Map<String, Object> maps = new HashMap<>();
                for (String key : map1.keySet())
                {
                    maps.put(key, map1.get(key));
                }
                String idValues = (String)maps.get("TREESOFTPRIMARYKEY");
                permissionDao.updateTableColumnForPostgreSQL(maps, databaseName, tableName, "columnName", idValues, databaseConfigId);
            }
        }
        return 0;
    }
    
    public int deleteTableColumnForPostgreSQL(String databaseName, String tableName, String[] ids, String databaseConfigId)
        throws Exception
    {
        return permissionDao.deleteTableColumnForPostgreSQL(databaseName, tableName, ids, databaseConfigId);
    }
    
    public int updateTableNullAbleForPostgreSQL(String databaseName, String tableName, String columnName, String isNullable, String databaseConfigId)
        throws Exception
    {
        return permissionDao.updateTableNullAbleForPostgreSQL(databaseName, tableName, columnName, isNullable, databaseConfigId);
    }
    
    public int savePrimaryKeyForPostgreSQL(String databaseName, String tableName, String columnName, String columnKey, String databaseConfigId)
        throws Exception
    {
        return permissionDao.savePrimaryKeyForPostgreSQL(databaseName, tableName, columnName, columnKey, databaseConfigId);
    }
    
    public String getViewSqlForPostgreSQL(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getViewSqlForPostgreSQL(databaseName, tableName, databaseConfigId);
    }
    
    public List<Map<String, Object>> getAllDataBaseForMSSQL(String databaseConfigId)
        throws Exception
    {
        return permissionDao.getAllDataBaseForMSSQL(databaseConfigId);
    }
    
    public List<Map<String, Object>> getAllDataBaseForHive2(String databaseConfigId)
        throws Exception
    {
        return permissionDao.getAllDataBaseForHive2(databaseConfigId);
    }
    
    public List<Map<String, Object>> getAllTablesForMSSQL(String dbName, String databaseConfigId)
    {
        return permissionDao.getAllTablesForMSSQL(dbName, databaseConfigId);
    }
    
    public List<Map<String, Object>> getTableColumns3ForMSSQL(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getTableColumns3ForMSSQL(databaseName, tableName, databaseConfigId);
    }
    
    public List<Map<String, Object>> getAllViewsForMSSQL(String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getAllViewsForMSSQL(dbName, databaseConfigId);
    }
    
    public List<Map<String, Object>> getAllViewsForHive2(String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getAllViewsForHive2(dbName, databaseConfigId);
    }
    
    public List<Map<String, Object>> getAllFuntionForMSSQL(String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getAllFuntionForMSSQL(dbName, databaseConfigId);
    }
    
    public Page<Map<String, Object>> getDataForMSSQL(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getDataForMSSQL(page, tableName, dbName, databaseConfigId);
    }
    
    public Page<Map<String, Object>> getDataForHive2(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getDataForHive2(page, tableName, dbName, databaseConfigId);
    }
    
    public Page<Map<String, Object>> executeSqlHaveResForMSSQL(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.executeSqlHaveResForMSSQL(page, sql, dbName, databaseConfigId);
    }
    
    public Page<Map<String, Object>> executeSqlHaveResForHive2(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.executeSqlHaveResForHive2(page, sql, dbName, databaseConfigId);
    }
    
    public int deleteRowsNewForMSSQL(String databaseName, String tableName, String primaryKey, List<String> condition, String databaseConfigId)
        throws Exception
    {
        return permissionDao.deleteRowsNewForMSSQL(databaseName, tableName, primaryKey, condition, databaseConfigId);
    }
    
    public String getViewSqlForMSSQL(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getViewSqlForMSSQL(databaseName, tableName, databaseConfigId);
    }
    
    public int saveRowsForMSSQL(Map<String, String> map, String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.saveRowsForMSSQL(map, databaseName, tableName, databaseConfigId);
    }
    
    public int updateRowsNewForMSSQL(String databaseName, String tableName, List<String> strList, String databaseConfigId)
        throws Exception
    {
        String sql = "";
        for (String str1 : strList)
        {
            if ((str1 == null) || ("".equals(str1)))
            {
                throw new Exception("数据不完整,保存失败!");
            }
            sql = " update  " + tableName + str1;
            permissionDao.executeSqlNotRes(sql, databaseName, databaseConfigId);
        }
        return 0;
    }
    
    public int saveDesginColumnForMSSQL(Map<String, String> map, String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.saveDesginColumnForMSSQL(map, databaseName, tableName, databaseConfigId);
    }
    
    @Transactional
    public int updateTableColumnForMSSQL(String updated, String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        if (updated != null)
        {
            JSONArray updateArray = JSONArray.parseArray(updated);
            for (int i = 0; i < updateArray.size(); i++)
            {
                Map<String, Object> map1 = (Map)updateArray.get(i);
                Map<String, Object> maps = new HashMap<>();
                for (String key : map1.keySet())
                {
                    maps.put(key, map1.get(key));
                }
                String idValues = (String)maps.get("TREESOFTPRIMARYKEY");
                permissionDao.updateTableColumnForMSSQL(maps, databaseName, tableName, "columnName", idValues, databaseConfigId);
            }
        }
        return 0;
    }
    
    public int deleteTableColumnForMSSQL(String databaseName, String tableName, String[] ids, String databaseConfigId)
        throws Exception
    {
        return permissionDao.deleteTableColumnForMSSQL(databaseName, tableName, ids, databaseConfigId);
    }
    
    public int updateTableNullAbleForMSSQL(String databaseName, String tableName, String columnName, String isNullable, String databaseConfigId)
        throws Exception
    {
        return permissionDao.updateTableNullAbleForMSSQL(databaseName, tableName, columnName, isNullable, databaseConfigId);
    }
    
    public int savePrimaryKeyForMSSQL(String databaseName, String tableName, String columnName, String columnKey, String databaseConfigId)
        throws Exception
    {
        return permissionDao.savePrimaryKeyForMSSQL(databaseName, tableName, columnName, columnKey, databaseConfigId);
    }
    
    public List<Map<String, Object>> getAllDataBaseForMongoDB(String databaseConfigId)
        throws Exception
    {
        return permissionDao.getAllDataBaseForMongoDB(databaseConfigId);
    }
    
    public List<Map<String, Object>> getAllTablesForMongoDB(String databaseName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getAllTablesForMongoDB(databaseName, databaseConfigId);
    }
    
    public List<Map<String, Object>> getAllTablesForHive2(String databaseName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getAllTablesForHive2(databaseName, databaseConfigId);
    }
    
    public Page<Map<String, Object>> getDataForMongoDB(Page<Map<String, Object>> page, String tableName, String databaseName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getDataForMongoDB(page, tableName, databaseName, databaseConfigId);
    }
    
    public Page<Map<String, Object>> getDataForMongoJson(Page<Map<String, Object>> page, String tableName, String databaseName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.getDataForMongoJson(page, tableName, databaseName, databaseConfigId);
    }
    
    public Page<Map<String, Object>> executeSqlHaveResForMongoDB(Page<Map<String, Object>> page, String sql, String databaseName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.executeSqlHaveResForMongoDB(page, sql, databaseName, databaseConfigId);
    }
    
    public int deleteRowsNewForMongoDB(String databaseName, String tableName, String primaryKey, List<String> condition, String databaseConfigId)
        throws Exception
    {
        return permissionDao.deleteRowsNewForMongoDB(databaseName, tableName, primaryKey, condition, databaseConfigId);
    }
    
    public int saveRowsForMongoDB(Map<String, String> map, String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.saveRowsForMongoDB(map, databaseName, tableName, databaseConfigId);
    }
    
    public int updateRowsNewForMongoDB(String databaseName, String tableName, List<String> strList, String databaseConfigId)
        throws Exception
    {
        permissionDao.updateRowsNewForMongoDB(databaseName, tableName, strList, databaseConfigId);
        return 0;
    }
    
    public int executeSqlNotResForMongoDB(String sql, String databaseName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.executeSqlNotResForMongoDB(sql, databaseName, databaseConfigId);
    }
    
    public List<Map<String, Object>> selectBackupList(String path)
    {
        List<Map<String, Object>> list = new ArrayList<>();
        List<File> files = getFileSort(path);
        for (File file : files)
        {
            Map<String, Object> map = new HashMap<>();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            map.put("fileName", file.getName());
            map.put("fileLength", file.length() / 1024.0F + " KB");
            map.put("fileModifiedDate", df.format(Long.valueOf(file.lastModified())));
            list.add(map);
        }
        return list;
    }
    
    public static List<File> getFileSort(String path)
    {
        List<File> list = getFiles(path, new ArrayList<>());
        if (CollectionUtils.isNotEmpty(list))
        {
            Collections.sort(list, new Comparator<File>()
            {
                @Override
                public int compare(File file, File newFile)
                {
                    if (file.lastModified() < newFile.lastModified())
                    {
                        return 1;
                    }
                    if (file.lastModified() == newFile.lastModified())
                    {
                        return 0;
                    }
                    return -1;
                }
            });
        }
        return list;
    }
    
    public static List<File> getFiles(String realpath, List<File> files)
    {
        File realFile = new File(realpath);
        if (realFile.isDirectory())
        {
            File[] subfiles = realFile.listFiles();
            for (File file : subfiles)
            {
                if (file.isFile())
                {
                    files.add(file);
                }
            }
        }
        return files;
    }
    
    public boolean backupDatabaseExecute(String databaseName, String tableName, String path, String databaseConfigId)
        throws Exception
    {
        return permissionDao.backupDatabaseExecute(databaseName, tableName, path, databaseConfigId);
    }
    
    public boolean backupDatabaseExecuteForOracle(String databaseName, String tableName, String path, String databaseConfigId)
        throws Exception
    {
        return permissionDao.backupDatabaseExecuteForOracle(databaseName, tableName, path, databaseConfigId);
    }
    
    public boolean backupDatabaseExecuteForPostgreSQL(String databaseName, String tableName, String path, String databaseConfigId)
        throws Exception
    {
        return permissionDao.backupDatabaseExecuteForPostgreSQL(databaseName, tableName, path, databaseConfigId);
    }
    
    public boolean backupDatabaseExecuteForMSSQL(String databaseName, String tableName, String path, String databaseConfigId)
        throws Exception
    {
        return permissionDao.backupDatabaseExecuteForMSSQL(databaseName, tableName, path, databaseConfigId);
    }
    
    public boolean copyTableForMySql(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.copyTableForMySql(databaseName, tableName, databaseConfigId);
    }
    
    public boolean copyTableForOracle(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.copyTableForOracle(databaseName, tableName, databaseConfigId);
    }
    
    public boolean copyTableForPostgreSQL(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.copyTableForPostgreSQL(databaseName, tableName, databaseConfigId);
    }
    
    public boolean renameTableForMySql(String databaseName, String tableName, String databaseConfigId, String newTableName)
        throws Exception
    {
        return permissionDao.renameTableForMySql(databaseName, tableName, databaseConfigId, newTableName);
    }
    
    public boolean renameTableForOracle(String databaseName, String tableName, String databaseConfigId, String newTableName)
        throws Exception
    {
        return permissionDao.renameTableForOracle(databaseName, tableName, databaseConfigId, newTableName);
    }
    
    public boolean renameTableForPostgreSQL(String databaseName, String tableName, String databaseConfigId, String newTableName)
        throws Exception
    {
        return permissionDao.renameTableForPostgreSQL(databaseName, tableName, databaseConfigId, newTableName);
    }
    
    public boolean renameTableForHive2(String databaseName, String tableName, String databaseConfigId, String newTableName)
        throws Exception
    {
        return permissionDao.renameTableForHive2(databaseName, tableName, databaseConfigId, newTableName);
    }
    
    public List<Map<String, Object>> exportDataToSQLForMySQL(String databaseName, String tableName, List<String> condition, String databaseConfigId)
        throws Exception
    {
        return permissionDao.exportDataToSQLForMySQL(databaseName, tableName, condition, databaseConfigId);
    }
    
    public List<Map<String, Object>> exportDataToSQLForOracle(String databaseName, String tableName, List<String> condition, String databaseConfigId)
        throws Exception
    {
        return permissionDao.exportDataToSQLForOracle(databaseName, tableName, condition, databaseConfigId);
    }
    
    public List<Map<String, Object>> exportDataToSQLForPostgreSQL(String databaseName, String tableName, List<String> condition, String databaseConfigId)
        throws Exception
    {
        return permissionDao.exportDataToSQLForPostgreSQL(databaseName, tableName, condition, databaseConfigId);
    }
    
    public List<Map<String, Object>> exportDataToSQLForMSSQL(String databaseName, String tableName, List<String> condition, String databaseConfigId)
        throws Exception
    {
        return permissionDao.exportDataToSQLForMSSQL(databaseName, tableName, condition, databaseConfigId);
    }
    
    public List<Map<String, Object>> exportDataToSQLForMongoDB(String databaseName, String tableName, List<String> condition, String databaseConfigId)
        throws Exception
    {
        return permissionDao.exportDataToSQLForMongoDB(databaseName, tableName, condition, databaseConfigId);
    }
    
    public boolean deleteBackupFile(String[] ids, String path)
        throws Exception
    {
        return permissionDao.deleteBackupFile(ids, path);
    }
    
    public boolean restoreDBFromFile(String databaseName, String fpath, String databaseConfigId)
        throws Exception
    {
        return permissionDao.restoreDBFromFile(databaseName, fpath, databaseConfigId);
    }
    
    public boolean dropTable(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.dropTable(databaseName, tableName, databaseConfigId);
    }
    
    public boolean dropTableForPostgreSQL(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.dropTableForPostgreSQL(databaseName, tableName, databaseConfigId);
    }
    
    public boolean dropTableForOracle(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.dropTableForOracle(databaseName, tableName, databaseConfigId);
    }
    
    public boolean dropDatabase(String databaseName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.dropDatabase(databaseName, databaseConfigId);
    }
    
    public boolean dropTableForMongoDB(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.dropTableForMongoDB(databaseName, tableName, databaseConfigId);
    }
    
    public boolean dropDatabaseForMongoDB(String databaseName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.dropDatabaseForMongoDB(databaseName, databaseConfigId);
    }
    
    public boolean clearTable(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.clearTable(databaseName, tableName, databaseConfigId);
    }
    
    public boolean clearTableForMongoDB(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.clearTableForMongoDB(databaseName, tableName, databaseConfigId);
    }
    
    public List<Map<String, Object>> viewTableMessForMySql(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        List<Map<String, Object>> listAllColumn = permissionDao.viewTableMessForMySql(databaseName, tableName, databaseConfigId);
        List<Map<String, Object>> listAllColumn2 = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(listAllColumn))
        {
            Map<String, Object> map = listAllColumn.get(0);
            Map<String, Object> tempMap = new HashMap<>();
            tempMap.put("propName", "表名");
            tempMap.put("propValue", tableName);
            listAllColumn2.add(tempMap);
            Map<String, Object> tempMap2 = new HashMap<>();
            tempMap2.put("propName", "数据库");
            tempMap2.put("propValue", databaseName);
            listAllColumn2.add(tempMap2);
            Map<String, Object> tempMap4 = new HashMap<>();
            tempMap4.put("propName", "总记录数");
            String num = permissionDao.getTableRows(databaseName, tableName, databaseConfigId);
            tempMap4.put("propValue", num);
            listAllColumn2.add(tempMap4);
            Map<String, Object> tempMap5 = new HashMap<>();
            tempMap5.put("propName", "表类型");
            tempMap5.put("propValue", map.get("ENGINE"));
            listAllColumn2.add(tempMap5);
            Map<String, Object> tempMap6 = new HashMap<>();
            tempMap6.put("propName", "自动递增数值");
            tempMap6.put("propValue", map.get("AUTO_INCREMENT"));
            listAllColumn2.add(tempMap6);
            Map<String, Object> tempMap7 = new HashMap<>();
            tempMap7.put("propName", "栏格式");
            tempMap7.put("propValue", map.get("ROW_FORMAT"));
            listAllColumn2.add(tempMap7);
            Map<String, Object> tempMap8 = new HashMap<>();
            tempMap8.put("propName", "刷新时间");
            tempMap8.put("propValue", map.get("UPDATE_TIME"));
            listAllColumn2.add(tempMap8);
            Map<String, Object> tempMap9 = new HashMap<>();
            tempMap9.put("propName", "创建时间");
            tempMap9.put("propValue", map.get("CREATE_TIME"));
            listAllColumn2.add(tempMap9);
            Map<String, Object> tempMap10 = new HashMap<>();
            tempMap10.put("propName", "校验时间");
            tempMap10.put("propValue", map.get("CHECK_TIME"));
            listAllColumn2.add(tempMap10);
            Map<String, Object> tempMap11 = new HashMap<>();
            tempMap11.put("propName", "索引长度");
            tempMap11.put("propValue", map.get("INDEX_LENGTH"));
            listAllColumn2.add(tempMap11);
            Map<String, Object> tempMap12 = new HashMap<>();
            tempMap12.put("propName", "数据长度");
            tempMap12.put("propValue", map.get("DATA_LENGTH"));
            listAllColumn2.add(tempMap12);
            Map<String, Object> tempMap13 = new HashMap<>();
            tempMap13.put("propName", "最大数据长度");
            tempMap13.put("propValue", map.get("MAX_DATA_LENGTH"));
            listAllColumn2.add(tempMap13);
            Map<String, Object> tempMap14 = new HashMap<>();
            tempMap14.put("propName", "数据空闲");
            tempMap14.put("propValue", map.get("DATA_FREE"));
            listAllColumn2.add(tempMap14);
            Map<String, Object> tempMap15 = new HashMap<>();
            tempMap15.put("propName", "整理");
            tempMap15.put("propValue", map.get("TABLE_COLLATION"));
            listAllColumn2.add(tempMap15);
            Map<String, Object> tempMap16 = new HashMap<>();
            tempMap16.put("propName", "创建选项");
            tempMap16.put("propValue", map.get("CREATE_OPTIONS"));
            listAllColumn2.add(tempMap16);
            Map<String, Object> tempMap17 = new HashMap<>();
            tempMap17.put("propName", "注释");
            tempMap17.put("propValue", map.get("TABLE_COMMENT"));
            listAllColumn2.add(tempMap17);
        }
        return listAllColumn2;
    }
    
    public List<Map<String, Object>> viewTableMessForOracle(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        List<Map<String, Object>> list = permissionDao.viewTableMessForOracle(databaseName, tableName, databaseConfigId);
        List<Map<String, Object>> listAllColumn2 = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list))
        {
            Map<String, Object> map = list.get(0);
            for (Map.Entry<String, Object> entry : map.entrySet())
            {
                Map<String, Object> tempMap = new HashMap<>();
                tempMap.put("propName", entry.getKey());
                tempMap.put("propValue", entry.getValue());
                listAllColumn2.add(tempMap);
            }
        }
        return listAllColumn2;
    }
    
    public List<Map<String, Object>> viewTableMessForPostgreSQL(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        List<Map<String, Object>> list = permissionDao.viewTableMessForPostgreSQL(databaseName, tableName, databaseConfigId);
        List<Map<String, Object>> listAllColumn2 = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list))
        {
            Map<String, Object> map = list.get(0);
            for (Map.Entry<String, Object> entry : map.entrySet())
            {
                Map<String, Object> tempMap = new HashMap<>();
                tempMap.put("propName", entry.getKey());
                tempMap.put("propValue", entry.getValue());
                listAllColumn2.add(tempMap);
            }
        }
        return listAllColumn2;
    }
    
    public List<Map<String, Object>> viewTableMessForHive2(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        List<Map<String, Object>> list = permissionDao.viewTableMessForHive2(databaseName, tableName, databaseConfigId);
        List<Map<String, Object>> listAllColumn2 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
        {
            Map<String, Object> map = list.get(i);
            Map<String, Object> tempMap = new HashMap<>();
            String colName = (String)map.get("colName");
            String dataType = (String)map.get("dataType");
            if (dataType != null)
            {
                dataType = dataType.trim();
                if (dataType.equals("numFiles"))
                {
                    colName = dataType;
                    dataType = (String)map.get("comment");
                }
                if (dataType.equals("numRows"))
                {
                    colName = dataType;
                    dataType = (String)map.get("comment");
                }
                if (dataType.equals("rawDataSize"))
                {
                    colName = dataType;
                    dataType = (String)map.get("comment");
                }
                if (dataType.equals("totalSize"))
                {
                    colName = dataType;
                    dataType = (String)map.get("comment");
                }
                if (dataType.equals("transient_lastDdlTime"))
                {
                    colName = dataType;
                    dataType = (String)map.get("comment");
                }
                if (dataType.equals("field.delim"))
                {
                    colName = dataType;
                    dataType = (String)map.get("comment");
                }
                if (dataType.equals("serialization.format"))
                {
                    colName = dataType;
                    dataType = (String)map.get("comment");
                }
            }
            tempMap.put("propName", colName);
            tempMap.put("propValue", dataType);
            listAllColumn2.add(tempMap);
        }
        return listAllColumn2;
    }
    
    public List<Map<String, Object>> viewTableMessForMSSQL(String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        List<Map<String, Object>> listAllColumn = permissionDao.viewTableMessForMSSQL(databaseName, tableName, databaseConfigId);
        List<Map<String, Object>> listAllColumn2 = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(listAllColumn))
        {
            Map<String, Object> map = listAllColumn.get(0);
            Map<String, Object> tempMap = new HashMap<>();
            tempMap.put("propName", "表名");
            tempMap.put("propValue", tableName);
            listAllColumn2.add(tempMap);
            Map<String, Object> tempMap2 = new HashMap<>();
            tempMap2.put("propName", "数据库");
            tempMap2.put("propValue", databaseName);
            listAllColumn2.add(tempMap2);
            Map<String, Object> tempMap4 = new HashMap<>();
            tempMap4.put("propName", "总记录数");
            tempMap4.put("propValue", map.get("rows"));
            listAllColumn2.add(tempMap4);
            Map<String, Object> tempMap6 = new HashMap<>();
            tempMap6.put("propName", "自动递增数值");
            tempMap6.put("propValue", map.get("indid"));
            listAllColumn2.add(tempMap6);
            Map<String, Object> tempMap7 = new HashMap<>();
            tempMap7.put("propName", "状态");
            tempMap7.put("propValue", map.get("status"));
            listAllColumn2.add(tempMap7);
            Map<String, Object> tempMap8 = new HashMap<>();
            tempMap8.put("propName", "刷新时间");
            tempMap8.put("propValue", map.get("refdate"));
            listAllColumn2.add(tempMap8);
            Map<String, Object> tempMap9 = new HashMap<>();
            tempMap9.put("propName", "创建时间");
            tempMap9.put("propValue", map.get("crdate"));
            listAllColumn2.add(tempMap9);
            Map<String, Object> tempMap11 = new HashMap<>();
            tempMap11.put("propName", "索引长度");
            tempMap11.put("propValue", map.get("indid"));
            listAllColumn2.add(tempMap11);
            Map<String, Object> tempMap12 = new HashMap<>();
            tempMap12.put("propName", "数据长度");
            tempMap12.put("propValue", map.get("rowcnt"));
            listAllColumn2.add(tempMap12);
            Map<String, Object> tempMap13 = new HashMap<>();
            tempMap13.put("propName", "最大数据长度");
            tempMap13.put("propValue", map.get("maxlen"));
            listAllColumn2.add(tempMap13);
        }
        return listAllColumn2;
    }
    
    public List<Map<String, Object>> viewTableMessForMongoDB(String databaseName, String tableName, String databaseConfigId)
    {
        return permissionDao.viewTableMessForMongoDB(databaseName, tableName, databaseConfigId);
    }
    
    public int saveNewTable(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.saveNewTable(insertArray, databaseName, tableName, databaseConfigId);
    }
    
    public int saveNewTableForMSSQL(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.saveNewTableForMSSQL(insertArray, databaseName, tableName, databaseConfigId);
    }
    
    public int saveNewTableForOracle(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.saveNewTableForOracle(insertArray, databaseName, tableName, databaseConfigId);
    }
    
    public int saveNewTableForPostgreSQL(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.saveNewTableForPostgreSQL(insertArray, databaseName, tableName, databaseConfigId);
    }
    
    public int saveNewTableForMongoDB(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId)
    {
        return permissionDao.saveNewTableForMongoDB(insertArray, databaseName, tableName, databaseConfigId);
    }
    
    public Map<String, Object> queryDatabaseStatus(String databaseName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.queryDatabaseStatus(databaseName, databaseConfigId);
    }
    
    public Map<String, Object> queryDatabaseStatusForOracle(String databaseName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.queryDatabaseStatusForOracle(databaseName, databaseConfigId);
    }
    
    public Map<String, Object> queryDatabaseStatusForPostgreSQL(String databaseName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.queryDatabaseStatusForPostgreSQL(databaseName, databaseConfigId);
    }
    
    public List<Map<String, Object>> queryTableSpaceForOracle(String databaseName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.queryTableSpaceForOracle(databaseName, databaseConfigId);
    }
    
    public Map<String, Object> queryDatabaseStatusForMongoDB(String databaseName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.queryDatabaseStatusForMongoDB(databaseName, databaseConfigId);
    }
    
    public List<Map<String, Object>> monitorItemValue(String databaseName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.monitorItemValue(databaseName, databaseConfigId);
    }
    
    public List<Map<String, Object>> monitorItemValueForOracle(String databaseName, String databaseConfigId)
        throws Exception
    {
        return permissionDao.monitorItemValueForOracle(databaseName, databaseConfigId);
    }
    
    public Map<String, Object> getConfig(String id)
    {
        return permissionDao.getConfig(id);
    }
    
    public Page<Map<String, Object>> configList(Page<Map<String, Object>> page)
    {
        return permissionDao.configList(page);
    }
    
    public List<Map<String, Object>> getAllConfigList()
    {
        return permissionDao.getAllConfigList();
    }
    
    public boolean deleteConfig(String[] ids)
    {
        return permissionDao.deleteConfig(ids);
    }
    
    public boolean authorize()
    {
        return permissionDao.authorize();
    }
    
    public Page<Map<String, Object>> dataSynchronizeList(Page<Map<String, Object>> page)
    {
        return permissionDao.dataSynchronizeList(page);
    }
    
    public boolean deleteDataSynchronize(String[] ids)
    {
        return permissionDao.deleteDataSynchronize(ids);
    }
    
    public Map<String, Object> getDataSynchronize(String id)
    {
        return permissionDao.getDataSynchronize(id);
    }
    
    public Map<String, Object> getDataSynchronizeById2(String id)
    {
        return permissionDao.getDataSynchronize(id);
    }
    
    public boolean dataSynchronizeUpdate(DataSynchronize dataSynchronize)
    {
        return permissionDao.dataSynchronizeUpdate(dataSynchronize);
    }
    
    public boolean dataSynchronizeUpdateStatus(String dataSynchronizeId, String status)
    {
        return permissionDao.dataSynchronizeUpdateStatus(dataSynchronizeId, status);
    }
    
    public List<Map<String, Object>> getDataSynchronizeListById(String[] ids)
    {
        return permissionDao.getDataSynchronizeListById(ids);
    }
    
    public List<Map<String, Object>> getDataSynchronizeList2(String state)
    {
        return permissionDao.getDataSynchronizeList2(state);
    }
    
    public boolean insertByDataListForMySQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId)
        throws Exception
    {
        return permissionDao.insertByDataListForMySQL(dataList, databaseName, table, databaseConfigId);
    }
    
    public boolean insertByDataListForOracle(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId)
        throws Exception
    {
        return permissionDao.insertByDataListForOracle(dataList, databaseName, table, databaseConfigId);
    }
    
    public boolean insertByDataListForMSSQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId)
        throws Exception
    {
        return permissionDao.insertByDataListForMSSQL(dataList, databaseName, table, databaseConfigId);
    }
    
    public boolean insertByDataListForHive2(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId)
        throws Exception
    {
        return permissionDao.insertByDataListForHive2(dataList, databaseName, table, databaseConfigId);
    }
    
    public boolean insertByDataListForMongoDB(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId)
        throws Exception
    {
        return permissionDao.insertByDataListForMongoDB(dataList, databaseName, table, databaseConfigId);
    }
    
    public boolean updateByDataListForMySQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification)
        throws Exception
    {
        return permissionDao.updateByDataListForMySQL(dataList, databaseName, table, databaseConfigId, qualification);
    }
    
    public boolean updateByDataListForOracle(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification)
        throws Exception
    {
        return permissionDao.updateByDataListForOracle(dataList, databaseName, table, databaseConfigId, qualification);
    }
    
    public boolean updateByDataListForMSSQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification)
        throws Exception
    {
        return permissionDao.updateByDataListForMSSQL(dataList, databaseName, table, databaseConfigId, qualification);
    }
    
    public boolean updateByDataListForMongoDB(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification)
        throws Exception
    {
        return permissionDao.updateByDataListForMongoDB(dataList, databaseName, table, databaseConfigId, qualification);
    }
    
    public boolean insertOrUpdateByDataListForMySQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification)
        throws Exception
    {
        return permissionDao.insertOrUpdateByDataListForMySQL(dataList, databaseName, table, databaseConfigId, qualification);
    }
    
    public boolean insertOrUpdateByDataListForOracle(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification)
        throws Exception
    {
        return permissionDao.insertOrUpdateByDataListForOracle(dataList, databaseName, table, databaseConfigId, qualification);
    }
    
    public boolean insertOrUpdateByDataListForMSSQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification)
        throws Exception
    {
        return permissionDao.insertOrUpdateByDataListForMSSQL(dataList, databaseName, table, databaseConfigId, qualification);
    }
    
    public boolean insertOrUpdateByDataListForMongoDB(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification)
        throws Exception
    {
        return permissionDao.insertOrUpdateByDataListForMongoDB(dataList, databaseName, table, databaseConfigId, qualification);
    }
    
    public boolean deleteByDataListForMySQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification)
        throws Exception
    {
        return permissionDao.deleteByDataListForMySQL(dataList, databaseName, table, databaseConfigId, qualification);
    }
    
    public boolean deleteByDataListForOracle(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification)
        throws Exception
    {
        return permissionDao.deleteByDataListForOracle(dataList, databaseName, table, databaseConfigId, qualification);
    }
    
    public boolean deleteByDataListForMSSQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification)
        throws Exception
    {
        return permissionDao.deleteByDataListForMSSQL(dataList, databaseName, table, databaseConfigId, qualification);
    }
    
    public boolean deleteByDataListForMongoDB(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification)
    {
        return permissionDao.deleteByDataListForMongoDB(dataList, databaseName, table, databaseConfigId, qualification);
    }
    
    public boolean dataSynchronizeLogSave(String status, String comments, String dataSynchronizeId)
    {
        return permissionDao.dataSynchronizeLogSave(status, comments, dataSynchronizeId);
    }
    
    public Page<Map<String, Object>> dataSynchronizeLogList(Page<Map<String, Object>> page, String dataSynchronizeId)
    {
        return permissionDao.dataSynchronizeLogList(page, dataSynchronizeId);
    }
    
    public boolean deleteDataSynchronizeLog(String[] ids)
    {
        return permissionDao.deleteDataSynchronizeLog(ids);
    }
    
    public boolean deleteDataSynchronizeLogByDS(String id)
    {
        return permissionDao.deleteDataSynchronizeLogByDS(id);
    }
    
    public boolean isTheConfigUsed(String[] ids)
    {
        return permissionDao.isTheConfigUsed(ids);
    }
    
    public int executeQueryForCount(String sql, String dbName, String databaseConfigId)
    {
        return permissionDao.executeQueryForCount(sql, dbName, databaseConfigId);
    }
    
    public boolean saveLog(String sql, String username, String ip)
    {
        return permissionDao.saveLog(sql, username, ip);
    }
}
