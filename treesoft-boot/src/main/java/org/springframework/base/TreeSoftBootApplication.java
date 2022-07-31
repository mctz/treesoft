package org.springframework.base;

import java.io.IOException;

import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TreeSoftBootApplication implements CommandLineRunner
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TreeSoftBootApplication.class);
    
    @Value("${server.port}")
    Integer port;
    
    public static void main(String[] args)
    {
        SpringApplication.run(TreeSoftBootApplication.class, args);
    }
    
    @Override
    public void run(String... args)
        throws IOException
    {
        // DataBaseInit.initUseSQL("/sql/treesoft.sql");
        if (SystemUtils.IS_OS_WINDOWS)
        {
            LOGGER.info("★★★★★★★★  now open Browser ★★★★★★★★ ");
            Runtime.getRuntime().exec("cmd /c start /min http://127.0.0.1:" + port);
        }
    }
}
