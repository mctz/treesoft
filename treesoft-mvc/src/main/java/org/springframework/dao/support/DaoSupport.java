package org.springframework.dao.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/support/DaoSupport.class */
public abstract class DaoSupport implements InitializingBean {
    protected final Log logger = LogFactory.getLog(getClass());

    protected abstract void checkDaoConfig() throws IllegalArgumentException;

    public final void afterPropertiesSet() throws IllegalArgumentException, BeanInitializationException {
        checkDaoConfig();
        try {
            initDao();
        } catch (Exception ex) {
            throw new BeanInitializationException("Initialization of DAO failed", ex);
        }
    }

    protected void initDao() throws Exception {
    }
}
