package org.springframework.base.system.dao.database;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBackDb
{
    static final Logger LOGGER = LoggerFactory.getLogger(AbstractBackDb.class);
    
    protected File fileName = null;
    
    protected FileOutputStream fos;
    
    protected BufferedOutputStream bos;
    
    protected String fileAllPath = "";
    
    protected String version = "";
    
    public abstract void readDataToFile(String databaseName, String tableName, String path, String databaseConfigId)
        throws Exception;
    
    public void createFile(String s)
    {
        try
        {
            this.bos.write(s.getBytes("UTF-8"));
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    public String bytesToHexString(byte[] src)
    {
        StringBuilder stringBuilder = new StringBuilder("");
        if ((src == null) || (src.length <= 0))
        {
            return null;
        }
        for (int i = 0; i < src.length; i++)
        {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2)
            {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    
    /**
     * 关闭 ResultSet
     * 
     * @param rs
     * @see [类、类#方法、类#成员]
     */
    protected void close(ResultSet rs)
    {
        try
        {
            if (rs != null)
            {
                rs.close();
            }
        }
        catch (SQLException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * 关闭 PreparedStatement
     * 
     * @param ps
     * @see [类、类#方法、类#成员]
     */
    protected void close(PreparedStatement ps)
    {
        try
        {
            if (ps != null)
            {
                ps.close();
            }
        }
        catch (SQLException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * 关闭 Statement
     * 
     * @param ps
     * @see [类、类#方法、类#成员]
     */
    protected void close(Statement stmt)
    {
        try
        {
            if (stmt != null)
            {
                stmt.close();
            }
        }
        catch (SQLException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
