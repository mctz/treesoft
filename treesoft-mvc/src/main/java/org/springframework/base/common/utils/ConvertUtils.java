package org.springframework.base.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

public class ConvertUtils
{
    private ConvertUtils()
    {
        super();
    }
    
    public static List<Object> convertElementPropertyToList(Collection<?> collection, String propertyName)
    {
        List<Object> list = new ArrayList<>();
        try
        {
            for (Object obj : collection)
            {
                list.add(PropertyUtils.getProperty(obj, propertyName));
            }
        }
        catch (Exception e)
        {
            throw Reflections.convertReflectionExceptionToUnchecked(e);
        }
        return list;
    }
    
    public static String convertElementPropertyToString(Collection<?> collection, String propertyName, String separator)
    {
        List<Object> list = convertElementPropertyToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }
}
