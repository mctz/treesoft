package org.springframework.base.system.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/entity/Order.class */
public class Order implements Serializable {
    private static final long serialVersionUID = -5645946189571445870L;
    private String id;
    private String orderName;
    private String createTime;
    private String updateTime;
    private String orderNumber;
    private String orderType;
    private String level;
    private String createUserId;
    private String createUserName;
    private String doSql;
    private String status;
    private String configId;
    private String configName;
    private String databaseName;
    private String runTime;
    private String runUserId;
    private String runUserName;
    private String runStatus;
    private String runMessage;
    private String auditTime;
    private String auditUserId;
    private String auditUserName;
    private String comments;
    private String remark;
    private String attachName;
    private String attachUrl;
    private List<String> attachmentIds;
    private List<Map<String, String>> attachList;

    public List<Map<String, String>> getAttachList() {
        return this.attachList;
    }

    public void setAttachList(List<Map<String, String>> attachList) {
        this.attachList = attachList;
    }

    public String getAttachName() {
        return this.attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public String getAttachUrl() {
        return this.attachUrl;
    }

    public void setAttachUrl(String attachUrl) {
        this.attachUrl = attachUrl;
    }

    public List<String> getAttachmentIds() {
        return this.attachmentIds;
    }

    public void setAttachmentIds(List<String> attachmentIds) {
        this.attachmentIds = attachmentIds;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderName() {
        return this.orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return this.createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getDoSql() {
        return this.doSql;
    }

    public void setDoSql(String doSql) {
        this.doSql = doSql;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConfigId() {
        return this.configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getConfigName() {
        return this.configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getRunTime() {
        return this.runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getRunUserId() {
        return this.runUserId;
    }

    public void setRunUserId(String runUserId) {
        this.runUserId = runUserId;
    }

    public String getRunUserName() {
        return this.runUserName;
    }

    public void setRunUserName(String runUserName) {
        this.runUserName = runUserName;
    }

    public String getRunMessage() {
        return this.runMessage;
    }

    public void setRunMessage(String runMessage) {
        this.runMessage = runMessage;
    }

    public String getAuditTime() {
        return this.auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditUserName() {
        return this.auditUserName;
    }

    public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAuditUserId() {
        return this.auditUserId;
    }

    public void setAuditUserId(String auditUserId) {
        this.auditUserId = auditUserId;
    }

    public String getRunStatus() {
        return this.runStatus;
    }

    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus;
    }

    public String getOrderType() {
        return this.orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
