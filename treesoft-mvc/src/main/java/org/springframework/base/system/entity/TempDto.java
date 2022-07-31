package org.springframework.base.system.entity;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/entity/TempDto.class */
public class TempDto {
    private String id;
    private String sql;
    private String dbName;
    private String tableName;
    private String oldPass;
    private String newPass;
    private String name;
    private String personNumber;
    private String company;
    private String token;
    private boolean isvalidate;
    private String configId;

    public String getId() {
        return this.id;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getDbName() {
        return this.dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getOldPass() {
        return this.oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return this.newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonNumber() {
        return this.personNumber;
    }

    public void setPersonNumber(String personNumber) {
        this.personNumber = personNumber;
    }

    public String getCompany() {
        return this.company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isIsvalidate() {
        return this.isvalidate;
    }

    public void setIsvalidate(boolean isvalidate) {
        this.isvalidate = isvalidate;
    }

    public String getConfigId() {
        return this.configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }
}
