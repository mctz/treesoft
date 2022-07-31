package org.springframework.base.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.base.system.dao.PermissionDao;
import org.springframework.base.system.dao.PostgreSQLDao;
import org.springframework.base.system.dao.RedshiftDao;
import org.springframework.base.system.dao.SybaseDao;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.Constants;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/MonitorGlobalService.class */
public class MonitorGlobalService {
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
    private PermissionDao permissionDao;

    public Page<Map<String, Object>> selectDataBaseAlarm(Page<Map<String, Object>> page) throws Exception {
        page.setTotalCount(Constants.alarmList.size());
        page.setResult(Constants.alarmList);
        return page;
    }

    public boolean addMonitortData(String databaseConfigId, String ip, String configName, String level, String type, String message) {
        Map<String, Object> map3 = new HashMap<>();
        map3.put("ip", ip);
        map3.put("configName", configName);
        map3.put("createTime", DateUtils.getDateTime());
        map3.put("level", level);
        map3.put("type", type);
        map3.put("message", message);
        Constants.alarmList.add(map3);
        int alarmListSize = Constants.alarmList.size();
        if (alarmListSize > 6) {
            for (int i = 6; i < alarmListSize; i++) {
                Constants.alarmList.remove(6);
            }
            return true;
        }
        return true;
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void monitortDataBase() {
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));
            if (props.get("max_threads_connected") != null) {
                Constants.MAX_THREADS_CONNECTED = Integer.parseInt(props.get("max_threads_connected").toString().trim());
            }
            if (props.get("max_qps") != null) {
                Constants.MAX_QPS = Integer.parseInt(props.get("max_qps").toString().trim());
            }
            if (props.get("monitorStatus") != null) {
                Constants.MONITOR_STATUS = (String) props.get("monitorStatus");
            }
        } catch (Exception e) {
            LogUtil.e("2数据库实时监测 出错", e);
            return;
        }
        if (StringUtils.isEmpty(Constants.MONITOR_STATUS) || "0".equals(Constants.MONITOR_STATUS)) {
            return;
        }
        List<Map<String, Object>> list = this.configDao.getAllVaildConfigList();
        for (int i = 0; i < list.size(); i++) {
            String databaseConfigId = list.get(i).get("id").toString();
            Map<String, Object> dbMap = this.configDao.getConfigById(databaseConfigId);
            String ip = dbMap.get("ip").toString();
            String port = dbMap.get("port").toString();
            String configName = dbMap.get("name").toString();
            String userName = dbMap.get("userName").toString();
            String password = dbMap.get("password").toString();
            String databaseName = dbMap.get("databaseName").toString();
            String databaseType = list.get(i).get("databaseType").toString();
            boolean connStatus = this.permissionDao.testConn(databaseType, databaseName, ip, port, userName, password);
            if (!connStatus) {
                addMonitortData(databaseConfigId, ip, configName, Constants.monitorLevel.ERROR.type, "连接状态", "数据库连接失败，请及时排查。");
                this.configDao.updateDBStatus(databaseConfigId, Constants.databaseStatus.ERROR.type);
                return;
            }
            this.configDao.updateDBStatus(databaseConfigId, Constants.databaseStatus.ONLINE.type);
            try {
                if (databaseType.equals("MySQL") || databaseType.equals("MySQL8.0") || databaseType.equals("MariaDB") || databaseType.equals("TiDB")) {
                    try {
                        int threads_connected = this.mysqlDao.queryDatabaseConnected(databaseName, databaseConfigId).intValue();
                        if (threads_connected > Constants.MAX_THREADS_CONNECTED) {
                            addMonitortData(databaseConfigId, ip, configName, Constants.monitorLevel.WARNING.type, "连接数", "数据库连接数超出最大值，当前值为" + threads_connected + "，请及时排查。");
                        }
                    } catch (Exception e2) {
                    }
                }
                if (databaseType.equals("Oracle")) {
                    try {
                        int threads_connected2 = this.oracleDao.queryDatabaseStatusForOracleSession(databaseName, databaseConfigId).intValue();
                        if (threads_connected2 > Constants.MAX_THREADS_CONNECTED) {
                            addMonitortData(databaseConfigId, ip, configName, Constants.monitorLevel.WARNING.type, "连接数", "数据库连接数超出最大值，当前值为" + threads_connected2 + "，请及时排查。");
                        }
                    } catch (Exception e3) {
                    }
                }
                databaseType.equals("HANA2");
                if (databaseType.equals("PostgreSQL")) {
                    try {
                        int threads_connected3 = this.postgreSQLDao.queryDatabaseStatusForPostgreSQLConn(databaseName, databaseConfigId);
                        if (threads_connected3 > Constants.MAX_THREADS_CONNECTED) {
                            addMonitortData(databaseConfigId, ip, configName, Constants.monitorLevel.WARNING.type, "连接数", "数据库连接数超出最大值，当前值为" + threads_connected3 + "，请及时排查。");
                        }
                    } catch (Exception e4) {
                    }
                }
                if (databaseType.equals("SQL Server")) {
                    try {
                        int threads_connected4 = this.msSQLDao.queryDatabaseStatusForMSSQLSession(databaseName, databaseConfigId).intValue();
                        if (threads_connected4 > Constants.MAX_THREADS_CONNECTED) {
                            addMonitortData(databaseConfigId, ip, configName, Constants.monitorLevel.WARNING.type, "连接数", "数据库连接数超出最大值，当前值为" + threads_connected4 + "，请及时排查。");
                        }
                    } catch (Exception e5) {
                    }
                }
                if (databaseType.equals("MongoDB")) {
                    try {
                        int threads_connected5 = this.mongoDBDao.queryDatabaseStatusForMongoDBConnection(databaseName, databaseConfigId).intValue();
                        if (threads_connected5 > Constants.MAX_THREADS_CONNECTED) {
                            addMonitortData(databaseConfigId, ip, configName, Constants.monitorLevel.WARNING.type, "连接数", "数据库连接数超出最大值，当前值为" + threads_connected5 + "，请及时排查。");
                        }
                    } catch (Exception e6) {
                    }
                }
                databaseType.equals("DM7");
            } catch (Exception e7) {
                LogUtil.e("1数据库实时监测 出错", e7);
            }
            LogUtil.e("2数据库实时监测 出错", e);
            return;
        }
    }
}
