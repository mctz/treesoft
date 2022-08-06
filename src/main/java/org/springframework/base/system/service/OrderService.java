package org.springframework.base.system.service;

import org.springframework.base.system.entity.Attach;
import org.springframework.base.system.entity.Order;
import org.springframework.base.system.persistence.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    public Page<Map<String, Object>> orderListData(Page<Map<String, Object>> page, String startTime, String endTime, String userId, String doSql) throws Exception {
        return null;
    }

    public Page<Map<String, Object>> orderAuditListData(Page<Map<String, Object>> page, String startTime, String endTime, String userName, String doSql) throws Exception {
        return null;
    }

    public boolean deleteOrder(String[] ids) throws Exception {
        return false;
    }

    public String orderRun(String id, String runUserId, String runUserName, String ip, List<Attach> list) throws Exception {
        return "";
    }

    public Map<String, Object> getOrderById(String id) throws Exception {
        return null;
    }

    public boolean orderSave(Order order) throws Exception {
        return false;
    }

    public boolean orderUpdate(Order order) throws Exception {
        return false;
    }

    public boolean updateOrderStatus(String orderId, String status) {
        return false;
    }

    public boolean updateOrderStatusByIds(String[] ids, String status, String remark, String auditUserId, String auditUserName) throws Exception {
        return false;
    }

    public Map<String, Object> selectOrderByNumber(String orderNumber) throws Exception {
        return null;
    }

    public List<Map<String, Object>> selectOrderByIds(String[] ids) throws Exception {
        return null;
    }

    public int getAuditOrderNumber() throws Exception {
        return 0;
    }
}
