package org.springframework.base.system.entity;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/entity/NewQueryDTO.class */
public class NewQueryDTO {
    private static final long serialVersionUID = -8253145776958317075L;
    private String id;
    private String sql;
    private String databaseName;
    private String databaseConfigId;
    private String requestMethod;
    private String requestURL;
    private String requestBody;

    public String getRequestBody() {
        return this.requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getId() {
        return this.id;
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

    public String getDatabaseName() {
        return this.databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseConfigId() {
        return this.databaseConfigId;
    }

    public void setDatabaseConfigId(String databaseConfigId) {
        this.databaseConfigId = databaseConfigId;
    }

    public String getRequestMethod() {
        return this.requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestURL() {
        return this.requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }
}
