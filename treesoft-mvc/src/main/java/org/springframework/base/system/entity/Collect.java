package org.springframework.base.system.entity;

import java.util.List;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/entity/Collect.class */
public class Collect {
    private String id;
    private String createTime;
    private String updateTime;
    private String name;
    private String alias;
    private String type;
    private String sourceConfigId;
    private String sourceDatabase;
    private String doSql;
    private String model;
    private String operation;
    private String plan;
    private String status;
    private String state;
    private String send;
    private String toplimit;
    private String speed;
    private String appKey;
    private Integer requestNumber;
    private String comments;
    private List<CollectParam> CollectParamList;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceConfigId() {
        return this.sourceConfigId;
    }

    public void setSourceConfigId(String sourceConfigId) {
        this.sourceConfigId = sourceConfigId;
    }

    public String getSourceDatabase() {
        return this.sourceDatabase;
    }

    public void setSourceDatabase(String sourceDatabase) {
        this.sourceDatabase = sourceDatabase;
    }

    public String getDoSql() {
        return this.doSql;
    }

    public void setDoSql(String doSql) {
        this.doSql = doSql;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOperation() {
        return this.operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getPlan() {
        return this.plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getToplimit() {
        return this.toplimit;
    }

    public void setToplimit(String toplimit) {
        this.toplimit = toplimit;
    }

    public String getSpeed() {
        return this.speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSend() {
        return this.send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public List<CollectParam> getCollectParamList() {
        return this.CollectParamList;
    }

    public void setCollectParamList(List<CollectParam> collectParamList) {
        this.CollectParamList = collectParamList;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Integer getRequestNumber() {
        return this.requestNumber;
    }

    public void setRequestNumber(Integer requestNumber) {
        this.requestNumber = requestNumber;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
