package org.springframework.transaction.annotation;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

@Configuration
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/annotation/AbstractTransactionManagementConfiguration.class */
public abstract class AbstractTransactionManagementConfiguration implements ImportAware {
    protected AnnotationAttributes enableTx;
    protected PlatformTransactionManager txManager;

    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableTx = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableTransactionManagement.class.getName(), false));
        Assert.notNull(this.enableTx, "@EnableTransactionManagement is not present on importing class " + importMetadata.getClassName());
    }

    @Autowired(required = false)
    void setConfigurers(Collection<TransactionManagementConfigurer> configurers) {
        if (CollectionUtils.isEmpty(configurers)) {
            return;
        }
        if (configurers.size() > 1) {
            throw new IllegalStateException("Only one TransactionManagementConfigurer may exist");
        }
        TransactionManagementConfigurer configurer = configurers.iterator().next();
        this.txManager = configurer.annotationDrivenTransactionManager();
    }
}
