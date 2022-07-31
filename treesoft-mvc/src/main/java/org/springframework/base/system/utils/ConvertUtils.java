package org.springframework.base.system.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.StringUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/ConvertUtils.class */
public class ConvertUtils {
    static {
        registerDateConverter();
    }

    public static List convertElementPropertyToList(Collection collection, String propertyName) {
        List list = new ArrayList();
        try {
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                list.add(PropertyUtils.getProperty(obj, propertyName));
            }
            return list;
        } catch (Exception e) {
            throw Reflections.convertReflectionExceptionToUnchecked(e);
        }
    }

    public static String convertElementPropertyToString(Collection collection, String propertyName, String separator) {
        List list = convertElementPropertyToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }

    public static Object convertStringToObject(String value, Class<?> toType) {
        try {
            return org.apache.commons.beanutils.ConvertUtils.convert(value, toType);
        } catch (Exception e) {
            throw Reflections.convertReflectionExceptionToUnchecked(e);
        }
    }

    private static void registerDateConverter() {
        DateConverter dc = new DateConverter();
        dc.setUseLocaleFormat(true);
        dc.setPatterns(new String[]{"yyyy-MM-dd", ExcelUtil.DATE_FORMAT});
        org.apache.commons.beanutils.ConvertUtils.register(dc, Date.class);
    }
}
