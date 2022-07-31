package org.springframework.base.system.service;

import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.DataSynchronizeDao;
import org.springframework.base.system.entity.DataSynchronize;
import org.springframework.base.system.persistence.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/DataSynchronizeService.class */
public class DataSynchronizeService {
    @Autowired
    private DataSynchronizeDao dataSynchronizeDao;

    public Page<Map<String, Object>> dataSynchronizeList(Page<Map<String, Object>> page) throws Exception {
        return this.dataSynchronizeDao.dataSynchronizeList(page);
    }

    public boolean deleteDataSynchronize(String[] ids) throws Exception {
        return this.dataSynchronizeDao.deleteDataSynchronize(ids);
    }

    public boolean copyDataSynchronize(String[] ids) throws Exception {
        return this.dataSynchronizeDao.copyDataSynchronize(ids);
    }

    public Map<String, Object> getDataSynchronizeById(String id) throws Exception {
        return this.dataSynchronizeDao.getDataSynchronizeById(id);
    }

    public boolean dataSynchronizeUpdate(DataSynchronize dataSynchronize) throws Exception {
        return this.dataSynchronizeDao.dataSynchronizeUpdate(dataSynchronize);
    }

    public boolean dataSynchronizeUpdateStatus(String dataSynchronizeId, String status) {
        return this.dataSynchronizeDao.dataSynchronizeUpdateStatus(dataSynchronizeId, status);
    }

    public boolean dataSynchronizeUpdateOffset(String dataSynchronizeId, int maxnum) {
        return this.dataSynchronizeDao.dataSynchronizeUpdateOffset(dataSynchronizeId, maxnum);
    }

    public List<Map<String, Object>> getDataSynchronizeListById(String[] ids) throws Exception {
        return this.dataSynchronizeDao.getDataSynchronizeListById(ids);
    }

    public List<Map<String, Object>> getDataSynchronizeList2(String state) {
        return this.dataSynchronizeDao.getDataSynchronizeList2(state);
    }

    public boolean dataSynchronizeLogSave(String status, String comments, String dataSynchronizeId, String times, String totals) {
        return this.dataSynchronizeDao.dataSynchronizeLogSave(status, comments, dataSynchronizeId, times, totals);
    }

    public Page<Map<String, Object>> dataSynchronizeLogList(Page<Map<String, Object>> page, String dataSynchronizeId) throws Exception {
        return this.dataSynchronizeDao.dataSynchronizeLogList(page, dataSynchronizeId);
    }

    public boolean deleteDataSynchronizeLog(String[] ids) throws Exception {
        return this.dataSynchronizeDao.deleteDataSynchronizeLog(ids);
    }

    public boolean deleteDataSynchronizeLogByDS(String id) throws Exception {
        return this.dataSynchronizeDao.deleteDataSynchronizeLogByDS(id);
    }

    public int selectDataSynchronizeNumber() {
        return this.dataSynchronizeDao.selectDataSynchronizeNumber();
    }

    public boolean cleanSynchronizeLog(String dataSynchronizeId) throws Exception {
        return this.dataSynchronizeDao.cleanSynchronizeLog(dataSynchronizeId);
    }

    public boolean batchStartSynchronize(String[] ids) throws Exception {
        return this.dataSynchronizeDao.batchStartSynchronize(ids);
    }

    public boolean batchBlockSynchronize(String[] ids) throws Exception {
        return this.dataSynchronizeDao.batchBlockSynchronize(ids);
    }
}
