package com.fly.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * 
 * 测试Main
 * 
 * @author 00fly
 * @version [版本号, 2018-11-21]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class TestMain
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TestMain.class);
    
    /**
     * Main
     * 
     * @param args
     * @see [类、类#方法、类#成员]
     */
    public static void main(String[] args)
    {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext();
        context.setValidating(false);
        context.load("classpath*:applicationContext.xml");
        context.refresh();
        int i = 1;
        for (String beanName : context.getBeanDefinitionNames())
        {
            LOGGER.info("{}.\t{}", i, beanName);
            i++;
        }
        context.close();
    }
    
}
