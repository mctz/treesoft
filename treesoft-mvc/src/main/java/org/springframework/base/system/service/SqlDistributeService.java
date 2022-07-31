package org.springframework.base.system.service;

import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.SqlDistributeDao;
import org.springframework.base.system.entity.SqlDistribute;
import org.springframework.base.system.persistence.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/SqlDistributeService.class */
public class SqlDistributeService {
    @Autowired
    private SqlDistributeDao sqlDistributeDao;

    public Page<Map<String, Object>> sqlDistributeList(Page<Map<String, Object>> page) throws Exception {
        return this.sqlDistributeDao.sqlDistributeList(page);
    }

    public boolean sqlDistributeDelete(String[] ids) throws Exception {
        return this.sqlDistributeDao.sqlDistributeDelete(ids);
    }

    public Map<String, Object> getSqlDistribute(String id) throws Exception {
        return this.sqlDistributeDao.getSqlDistribute(id);
    }

    public List<Map<String, Object>> getSqlDistributeByIds(String[] ids) throws Exception {
        return this.sqlDistributeDao.getSqlDistributeByIds(ids);
    }

    public List<Map<String, Object>> getSqlDistributeDatabaseConfigList(String sql_distribute_id) throws Exception {
        return this.sqlDistributeDao.getSqlDistributeDatabaseConfigList(sql_distribute_id);
    }

    public boolean sqlDistributeUpdate(SqlDistribute sqlDistribute) throws Exception {
        return this.sqlDistributeDao.sqlDistributeUpdate(sqlDistribute);
    }

    public boolean sqlDistributeUpdateStatus(String sqlDistributeId, String status) {
        return this.sqlDistributeDao.sqlDistributeUpdateStatus(sqlDistributeId, status);
    }

    public Page<Map<String, Object>> sqlDistributeLogList(Page<Map<String, Object>> page, String taskId) throws Exception {
        return this.sqlDistributeDao.sqlDistributeLogList(page, taskId);
    }

    public List<Map<String, Object>> getTaskList2(String state) {
        return this.sqlDistributeDao.getTaskList2(state);
    }

    public boolean deleteTaskLog(String[] ids) throws Exception {
        return this.sqlDistributeDao.deleteTaskLog(ids);
    }

    public boolean saveSqlDistributeLog(String configId, String databaseName, String status, String comments, String sqlDistributeId) {
        return this.sqlDistributeDao.saveSqlDistributeLog(configId, databaseName, status, comments, sqlDistributeId);
    }

    public boolean deleteTaskLogByDS(String sqlDistributeId) throws Exception {
        return this.sqlDistributeDao.deleteTaskLogByDS(sqlDistributeId);
    }

    public boolean copySqlDistribute(String[] ids) throws Exception {
        return this.sqlDistributeDao.copySqlDistribute(ids);
    }
}
