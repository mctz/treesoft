package org.springframework.base.system.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.base.common.utils.DateUtil;

public class FileWriteBuffer
{
    private static final Logger logger = LoggerFactory.getLogger(FileWriteBuffer.class);
    
    private Connection connection = null;
    
    private PreparedStatement ps = null;
    
    private ResultSet rs = null;
    
    private String filePath = "";
    
    private File fileName = null;
    
    private FileOutputStream fos;
    
    private BufferedOutputStream bos;
    
    private String fileAllPath = "";
    
    Map<String, Object> map = new HashMap<>();
    
    public void init()
    {
    }
    
    public void readDataToFile()
    {
        long totalStart = System.currentTimeMillis();
        init();
        try
        {
            String nowDateStr = DateUtil.getDateTimeStringNotTime();
            this.fileAllPath = (this.filePath + nowDateStr + ".txt");
            this.fileName = new File(this.fileAllPath);
            try
            {
                this.fos = new FileOutputStream(this.fileName);
                this.bos = new BufferedOutputStream(this.fos);
            }
            catch (IOException e)
            {
                logger.error(e.getMessage(), e);
            }
            createFile("");
            String sql_a = (String)this.map.get("SQL");
            this.ps = this.connection.prepareStatement(sql_a);
            this.rs = this.ps.executeQuery();
            int num = 0;
            while (this.rs.next())
            {
                String size = (String)this.map.get("SIZE");
                String s = "";
                for (int i = 1; i <= Integer.parseInt(size); i++)
                {
                    s = s + this.rs.getString(i) + "|";
                }
                s = s.substring(0, s.length() - 1);
                createFile(s);
                num++;
                if (num >= 1000000)
                {
                    break;
                }
                if (num == 0)
                {
                    logger.info("===============清缓存一次===========");
                    try
                    {
                        this.bos.flush();
                    }
                    catch (IOException e)
                    {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            finish();
            closeDB();
        }
        long totalEnd = System.currentTimeMillis();
        logger.info("----总耗时：" + (totalEnd - totalStart) + "毫秒");
    }
    
    public void createFile(String s)
    {
        try
        {
            this.bos.write(s.getBytes());
        }
        catch (IOException e)
        {
            logger.error(e.getMessage(), e);
        }
    }
    
    public void finish()
    {
        try
        {
            this.bos.flush();
            this.bos.close();
            this.fos.close();
        }
        catch (IOException iox)
        {
            logger.error(iox.getMessage(), iox);
        }
    }
    
    public void closeDB()
    {
        try
        {
            if (rs != null)
            {
                rs.close();
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        try
        {
            if (ps != null)
            {
                ps.close();
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        try
        {
            if (connection != null)
            {
                connection.close();
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }
}
