package org.springframework.base.system.service;

import java.util.List;
import java.util.Map;
import org.springframework.base.system.persistence.Page;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    public Page<Map<String, Object>> logList(Page<Map<String, Object>> page, String startTime, String endTime, String userName, String log) throws Exception {
        return null;
    }

    public List<Map<String, Object>> allLogList(String startTime, String endTime, String userName, String log) throws Exception {
        return null;
    }

    public boolean deleteLog(String[] ids) throws Exception {
        return false;
    }

    public boolean deleteAllLog() throws Exception {
        return false;
    }

    public boolean saveLog(String sql, String username, String ip, String databaseName, String databaseConfigId) {
       return false;
    }
}
