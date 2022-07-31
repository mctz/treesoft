package org.springframework.base.system.entity;

import java.io.Serializable;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/entity/DmsDto.class */
public class DmsDto implements Serializable {
    private static final long serialVersionUID = 1857850391675091932L;
    private String databaseConfigId;
    private String tableName;
    private String databaseName;
    private String dbName;
    private String sql;

    public String getDatabaseConfigId() {
        return this.databaseConfigId;
    }

    public void setDatabaseConfigId(String databaseConfigId) {
        this.databaseConfigId = databaseConfigId;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDbName() {
        return this.dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
