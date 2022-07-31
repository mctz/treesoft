package org.springframework.transaction.interceptor;

import java.beans.PropertyEditorSupport;
import java.util.Enumeration;
import java.util.Properties;
import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.util.StringUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/TransactionAttributeSourceEditor.class */
public class TransactionAttributeSourceEditor extends PropertyEditorSupport {
    public void setAsText(String text) throws IllegalArgumentException {
        MethodMapTransactionAttributeSource source = new MethodMapTransactionAttributeSource();
        if (StringUtils.hasLength(text)) {
            PropertiesEditor propertiesEditor = new PropertiesEditor();
            propertiesEditor.setAsText(text);
            Properties props = (Properties) propertiesEditor.getValue();
            TransactionAttributeEditor tae = new TransactionAttributeEditor();
            Enumeration<?> propNames = props.propertyNames();
            while (propNames.hasMoreElements()) {
                String name = (String) propNames.nextElement();
                String value = props.getProperty(name);
                tae.setAsText(value);
                TransactionAttribute attr = (TransactionAttribute) tae.getValue();
                source.addTransactionalMethod(name, attr);
            }
        }
        setValue(source);
    }
}
