package org.springframework.base.system.utils;

import java.io.File;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/InitialUtil.class */
public class InitialUtil implements ApplicationListener<ContextRefreshedEvent> {
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            try {
                File file = ResourceUtils.getFile("classpath:servlet-context.xml");
                Constants.DATABASEPATH = file.getAbsolutePath();
            } catch (Exception e) {
            }
        }
    }
}
