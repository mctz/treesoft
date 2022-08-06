package org.springframework.base.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class Reflections
{
    private static Logger logger = LoggerFactory.getLogger(Reflections.class);
    
    private Reflections()
    {
        super();
    }
    
    public static Object invokeGetter(Object obj, String propertyName)
    {
        Object object = obj;
        for (String name : StringUtils.split(propertyName, "."))
        {
            String getterMethodName = "get" + StringUtils.capitalize(name);
            object = invokeMethod(object, getterMethodName, new Class[0], new Object[0]);
        }
        return object;
    }
    
    public static void invokeSetter(Object obj, String propertyName, Object value)
    {
        Object object = obj;
        String[] names = StringUtils.split(propertyName, ".");
        for (int i = 0; i < names.length; i++)
        {
            if (i < names.length - 1)
            {
                String getterMethodName = "get" + StringUtils.capitalize(names[i]);
                object = invokeMethod(object, getterMethodName, new Class[0], new Object[0]);
            }
            else
            {
                String setterMethodName = "set" + StringUtils.capitalize(names[i]);
                invokeMethodByName(object, setterMethodName, new Object[] {value});
            }
        }
    }
    
    public static Object getFieldValue(Object obj, String fieldName)
    {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null)
        {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }
        Object result = null;
        try
        {
            result = field.get(obj);
        }
        catch (IllegalAccessException e)
        {
            logger.error("不可能抛出的异常{}", e.getMessage());
        }
        return result;
    }
    
    public static void setFieldValue(Object obj, String fieldName, Object value)
    {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null)
        {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }
        try
        {
            field.set(obj, value);
        }
        catch (IllegalAccessException e)
        {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }
    }
    
    public static Object invokeMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object[] args)
    {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null)
        {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }
        try
        {
            return method.invoke(obj, args);
        }
        catch (Exception e)
        {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }
    
    public static Object invokeMethodByName(Object obj, String methodName, Object[] args)
    {
        Method method = getAccessibleMethodByName(obj, methodName);
        if (method == null)
        {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }
        try
        {
            return method.invoke(obj, args);
        }
        catch (Exception e)
        {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }
    
    public static Field getAccessibleField(Object obj, String fieldName)
    {
        Validate.notNull(obj, "object can't be null");
        Validate.notBlank(fieldName, "fieldName can't be blank");
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass())
        {
            try
            {
                Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            }
            catch (NoSuchFieldException e)
            {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }
    
    public static Method getAccessibleMethod(Object obj, String methodName, Class<?>... parameterTypes)
    {
        Validate.notNull(obj, "object can't be null");
        Validate.notBlank(methodName, "methodName can't be blank");
        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass())
        {
            try
            {
                Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
                makeAccessible(method);
                return method;
            }
            catch (NoSuchMethodException e)
            {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }
    
    public static Method getAccessibleMethodByName(Object obj, String methodName)
    {
        Validate.notNull(obj, "object can't be null");
        Validate.notBlank(methodName, "methodName can't be blank");
        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass())
        {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods)
            {
                if (method.getName().equals(methodName))
                {
                    makeAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }
    
    public static void makeAccessible(Method method)
    {
        if (((!Modifier.isPublic(method.getModifiers())) || (!Modifier.isPublic(method.getDeclaringClass().getModifiers()))) && (!method.isAccessible()))
        {
            method.setAccessible(true);
        }
    }
    
    public static void makeAccessible(Field field)
    {
        if (((!Modifier.isPublic(field.getModifiers())) || (!Modifier.isPublic(field.getDeclaringClass().getModifiers())) || (Modifier.isFinal(field.getModifiers()))) && (!field.isAccessible()))
        {
            field.setAccessible(true);
        }
    }
    
    public static <T> Class<T> getClassGenricType(Class<?> clazz)
    {
        return getClassGenricType(clazz, 0);
    }
    
    public static Class getClassGenricType(Class<?> clazz, int index)
    {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType))
        {
            logger.warn("{}'s superclass not ParameterizedType", clazz.getSimpleName());
            return Object.class;
        }
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
        if ((index >= params.length) || (index < 0))
        {
            logger.warn("Index:{}, Size of {}'s Parameterized Type: {}", index, clazz.getSimpleName(), params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class))
        {
            logger.warn("{} not set the actual class on superclass generic parameter", clazz.getSimpleName());
            return Object.class;
        }
        return (Class<?>)params[index];
    }
    
    public static Class<?> getUserClass(Object instance)
    {
        Assert.notNull(instance, "Instance must not be null");
        Class<?> clazz = instance.getClass();
        if (clazz != null && clazz.getName().contains("$$"))
        {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && (!Object.class.equals(superClass)))
            {
                return superClass;
            }
        }
        return clazz;
    }
    
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e)
    {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException || e instanceof NoSuchMethodException)
        {
            return new IllegalArgumentException(e);
        }
        if (e instanceof InvocationTargetException)
        {
            return new RuntimeException(((InvocationTargetException)e).getTargetException());
        }
        if (e instanceof RuntimeException)
        {
            return (RuntimeException)e;
        }
        return new RuntimeException("Unexpected Checked Exception.", e);
    }
}
