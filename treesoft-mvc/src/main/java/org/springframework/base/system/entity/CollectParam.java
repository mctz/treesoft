package org.springframework.base.system.entity;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/entity/CollectParam.class */
public class CollectParam {
    private String id;
    private String paramName;
    private String columnName;
    private String operate;
    private String assemble;
    private String comments;
    private String collectId;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParamName() {
        return this.paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getOperate() {
        return this.operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getCollectId() {
        return this.collectId;
    }

    public void setCollectId(String collectId) {
        this.collectId = collectId;
    }

    public String getAssemble() {
        return this.assemble;
    }

    public void setAssemble(String assemble) {
        this.assemble = assemble;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
