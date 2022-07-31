package org.springframework.base.system.entity;

public class Config
{
    private String id;
    
    private String createDate;
    
    private String updateDate;
    
    private String databaseType;
    
    private String driver;
    
    private String url;
    
    private String isdefault;
    
    private String databaseName;
    
    private String userName;
    
    private String password;
    
    private String port;
    
    private String ip;
    
    private String name;
    
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
    
    public String getDatabaseType()
    {
        return this.databaseType;
    }
    
    public void setDatabaseType(String databaseType)
    {
        this.databaseType = databaseType;
    }
    
    public String getDriver()
    {
        return this.driver;
    }
    
    public void setDriver(String driver)
    {
        this.driver = driver;
    }
    
    public String getUrl()
    {
        return this.url;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    public String getDatabaseName()
    {
        return this.databaseName;
    }
    
    public void setDatabaseName(String databaseName)
    {
        this.databaseName = databaseName;
    }
    
    public String getUserName()
    {
        return this.userName;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    public String getId()
    {
        return this.id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getIsdefault()
    {
        return this.isdefault;
    }
    
    public void setIsdefault(String isdefault)
    {
        this.isdefault = isdefault;
    }
    
    public String getPassword()
    {
        return this.password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getPort()
    {
        return this.port;
    }
    
    public void setPort(String port)
    {
        this.port = port;
    }
    
    public String getIp()
    {
        return this.ip;
    }
    
    public void setIp(String ip)
    {
        this.ip = ip;
    }
}
