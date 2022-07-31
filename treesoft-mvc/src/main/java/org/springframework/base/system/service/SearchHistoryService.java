package org.springframework.base.system.service;

import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.SearchHistoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/SearchHistoryService.class */
public class SearchHistoryService {
    @Autowired
    private SearchHistoryDao searchHistoryDao;

    public boolean saveSearchHistory(String name, String sql, String dbName, String userId, String configId) {
        return this.searchHistoryDao.saveSearchHistory(name, sql, dbName, userId, configId);
    }

    public boolean updateSearchHistory(String id, String name, String sql, String dbName, String configId) {
        return this.searchHistoryDao.updateSearchHistory(id, name, sql, dbName, configId);
    }

    public boolean deleteSearchHistory(String id) {
        return this.searchHistoryDao.deleteSearchHistory(id);
    }

    public List<Map<String, Object>> selectSearchHistory() {
        return this.searchHistoryDao.selectSearchHistory();
    }

    public Map<String, Object> selectSearchHistoryById(String id) {
        return this.searchHistoryDao.selectSearchHistoryById(id);
    }
}
