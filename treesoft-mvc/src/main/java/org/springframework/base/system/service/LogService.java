package org.springframework.base.system.service;

import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.LogDao;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/LogService.class */
public class LogService {
    @Autowired
    private LogDao logDao;

    public Page<Map<String, Object>> logList(Page<Map<String, Object>> page, String startTime, String endTime, String userName, String log) throws Exception {
        return this.logDao.logList(page, startTime, endTime, userName, log);
    }

    public List<Map<String, Object>> allLogList(String startTime, String endTime, String userName, String log) throws Exception {
        return this.logDao.allLogList(startTime, endTime, userName, log);
    }

    public boolean deleteLog(String[] ids) throws Exception {
        return this.logDao.deleteLog(ids);
    }

    public boolean deleteAllLog() throws Exception {
        return this.logDao.deleteAllLog();
    }

    public boolean saveLog(String sql, String username, String ip, String databaseName, String databaseConfigId) {
        try {
            return this.logDao.saveLog(sql, username, ip, databaseName, databaseConfigId);
        } catch (Exception e) {
            LogUtil.e("保存操作日志出错了，", e);
            return false;
        }
    }
}
