package org.springframework.base.system.entity;

import java.util.List;
import java.util.Map;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/entity/SqlDistribute.class */
public class SqlDistribute {
    private String id;
    private String name;
    private String createTime;
    private String updateTime;
    private String doSql;
    private String status;
    private String state;
    private String updateUser;
    private String comments;
    private List<Map<String, String>> databaseConfigList;
    private String[] databaseConfigId;
    private String[] databaseName;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDoSql() {
        return this.doSql;
    }

    public void setDoSql(String doSql) {
        this.doSql = doSql;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdateUser() {
        return this.updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setDatabaseConfigList(List<Map<String, String>> databaseConfigList) {
        this.databaseConfigList = databaseConfigList;
    }

    public List<Map<String, String>> getDatabaseConfigList() {
        return this.databaseConfigList;
    }

    public void setDatabaseConfigId(String[] databaseConfigId) {
        this.databaseConfigId = databaseConfigId;
    }

    public String[] getDatabaseConfigId() {
        return this.databaseConfigId;
    }

    public void setDatabaseName(String[] databaseName) {
        this.databaseName = databaseName;
    }

    public String[] getDatabaseName() {
        return this.databaseName;
    }
}
