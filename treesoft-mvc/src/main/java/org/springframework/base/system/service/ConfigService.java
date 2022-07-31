package org.springframework.base.system.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.ClickHouseDao;
import org.springframework.base.system.dao.ConfigDao;
import org.springframework.base.system.dao.Db2Dao;
import org.springframework.base.system.dao.Dm7Dao;
import org.springframework.base.system.dao.ESDao;
import org.springframework.base.system.dao.Hana2Dao;
import org.springframework.base.system.dao.Hive2Dao;
import org.springframework.base.system.dao.KingbaseDao;
import org.springframework.base.system.dao.MSSQLDao;
import org.springframework.base.system.dao.MongoDBDao;
import org.springframework.base.system.dao.MysqlDao;
import org.springframework.base.system.dao.OracleDao;
import org.springframework.base.system.dao.PostgreSQLDao;
import org.springframework.base.system.dao.RedshiftDao;
import org.springframework.base.system.dao.ShenTongDao;
import org.springframework.base.system.dao.SybaseDao;
import org.springframework.base.system.entity.Config;
import org.springframework.base.system.entity.IdsDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/ConfigService.class */
public class ConfigService {
    @Autowired
    private ConfigDao configDao;
    @Autowired
    private MysqlDao mysqlDao;
    @Autowired
    private OracleDao oracleDao;
    @Autowired
    private MSSQLDao msSQLDao;
    @Autowired
    private Hana2Dao hana2Dao;
    @Autowired
    private Hive2Dao hive2Dao;
    @Autowired
    private MongoDBDao mongoDBDao;
    @Autowired
    private Db2Dao db2Dao;
    @Autowired
    private Dm7Dao dm7Dao;
    @Autowired
    private SybaseDao sybaseDao;
    @Autowired
    private KingbaseDao kingbaseDao;
    @Autowired
    private PostgreSQLDao postgreSQLDao;
    @Autowired
    private ClickHouseDao clickHouseDao;
    @Autowired
    private RedshiftDao redshiftDao;
    @Autowired
    private ESDao esDao;
    @Autowired
    private ShenTongDao shenTongDao;

    public Map<String, Object> getConfigById(String id) {
        return this.configDao.getConfigById(id);
    }

    public Map<String, Object> getConfigBySort(String sort) {
        return this.configDao.getConfigBySort(sort);
    }

    public boolean checkSortIsExists(Config config) {
        return this.configDao.checkSortIsExists(config);
    }

    public List<Map<String, Object>> getConfigAllDataBase() throws Exception {
        return this.configDao.getConfigAllDataBase();
    }

    public List<Map<String, Object>> getAllDataBaseById(String datascope) throws Exception {
        return this.configDao.getAllDataBaseById(datascope);
    }

    public boolean configInsert(Config config) throws Exception {
        return this.configDao.configInsert(config);
    }

    public boolean configUpdate(Config config) throws Exception {
        return this.configDao.configUpdate(config);
    }

    public Page<Map<String, Object>> configList(Page<Map<String, Object>> page, String name, String ip, String userName) throws Exception {
        return this.configDao.configList(page, name, ip, userName);
    }

    public Page<Map<String, Object>> configListForMonitor(Page<Map<String, Object>> page, String name, String ip, String userName) throws Exception {
        return this.configDao.configListForMonitor(page, name, ip, userName);
    }

    public List<Map<String, Object>> getAllConfigList() throws Exception {
        return this.configDao.getAllConfigList();
    }

    public boolean deleteConfig(String[] ids) throws Exception {
        return this.configDao.deleteConfig(ids);
    }

    public int selectConfigNumber() {
        return this.configDao.selectConfigNumber();
    }

    public boolean isConfigExistsById(String configId) {
        return this.configDao.isConfigExistsById(configId);
    }

    public List<Map<String, Object>> dataBaseTypeCount() throws Exception {
        return this.configDao.dataBaseTypeCount();
    }

    public List<Map<String, Object>> getDBTotals() throws Exception {
        List<Map<String, Object>> list = this.configDao.getDBTotals();
        return list;
    }

    public boolean updateDBStatus(String databaseConfigId, String status) throws Exception {
        return this.configDao.updateDBStatus(databaseConfigId, status);
    }

