package org.springframework.jca.cci.core.support;

import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.ConnectionSpec;
import org.springframework.dao.support.DaoSupport;
import org.springframework.jca.cci.CannotGetCciConnectionException;
import org.springframework.jca.cci.connection.ConnectionFactoryUtils;
import org.springframework.jca.cci.core.CciTemplate;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/core/support/CciDaoSupport.class */
public abstract class CciDaoSupport extends DaoSupport {
    private CciTemplate cciTemplate;

    public final void setConnectionFactory(ConnectionFactory connectionFactory) {
        if (this.cciTemplate == null || connectionFactory != this.cciTemplate.getConnectionFactory()) {
            this.cciTemplate = createCciTemplate(connectionFactory);
        }
    }

    protected CciTemplate createCciTemplate(ConnectionFactory connectionFactory) {
        return new CciTemplate(connectionFactory);
    }

    public final ConnectionFactory getConnectionFactory() {
        return this.cciTemplate.getConnectionFactory();
    }

    public final void setCciTemplate(CciTemplate cciTemplate) {
        this.cciTemplate = cciTemplate;
    }

    public final CciTemplate getCciTemplate() {
        return this.cciTemplate;
    }

    @Override // org.springframework.dao.support.DaoSupport
    protected final void checkDaoConfig() {
        if (this.cciTemplate == null) {
            throw new IllegalArgumentException("'connectionFactory' or 'cciTemplate' is required");
        }
    }

    protected final CciTemplate getCciTemplate(ConnectionSpec connectionSpec) {
        return getCciTemplate().getDerivedTemplate(connectionSpec);
    }

    protected final Connection getConnection() throws CannotGetCciConnectionException {
        return ConnectionFactoryUtils.getConnection(getConnectionFactory());
    }

    protected final void releaseConnection(Connection con) {
        ConnectionFactoryUtils.releaseConnection(con, getConnectionFactory());
    }
}
