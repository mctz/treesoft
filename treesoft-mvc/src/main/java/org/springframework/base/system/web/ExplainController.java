package org.springframework.base.system.web;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.base.system.entity.DmsDto;
import org.springframework.base.system.service.ClickHouseService;
import org.springframework.base.system.service.ConfigService;
import org.springframework.base.system.service.MongoDBService;
import org.springframework.base.system.service.MysqlService;
import org.springframework.base.system.service.OracleService;
import org.springframework.base.system.service.PermissionService;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping({"explain"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/ExplainController.class */
public class ExplainController {
    @Autowired
    private MysqlService mysqlService;
    @Autowired
    private OracleService oracleService;
    @Autowired
    private MongoDBService mongoDBService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private ClickHouseService clickHouseService;
    @Autowired
    private ConfigService configService;

    @RequestMapping({"queryExplain"})
    @ResponseBody
    public Map<String, Object> queryExplainSQL(HttpServletRequest request, DmsDto dto) {
        Map<String, Object> returnMap;
        Map<String, Object> returnMap2 = new HashMap<>();
        Map<String, Object> map = this.configService.getConfigById(dto.getDatabaseConfigId());
        String databaseType = (String) map.get("databaseType");
        try {
            if (databaseType.equals("MySQL") || databaseType.equals("TiDB")) {
                returnMap = this.mysqlService.queryExplainSQLForMysql(dto);
            } else if (databaseType.equals("MariaDB")) {
                returnMap = this.mysqlService.queryExplainSQLForMysql(dto);
            } else if (databaseType.equals("MySQL8.0")) {
                returnMap = this.mysqlService.queryExplainSQLForMysql(dto);
            } else if (databaseType.equals("Oracle")) {
                returnMap = this.oracleService.queryExplainSQLForOracle(dto);
            } else if (databaseType.equals("PostgreSQL")) {
                returnMap2.put("mess", "暂不支持该类型数据库");
                returnMap2.put("status", "fail");
                return returnMap2;
            } else if (databaseType.equals("SQL Server")) {
                returnMap2.put("mess", "暂不支持该类型数据库");
                returnMap2.put("status", "fail");
                return returnMap2;
            } else if (databaseType.equals("MongoDB")) {
                return this.mongoDBService.queryExplainSQLForMongoDB(dto);
            } else {
                if (databaseType.equals("DB2")) {
                    returnMap2.put("mess", "暂不支持该类型数据库");
                    returnMap2.put("status", "fail");
                    return returnMap2;
                } else if (databaseType.equals("ClickHouse")) {
                    returnMap2.put("mess", "暂不支持该类型数据库");
                    returnMap2.put("status", "fail");
                    return returnMap2;
                } else if (databaseType.equals("Redshift")) {
                    returnMap2.put("mess", "暂不支持该类型数据库");
                    returnMap2.put("status", "fail");
                    return returnMap2;
                } else {
                    returnMap2.put("mess", "暂不支持该类型数据库");
                    returnMap2.put("status", "fail");
                    return returnMap2;
                }
            }
            returnMap.put("mess", "查询成功");
            returnMap.put("status", "success");
            return returnMap;
        } catch (Exception e) {
            LogUtil.e("查询SQL语句的执行计划出错，", e);
            returnMap2.put("mess", "查询执行计划出错");
            returnMap2.put("status", "fail");
            return returnMap2;
        }
    }
}
