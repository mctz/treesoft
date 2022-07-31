package org.springframework.transaction.annotation;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;
import org.springframework.transaction.config.TransactionManagementConfigUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/annotation/TransactionManagementConfigurationSelector.class */
public class TransactionManagementConfigurationSelector extends AdviceModeImportSelector<EnableTransactionManagement> {

    /* renamed from: org.springframework.transaction.annotation.TransactionManagementConfigurationSelector$1 */
    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/annotation/TransactionManagementConfigurationSelector$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$org$springframework$context$annotation$AdviceMode = new int[AdviceMode.values().length];

        static {
            try {
                $SwitchMap$org$springframework$context$annotation$AdviceMode[AdviceMode.PROXY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$springframework$context$annotation$AdviceMode[AdviceMode.ASPECTJ.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    protected String[] selectImports(AdviceMode adviceMode) {
        switch (AnonymousClass1.$SwitchMap$org$springframework$context$annotation$AdviceMode[adviceMode.ordinal()]) {
            case 1:
                return new String[]{AutoProxyRegistrar.class.getName(), ProxyTransactionManagementConfiguration.class.getName()};
            case 2:
                return new String[]{TransactionManagementConfigUtils.TRANSACTION_ASPECT_CONFIGURATION_CLASS_NAME};
            default:
                return null;
        }
    }
}
