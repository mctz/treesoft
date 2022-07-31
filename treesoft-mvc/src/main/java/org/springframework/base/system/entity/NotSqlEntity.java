package org.springframework.base.system.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/entity/NotSqlEntity.class */
public class NotSqlEntity implements Serializable {
    private static final long serialVersionUID = -362284023270541690L;
    private String key;
    private String value;
    private String type;
    private List<String> list;
    private Set<String> set;
    private List<Map<String, Object>> listMap;
    private String[] valuek;
    private String[] valuev;
    private String exTime;
    private String noSQLDbName;
    private String databaseConfigId;

    public String getNoSQLDbName() {
        return this.noSQLDbName;
    }

    public void setNoSQLDbName(String noSQLDbName) {
        this.noSQLDbName = noSQLDbName;
    }

    public String getDatabaseConfigId() {
        return this.databaseConfigId;
    }

    public void setDatabaseConfigId(String databaseConfigId) {
        this.databaseConfigId = databaseConfigId;
    }

    public String getExTime() {
        return this.exTime;
    }

    public void setExTime(String exTime) {
        this.exTime = exTime;
    }

    public String[] getValuek() {
        return this.valuek;
    }

    public void setValuek(String[] valuek) {
        this.valuek = valuek;
    }

    public String[] getValuev() {
        return this.valuev;
    }

    public void setValuev(String[] valuev) {
        this.valuev = valuev;
    }

    public List<String> getList() {
        return this.list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Set<String> getSet() {
        return this.set;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setListMap(List<Map<String, Object>> listMap) {
        this.listMap = listMap;
    }

    public List<Map<String, Object>> getListMap() {
        return this.listMap;
    }

    public String toString() {
        String value2;
        String value22 = String.valueOf(String.valueOf("") + "key=" + getKey() + ", ") + "type=" + getType() + ", ";
        if (getValuek() != null) {
            value2 = String.valueOf(value22) + " value=" + Arrays.toString(getValuek());
        } else if (getListMap() != null) {
            value2 = String.valueOf(value22) + " value=" + getListMap().toString();
        } else {
            value2 = String.valueOf(value22) + " value=" + getValue();
        }
        return value2;
    }
}
