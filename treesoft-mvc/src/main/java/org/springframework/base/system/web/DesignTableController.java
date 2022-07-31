package org.springframework.base.system.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.base.system.entity.DmsDto;
import org.springframework.base.system.entity.IdsDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.service.ConfigService;
import org.springframework.base.system.service.DesignTableService;
import org.springframework.base.system.service.MSSQLService;
import org.springframework.base.system.service.OracleService;
import org.springframework.base.system.service.PostgreSQLService;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.base.system.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping({"designTable"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/DesignTableController.class */
public class DesignTableController extends BaseController {
    @Autowired
    private ConfigService configService;
    @Autowired
    private DesignTableService designTableService;
    @Autowired
    private OracleService oracleService;
    @Autowired
    private MSSQLService mssqlService;
    @Autowired
    private PostgreSQLService postgreSQLService;

    @RequestMapping({"selectTableIndexs"})
    @ResponseBody
    public Map<String, Object> selectTableIndexs(HttpServletRequest request, DmsDto dto) {
        Map<String, Object> map = this.configService.getConfigById(dto.getDatabaseConfigId());
        Map<String, Object> map2 = new HashMap<>();
        String databaseType = (String) map.get("databaseType");
        Page<Map<String, Object>> page = getPage(request);
        try {
            if (databaseType.equals("MySQL") || databaseType.equals("MySQL8.0") || databaseType.equals("MariaDB")) {
                page = this.designTableService.selectTableIndexsForMySQL(page, dto);
            } else if (databaseType.equals("Oracle")) {
                page = this.oracleService.selectTableIndexsForOracle(page, dto);
            } else if (databaseType.equals("SQL Server")) {
                page = this.mssqlService.selectTableIndexsForMSSQL(page, dto);
            } else if (databaseType.equals("PostgreSQL")) {
                page = this.postgreSQLService.selectTableIndexsForPostgreSQL(page, dto);
            }
            map2.put("rows", page.getResult());
            map2.put("total", Long.valueOf(page.getTotalCount()));
            map2.put("columns", page.getColumns());
            map2.put("primaryKey", page.getPrimaryKey());
            map2.put("mess", "执行成功！");
            map2.put("status", "success");
        } catch (Exception e) {
            LogUtil.e("执行出错，", e);
            map2.put("mess", "执行出错");
            map2.put("status", "fail");
        }
        return map2;
    }

    @RequestMapping({"indexSave"})
    @ResponseBody
    public String indexSave(HttpServletRequest request, @RequestBody IdsDto dto) {
        JSONObject json = new JSONObject();
        Map<String, Object> map = this.configService.getConfigById(dto.getDatabaseConfigId());
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        String databaseType = (String) map.get("databaseType");
        try {
            JSONArray indexList = JSONArray.parseArray(dto.getCheckedItems());
            for (int i = 0; i < indexList.size(); i++) {
                JSONObject jsonTemp = indexList.getJSONObject(i);
                dto.setIndexName(jsonTemp.getString("indexName"));
                dto.setColumn_name(jsonTemp.getString("columnName"));
                if (databaseType.equals("MySQL") || databaseType.equals("MySQL8.0") || databaseType.equals("MariaDB")) {
                    this.designTableService.indexSaveForMySQL(request, dto, username);
                } else if (databaseType.equals("Oracle")) {
                    this.oracleService.indexSaveForOracle(request, dto, username);
                } else if (databaseType.equals("SQL Server")) {
                    this.mssqlService.indexSaveForMSSQL(request, dto, username);
                } else if (databaseType.equals("PostgreSQL")) {
                    this.postgreSQLService.indexSaveForPostgreSQL(request, dto, username);
                }
            }
            json.put("mess", "操作成功");
            json.put("status", "success");
            return json.toString();
        } catch (Exception e) {
            LogUtil.e("执行出错，", e);
            json.put("mess", "执行出错，" + e.getMessage());
            json.put("status", "fail");
            return json.toString();
        }
    }

    @RequestMapping({"indexDelete"})
    @ResponseBody
    public String indexDelete(HttpServletRequest request, @RequestBody IdsDto dto) {
        JSONObject json = new JSONObject();
        Map<String, Object> map = this.configService.getConfigById(dto.getDatabaseConfigId());
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        String databaseType = (String) map.get("databaseType");
        String isRead = (String) map.get("isRead");
        if (StringUtil.isNotEmpty(isRead) && isRead.equals("1")) {
            json.put("mess", "当前数据库只读，限制该操作");
            json.put("status", "fail");
            return json.toString();
        }
        try {
            if (databaseType.equals("MySQL") || databaseType.equals("MySQL8.0") || databaseType.equals("MariaDB")) {
                if (dto.getIndexName().equals("PRIMARY")) {
                    json.put("mess", "主键索引或唯一索引 不允许删除");
                    json.put("status", "fail");
                    return json.toString();
                }
                this.designTableService.indexDeleteForMySQL(request, dto, username);
            } else if (databaseType.equals("Oracle")) {
                if (dto.getIndexName().equals("PRIMARY")) {
                    json.put("mess", "You cannot delete primary/unique key indexes.");
                    json.put("status", "fail");
                    return json.toString();
                }
                this.oracleService.indexDeleteForOracle(request, dto, username);
            } else if (databaseType.equals("SQL Server")) {
                this.mssqlService.indexDeleteForMSSQL(request, dto, username);
            } else if (databaseType.equals("PostgreSQL")) {
                this.postgreSQLService.indexDeleteForPostgreSQL(request, dto, username);
            }
            json.put("mess", "操作成功");
            json.put("status", "success");
            return json.toString();
        } catch (Exception e) {
            LogUtil.e("删除表的索引  执行出错，", e);
            json.put("mess", "执行出错，" + e.getMessage());
            json.put("status", "fail");
            return json.toString();
        }
    }

    @RequestMapping({"selectTableTriggers"})
    @ResponseBody
    public Map<String, Object> selectTableTriggers(HttpServletRequest request, DmsDto dto) {
        Map<String, Object> map = this.configService.getConfigById(dto.getDatabaseConfigId());
        Map<String, Object> map2 = new HashMap<>();
        String databaseType = (String) map.get("databaseType");
        Page<Map<String, Object>> page = getPage(request);
        try {
            if (databaseType.equals("MySQL") || databaseType.equals("MySQL8.0") || databaseType.equals("MariaDB")) {
                page = this.designTableService.selectTableTriggersForMySQL(page, dto);
            } else if (databaseType.equals("Oracle")) {
                page = this.oracleService.selectTableTriggersForOracle(page, dto);
            } else if (databaseType.equals("SQL Server")) {
                page = this.mssqlService.selectTableTriggersForMSSQL(page, dto);
            } else if (databaseType.equals("PostgreSQL")) {
                page = this.postgreSQLService.selectTableTriggersForPostgreSQL(page, dto);
            }
            map2.put("rows", page.getResult());
            map2.put("total", Long.valueOf(page.getTotalCount()));
            map2.put("columns", page.getColumns());
            map2.put("primaryKey", page.getPrimaryKey());
            map2.put("mess", "执行成功！");
            map2.put("status", "success");
        } catch (Exception e) {
            LogUtil.e("执行出错，", e);
            map2.put("mess", "执行出错");
            map2.put("status", "fail");
        }
        return map2;
    }

    @RequestMapping({"triggerDelete"})
    @ResponseBody
    public String triggerDelete(HttpServletRequest request, @RequestBody IdsDto dto) {
        JSONObject json = new JSONObject();
        Map<String, Object> map = this.configService.getConfigById(dto.getDatabaseConfigId());
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        String databaseType = (String) map.get("databaseType");
        String isRead = (String) map.get("isRead");
        if (StringUtil.isNotEmpty(isRead) && isRead.equals("1")) {
            json.put("mess", "当前数据库只读，限制该操作");
            json.put("status", "fail");
            return json.toString();
        }
        try {
            if (databaseType.equals("MySQL") || databaseType.equals("MySQL8.0") || databaseType.equals("MariaDB")) {
                this.designTableService.triggerDeleteForMySQL(request, dto, username);
            } else if (databaseType.equals("Oracle")) {
                this.oracleService.triggerDeleteForOracle(request, dto, username);
            } else if (databaseType.equals("SQL Server")) {
                this.mssqlService.triggerDeleteForMSSQL(request, dto, username);
            } else if (databaseType.equals("PostgreSQL")) {
                this.postgreSQLService.triggerDeleteForPostgreSQL(request, dto, username);
            }
            json.put("mess", "操作成功");
            json.put("status", "success");
            return json.toString();
        } catch (Exception e) {
            LogUtil.e("删除表的触发器 执行出错，", e);
            json.put("mess", "执行出错，" + e.getMessage());
            json.put("status", "fail");
            return json.toString();
        }
    }

    @RequestMapping({"selectTableForeignKey"})
    @ResponseBody
    public String selectTableForeignKey(HttpServletRequest request, DmsDto dto) {
        JSONObject json = new JSONObject();
        Map<String, Object> map = this.configService.getConfigById(dto.getDatabaseConfigId());
        String databaseType = (String) map.get("databaseType");
        Page<Map<String, Object>> page = getPage(request);
        try {
            if (databaseType.equals("MySQL") || databaseType.equals("MySQL8.0") || databaseType.equals("MariaDB")) {
                page = this.designTableService.selectTableForeignKeyForMySQL(page, dto);
            } else if (!databaseType.equals("Oracle") && !databaseType.equals("SQL Server")) {
                databaseType.equals("PostgreSQL");
            }
            json.put("rows", page.getResult());
            json.put("total", Long.valueOf(page.getTotalCount()));
            json.put("columns", page.getColumns());
            json.put("primaryKey", page.getPrimaryKey());
            json.put("mess", "操作成功");
            json.put("status", "success");
            return json.toString();
        } catch (Exception e) {
            LogUtil.e("查询表 " + dto.getTableName() + " 的外键出错，", e);
            json.put("mess", "执行出错，" + e.getMessage());
            json.put("status", "fail");
            return json.toString();
        }
    }

    @RequestMapping({"deleteForeignKey"})
    @ResponseBody
    public String deleteForeignKey(HttpServletRequest request, @RequestBody IdsDto dto) {
        JSONObject json = new JSONObject();
        Map<String, Object> map = this.configService.getConfigById(dto.getDatabaseConfigId());
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        String databaseType = (String) map.get("databaseType");
        String isRead = (String) map.get("isRead");
        if (StringUtil.isNotEmpty(isRead) && isRead.equals("1")) {
            json.put("mess", "当前数据库只读，限制该操作");
            json.put("status", "fail");
            return json.toString();
        }
        try {
            if (databaseType.equals("MySQL") || databaseType.equals("MySQL8.0") || databaseType.equals("MariaDB")) {
                this.designTableService.deleteForeignKeyForMySQL(request, dto, username);
            } else if (!databaseType.equals("Oracle") && !databaseType.equals("SQL Server")) {
                databaseType.equals("PostgreSQL");
            }
            json.put("mess", "操作成功");
            json.put("status", "success");
            return json.toString();
        } catch (Exception e) {
            LogUtil.e("删除表的外键出错，", e);
            json.put("mess", "执行出错，" + e.getMessage());
            json.put("status", "fail");
            return json.toString();
        }
    }
}
