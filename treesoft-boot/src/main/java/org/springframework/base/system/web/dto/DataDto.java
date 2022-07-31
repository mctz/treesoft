package org.springframework.base.system.web.dto;

public class DataDto
{
    private String tableName;
    
    private String databaseName;
    
    private String inserted;
    
    private String updated;
    
    public String getTableName()
    {
        return this.tableName;
    }
    
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
    
    public String getDatabaseName()
    {
        return this.databaseName;
    }
    
    public void setDatabaseName(String databaseName)
    {
        this.databaseName = databaseName;
    }
    
    public String getInserted()
    {
        return this.inserted;
    }
    
    public void setInserted(String inserted)
    {
        this.inserted = inserted;
    }
    
    public String getUpdated()
    {
        return this.updated;
    }
    
    public void setUpdated(String updated)
    {
        this.updated = updated;
    }
}
