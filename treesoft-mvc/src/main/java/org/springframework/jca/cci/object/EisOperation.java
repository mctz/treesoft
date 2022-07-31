package org.springframework.jca.cci.object;

import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.InteractionSpec;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jca.cci.core.CciTemplate;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/object/EisOperation.class */
public abstract class EisOperation implements InitializingBean {
    private CciTemplate cciTemplate = new CciTemplate();
    private InteractionSpec interactionSpec;

    public void setCciTemplate(CciTemplate cciTemplate) {
        if (cciTemplate == null) {
            throw new IllegalArgumentException("cciTemplate must not be null");
        }
        this.cciTemplate = cciTemplate;
    }

    public CciTemplate getCciTemplate() {
        return this.cciTemplate;
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.cciTemplate.setConnectionFactory(connectionFactory);
    }

    public void setInteractionSpec(InteractionSpec interactionSpec) {
        this.interactionSpec = interactionSpec;
    }

    public InteractionSpec getInteractionSpec() {
        return this.interactionSpec;
    }

    public void afterPropertiesSet() {
        this.cciTemplate.afterPropertiesSet();
        if (this.interactionSpec == null) {
            throw new IllegalArgumentException("interactionSpec is required");
        }
    }
}