    public boolean countDBDataTotals(IdsDto dto) throws Exception {
        List<Map<String, Object>> dbList;
        new ArrayList();
        new ArrayList();
        if (dto.getIds().length > 0) {
            dbList = this.configDao.getAllVaildConfigListById(dto);
        } else {
            dbList = this.configDao.getAllVaildConfigList();
        }
        for (int i = 0; i < dbList.size(); i++) {
            String dbName = dbList.get(i).get("databaseName").toString();
            String databaseConfigId = dbList.get(i).get("id").toString();
            String databaseType = dbList.get(i).get("databaseType").toString();
            int dataTotals = 0;
            if (databaseType.equals("MySQL") || databaseType.equals("TiDB")) {
                List<Map<String, Object>> tableList = this.mysqlDao.getAllTables(dbName, databaseConfigId);
                Iterator<Map<String, Object>> it = tableList.iterator();
                while (it.hasNext()) {
                    Map<String, Object> map = it.next();
                    String tableName = (String) map.get("TABLE_NAME");
                    Integer rowCount = this.mysqlDao.getTableRows(dbName, tableName, databaseConfigId);
                    dataTotals += rowCount.intValue();
                }
            }
            if (databaseType.equals("MariaDB")) {
                List<Map<String, Object>> tableList2 = this.mysqlDao.getAllTables(dbName, databaseConfigId);
                Iterator<Map<String, Object>> it2 = tableList2.iterator();
                while (it2.hasNext()) {
                    Map<String, Object> map2 = it2.next();
                    String tableName2 = (String) map2.get("TABLE_NAME");
                    Integer rowCount2 = this.mysqlDao.getTableRows(dbName, tableName2, databaseConfigId);
                    dataTotals += rowCount2.intValue();
                }
            }
            if (databaseType.equals("MySQL8.0")) {
                List<Map<String, Object>> tableList3 = this.mysqlDao.getAllTables(dbName, databaseConfigId);
                Iterator<Map<String, Object>> it3 = tableList3.iterator();
                while (it3.hasNext()) {
                    Map<String, Object> map3 = it3.next();
                    String tableName3 = (String) map3.get("TABLE_NAME");
                    Integer rowCount3 = this.mysqlDao.getTableRows(dbName, tableName3, databaseConfigId);
                    dataTotals += rowCount3.intValue();
                }
            }
            if (databaseType.equals("Oracle")) {
                List<Map<String, Object>> tableList4 = this.oracleDao.getAllTablesForOracle(dbName, databaseConfigId);
                Iterator<Map<String, Object>> it4 = tableList4.iterator();
                while (it4.hasNext()) {
                    Map<String, Object> map4 = it4.next();
                    String tableName4 = (String) map4.get("TABLE_NAME");
                    Integer totals = this.oracleDao.getTableRowsForOracle(dbName, tableName4, databaseConfigId);
                    dataTotals += totals.intValue();
                }
            }
            if (databaseType.equals("HANA2")) {
                List<Map<String, Object>> tableList5 = this.hana2Dao.getAllTablesForHana2(dbName, databaseConfigId);
                Iterator<Map<String, Object>> it5 = tableList5.iterator();
                while (it5.hasNext()) {
                    Map<String, Object> map5 = it5.next();
                    String tableName5 = (String) map5.get("TABLE_NAME");
                    Integer totals2 = this.hana2Dao.getTableRowsForHana2(dbName, tableName5, databaseConfigId);
                    dataTotals += totals2.intValue();
                }
            }
            if (databaseType.equals("PostgreSQL")) {
                List<Map<String, Object>> tableList6 = this.postgreSQLDao.getAllTablesForPostgreSQL(dbName, databaseConfigId);
                Iterator<Map<String, Object>> it6 = tableList6.iterator();
                while (it6.hasNext()) {
                    Map<String, Object> map6 = it6.next();
                    String tableName6 = (String) map6.get("TABLE_NAME");
                    Integer totals3 = this.postgreSQLDao.getTableRowsForPostgreSQL(dbName, tableName6, databaseConfigId);
                    dataTotals += totals3.intValue();
                }
            }
            if (databaseType.equals("SQL Server")) {
                List<Map<String, Object>> tableList7 = this.msSQLDao.getAllTablesForMSSQL(dbName, databaseConfigId);
                Iterator<Map<String, Object>> it7 = tableList7.iterator();
                while (it7.hasNext()) {
                    Map<String, Object> map7 = it7.next();
                    String tableName7 = (String) map7.get("TABLE_NAME");
                    Integer totals4 = this.msSQLDao.getTableRowsForMSSQL(dbName, tableName7, databaseConfigId);
                    dataTotals += totals4.intValue();
                }
            }
            if (databaseType.equals("MongoDB")) {
                List<Map<String, Object>> tableList8 = this.mongoDBDao.getAllTablesForMongoDB(dbName, databaseConfigId);
                Iterator<Map<String, Object>> it8 = tableList8.iterator();
                while (it8.hasNext()) {
                    Map<String, Object> map8 = it8.next();
                    String tableName8 = (String) map8.get("TABLE_NAME");
                    Integer totals5 = this.mongoDBDao.getTableRowsForMongoDB(dbName, tableName8, databaseConfigId);
                    dataTotals += totals5.intValue();
                }
            }
            databaseType.equals("Hive2");
            databaseType.equals("Cache");
            if (databaseType.equals("DB2")) {
                List<Map<String, Object>> tableList9 = this.db2Dao.getAllTablesForDb2(dbName, databaseConfigId);
                Iterator<Map<String, Object>> it9 = tableList9.iterator();
                while (it9.hasNext()) {
                    Map<String, Object> map9 = it9.next();
                    String tableName9 = (String) map9.get("TABLE_NAME");
                    Integer totals6 = this.db2Dao.getTableRowsForDb2(dbName, tableName9, databaseConfigId);
                    dataTotals += totals6.intValue();
                }
            }
            if (databaseType.equals("DM7")) {
                List<Map<String, Object>> tableList10 = this.dm7Dao.getAllTablesForDm7(dbName, databaseConfigId);
                Iterator<Map<String, Object>> it10 = tableList10.iterator();
                while (it10.hasNext()) {
                    Map<String, Object> map10 = it10.next();
                    String tableName10 = (String) map10.get("TABLE_NAME");
                    Integer totals7 = this.dm7Dao.getTableRowsForDm7(dbName, tableName10, databaseConfigId);
                    dataTotals += totals7.intValue();
                }
            }
            if (databaseType.equals("ShenTong")) {
                String userNameTemp = dbList.get(i).get("userName").toString();
                List<Map<String, Object>> tableList11 = this.shenTongDao.getAllTablesForShenTong(userNameTemp, databaseConfigId);
                Iterator<Map<String, Object>> it11 = tableList11.iterator();
                while (it11.hasNext()) {
                    Map<String, Object> map11 = it11.next();
                    String tableName11 = (String) map11.get("TABLE_NAME");
                    Integer totals8 = this.shenTongDao.getTableRows(userNameTemp, tableName11, databaseConfigId);
                    dataTotals += totals8.intValue();
                }
            }
            if (databaseType.equals("Sybase")) {
                List<Map<String, Object>> tableList12 = this.sybaseDao.getAllTablesForSybase(dbName, databaseConfigId);
                Iterator<Map<String, Object>> it12 = tableList12.iterator();
                while (it12.hasNext()) {
                    Map<String, Object> map12 = it12.next();
                    String tableName12 = (String) map12.get("TABLE_NAME");
                    Integer totals9 = this.sybaseDao.getTableRowsForSybase(dbName, tableName12, databaseConfigId);
                    dataTotals += totals9.intValue();
                }
            }
            if (databaseType.equals("Kingbase")) {
                List<Map<String, Object>> tableList13 = this.kingbaseDao.getAllTablesForKingbase(dbName, databaseConfigId);
                Iterator<Map<String, Object>> it13 = tableList13.iterator();
                while (it13.hasNext()) {
                    Map<String, Object> map13 = it13.next();
                    String tableName13 = (String) map13.get("TABLE_NAME");
                    Integer totals10 = this.kingbaseDao.getTableRowsForKingbase(dbName, tableName13, databaseConfigId);
                    dataTotals += totals10.intValue();
                }
            }
            databaseType.equals("Informix");
            databaseType.equals("ClickHouse");
            databaseType.equals("Redshift");
            databaseType.equals("ES");
            Thread.sleep(2000L);
            this.configDao.updateDBDataTotals(databaseConfigId, Integer.valueOf(dataTotals));
        }
        return true;
    }
}
