package com.fly.test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.base.system.utils.CryptoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Junit 单元测试
 * 
 * @author 00fly
 * @version [版本号, 2018-11-06]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RunWith(SpringRunner.class)
@ContextConfiguration({"/applicationContext.xml"})
public class ServiceTest
{
    static final Logger LOGGER = LoggerFactory.getLogger(ServiceTest.class);
    
    @Autowired
    ApplicationContext applicationContext;
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    /**
     * 业务数据源,key为treesoft_config表id
     */
    private Map<String, DataSource> dsMap = new ConcurrentHashMap<>();
    
    @Before
    public void before()
    {
        LOGGER.info("★★★★★★★★ ApplicationContext = {}", applicationContext);
        int i = 1;
        for (String beanName : applicationContext.getBeanDefinitionNames())
        {
            LOGGER.info("{}.\t{}", i, beanName);
            i++;
        }
    }
    
    /**
     * 初始化dsMap
     * 
     * @see [类、类#方法、类#成员]
     */
    @PostConstruct
    private void initDs()
    {
        LOGGER.info("★★★★ initDs()");
        String sql = " select id, databaseType, userName, password, port, ip, url from treesoft_config";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> map : list)
        {
            String id = String.valueOf(map.get("id"));
            String url = (String)map.get("url");
            String userName = (String)map.get("userName");
            String password = CryptoUtil.decode(map.get("password").toString());
            String databaseType = (String)map.get("databaseType");
            DataSource dataSource = null;
            switch (databaseType)
            {
                case "MySql":
                    dataSource = new DataSource();
                    dataSource.setPoolProperties(initPoolProperties(url, "com.mysql.jdbc.Driver", userName, password));
                    break;
                case "MariaDB":
                    dataSource = new DataSource();
                    dataSource.setPoolProperties(initPoolProperties(url, "com.mysql.jdbc.Driver", userName, password));
                    break;
                case "Oracle":
                    dataSource = new DataSource();
                    dataSource.setPoolProperties(initPoolProperties(url, "oracle.jdbc.driver.OracleDriver", userName, password));
                    break;
                case "PostgreSQL":
                    dataSource = new DataSource();
                    dataSource.setPoolProperties(initPoolProperties(url, "org.postgresql.Driver", userName, password));
                    break;
                case "MSSQL":
                    dataSource = new DataSource();
                    dataSource.setPoolProperties(initPoolProperties(url, "com.microsoft.sqlserver.jdbc.SQLServerDriver", userName, password));
                    break;
                case "Hive2":
                    dataSource = new DataSource();
                    dataSource.setPoolProperties(initPoolProperties(url, "org.apache.hive.jdbc.HiveDriver", userName, password));
                    break;
                default:
                    break;
            }
            if (dataSource != null)
            {
                dsMap.put(id, dataSource);
            }
        }
    }
    
    /**
     * 初始化数据源配置
     * 
     * @param url
     * @param driver
     * @param username
     * @param password
     * @return
     * @see [类、类#方法、类#成员]
     */
    private PoolProperties initPoolProperties(String url, String driver, String username, String password)
    {
        PoolProperties properties = new PoolProperties();
        properties.setUrl(url);
        properties.setDriverClassName(driver);
        properties.setUsername(username);
        properties.setPassword(password);
        properties.setFairQueue(true);
        properties.setJmxEnabled(true);
        properties.setTestWhileIdle(false);
        properties.setTestOnBorrow(true);
        properties.setValidationQuery("SELECT 1");
        properties.setTestOnReturn(false);
        properties.setValidationInterval(30000);
        properties.setTimeBetweenEvictionRunsMillis(30000);
        properties.setMaxActive(100);
        properties.setInitialSize(10);
        properties.setMaxWait(10000);
        properties.setRemoveAbandonedTimeout(60);
        properties.setMinEvictableIdleTimeMillis(30000);
        properties.setMinIdle(10);
        properties.setLogAbandoned(true);
        properties.setRemoveAbandoned(true);
        properties.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        return properties;
    }
    
    @Test
    public void test()
    {
        System.out.println(dsMap);
    }
}
