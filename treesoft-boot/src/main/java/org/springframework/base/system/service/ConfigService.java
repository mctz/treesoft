package org.springframework.base.system.service;

import org.springframework.base.system.entity.Config;
import org.springframework.base.system.entity.IdsDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ConfigService {

    public Map<String, Object> getConfigById(String id) {
        return null;
    }

    public Map<String, Object> getConfigBySort(String sort) {
        return null;
    }

    public boolean checkSortIsExists(Config config) {
        return false;
    }

    public List<Map<String, Object>> getConfigAllDataBase() throws Exception {
        return null;
    }

    public List<Map<String, Object>> getAllDataBaseById(String datascope) throws Exception {
        return null;
    }

    public boolean configInsert(Config config) throws Exception {
        return false;
    }

    public boolean configUpdate(Config config) throws Exception {
        return false;
    }

    public Page<Map<String, Object>> configList(Page<Map<String, Object>> page, String name, String ip, String userName) throws Exception {
        return null;
    }

    public Page<Map<String, Object>> configListForMonitor(Page<Map<String, Object>> page, String name, String ip, String userName) throws Exception {
        return null;
    }

    public List<Map<String, Object>> getAllConfigList() throws Exception {
        return null;
    }

    public boolean deleteConfig(String[] ids) throws Exception {
        return false;
    }

    public int selectConfigNumber() {
        return 0;
    }

    public boolean isConfigExistsById(String configId) {
        return false;
    }

    public List<Map<String, Object>> dataBaseTypeCount() throws Exception {
        return null;
    }

    public List<Map<String, Object>> getDBTotals() throws Exception {
        return null;
    }

    public boolean updateDBStatus(String databaseConfigId, String status) throws Exception {
        return false;
    }

    public boolean countDBDataTotals(IdsDto dto) throws Exception {
        return false;
    }
}
