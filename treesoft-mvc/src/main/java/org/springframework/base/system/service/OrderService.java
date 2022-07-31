package org.springframework.base.system.service;

import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.OrderDao;
import org.springframework.base.system.entity.Attach;
import org.springframework.base.system.entity.Order;
import org.springframework.base.system.persistence.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/OrderService.class */
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    public Page<Map<String, Object>> orderListData(Page<Map<String, Object>> page, String startTime, String endTime, String userId, String doSql) throws Exception {
        return this.orderDao.orderListData(page, startTime, endTime, userId, doSql);
    }

    public Page<Map<String, Object>> orderAuditListData(Page<Map<String, Object>> page, String startTime, String endTime, String userName, String doSql) throws Exception {
        return this.orderDao.orderAuditListData(page, startTime, endTime, userName, doSql);
    }

    public boolean deleteOrder(String[] ids) throws Exception {
        return this.orderDao.deleteOrder(ids);
    }

    public String orderRun(String id, String runUserId, String runUserName, String ip, List<Attach> list) throws Exception {
        return this.orderDao.orderRun(id, runUserId, runUserName, ip, list);
    }

    public Map<String, Object> getOrderById(String id) throws Exception {
        return this.orderDao.getOrderById(id);
    }

    public boolean orderSave(Order order) throws Exception {
        return this.orderDao.orderSave(order);
    }

    public boolean orderUpdate(Order order) throws Exception {
        return this.orderDao.orderUpdate(order);
    }

    public boolean updateOrderStatus(String orderId, String status) {
        return this.orderDao.updateOrderStatus(orderId, status);
    }

    public boolean updateOrderStatusByIds(String[] ids, String status, String remark, String auditUserId, String auditUserName) throws Exception {
        return this.orderDao.updateOrderStatusByIds(ids, status, remark, auditUserId, auditUserName);
    }

    public Map<String, Object> selectOrderByNumber(String orderNumber) throws Exception {
        return this.orderDao.selectOrderByNumber(orderNumber);
    }

    public List<Map<String, Object>> selectOrderByIds(String[] ids) throws Exception {
        return this.orderDao.selectOrderByIds(ids);
    }

    public int getAuditOrderNumber() throws Exception {
        return this.orderDao.getAuditOrderNumber();
    }
}
