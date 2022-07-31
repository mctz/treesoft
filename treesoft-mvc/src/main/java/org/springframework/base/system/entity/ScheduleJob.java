package org.springframework.base.system.entity;

import java.io.Serializable;

public class ScheduleJob implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private String id;
    
    private String name;
    
    private String createDate;
    
    private String updateDate;
    
    private String souceConfigId;
    
    private String souceDataBase;
    
    private String doSql;
    
    private String targetConfigId;
    
    private String targetDataBase;
    
    private String targetTable;
    
    private String cron;
    
    private String operation;
    
    private String comments;
    
    private String status;
    
    private String state;
    
    private String updateUser;
    
    public String getStatus()
    {
        return this.status;
    }
    
    public String getId()
    {
        return this.id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getCreateDate()
    {
        return this.createDate;
    }
    
    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }
    
    public String getUpdateDate()
    {
        return this.updateDate;
    }
    
    public void setUpdateDate(String updateDate)
    {
        this.updateDate = updateDate;
    }
    
    public String getSouceConfigId()
    {
        return this.souceConfigId;
    }
    
    public void setSouceConfigId(String souceConfigId)
    {
        this.souceConfigId = souceConfigId;
    }
    
    public String getSouceDataBase()
    {
        return this.souceDataBase;
    }
    
    public void setSouceDataBase(String souceDataBase)
    {
        this.souceDataBase = souceDataBase;
    }
    
    public String getDoSql()
    {
        return this.doSql;
    }
    
    public void setDoSql(String doSql)
    {
        this.doSql = doSql;
    }
    
    public String getTargetConfigId()
    {
        return this.targetConfigId;
    }
    
    public void setTargetConfigId(String targetConfigId)
    {
        this.targetConfigId = targetConfigId;
    }
    
    public String getTargetDataBase()
    {
        return this.targetDataBase;
    }
    
    public void setTargetDataBase(String targetDataBase)
    {
        this.targetDataBase = targetDataBase;
    }
    
    public String getTargetTable()
    {
        return this.targetTable;
    }
    
    public void setTargetTable(String targetTable)
    {
        this.targetTable = targetTable;
    }
    
    public String getCron()
    {
        return this.cron;
    }
    
    public void setCron(String cron)
    {
        this.cron = cron;
    }
    
    public String getOperation()
    {
        return this.operation;
    }
    
    public void setOperation(String operation)
    {
        this.operation = operation;
    }
    
    public String getComments()
    {
        return this.comments;
    }
    
    public void setComments(String comments)
    {
        this.comments = comments;
    }
    
    public String getState()
    {
        return this.state;
    }
    
    public void setState(String state)
    {
        this.state = state;
    }
    
    public String getUpdateUser()
    {
        return this.updateUser;
    }
    
    public void setUpdateUser(String updateUser)
    {
        this.updateUser = updateUser;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
}
