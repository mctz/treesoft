package org.springframework.base.system.entity;

import java.io.Serializable;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/entity/IdsDto.class */
public class IdsDto implements Serializable {
    private static final long serialVersionUID = -8443145976158317075L;
    private String id;
    private String[] ids;
    private String tableName;
    private String databaseName;
    private String primary_key;
    private String column_name;
    private String column_name2;
    private String is_nullable;
    private String column_key;
    private String checkedItems;
    private String newTableName;
    private String clientId;
    private String remark;
    private String databaseConfigId;
    private String indexName;
    private String triggerName;
    private String foreignKeyName;
    private String noSQLDbName;

    public String getForeignKeyName() {
        return this.foreignKeyName;
    }

    public void setForeignKeyName(String foreignKeyName) {
        this.foreignKeyName = foreignKeyName;
    }

    public String getDatabaseConfigId() {
        return this.databaseConfigId;
    }

    public void setDatabaseConfigId(String databaseConfigId) {
        this.databaseConfigId = databaseConfigId;
    }

    public String getNoSQLDbName() {
        return this.noSQLDbName;
    }

    public void setNoSQLDbName(String noSQLDbName) {
        this.noSQLDbName = noSQLDbName;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getNewTableName() {
        return this.newTableName;
    }

    public void setNewTableName(String newTableName) {
        this.newTableName = newTableName;
    }

    public String getCheckedItems() {
        return this.checkedItems;
    }

    public void setCheckedItems(String checkedItems) {
        this.checkedItems = checkedItems;
    }

    public String getColumn_name2() {
        return this.column_name2;
    }

    public void setColumn_name2(String columnName2) {
        this.column_name2 = columnName2;
    }

    public String getColumn_name() {
        return this.column_name;
    }

    public void setColumn_name(String columnName) {
        this.column_name = columnName;
    }

    public String getIs_nullable() {
        return this.is_nullable;
    }

    public void setIs_nullable(String isNullable) {
        this.is_nullable = isNullable;
    }

    public String getColumn_key() {
        return this.column_key;
    }

    public void setColumn_key(String columnKey) {
        this.column_key = columnKey;
    }

    public String getPrimary_key() {
        return this.primary_key;
    }

    public void setPrimary_key(String primaryKey) {
        this.primary_key = primaryKey;
    }

    public String[] getIds() {
        return this.ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
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

    public String getIndexName() {
        return this.indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getTriggerName() {
        return this.triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }
}
