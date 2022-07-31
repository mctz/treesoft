package org.springframework.base.system.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.base.system.utils.ConvertUtils;
import org.springframework.base.system.utils.ServletUtils;
import org.springframework.base.system.utils.StringUtil;
import org.springframework.util.Assert;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/persistence/PropertyFilter.class */
public class PropertyFilter {
    public static final String OR_SEPARATOR = "_OR_";
    private MatchType matchType;
    private Object matchValue;
    private Class<?> propertyClass;
    private String[] propertyNames;

    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/persistence/PropertyFilter$MatchType.class */
    public enum MatchType {
        EQ,
        LIKE,
        LT,
        GT,
        LE,
        GE
    }

    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/persistence/PropertyFilter$PropertyType.class */
    public enum PropertyType {
        S(String.class),
        I(Integer.class),
        L(Long.class),
        N(Double.class),
        D(Date.class),
        B(Boolean.class);
        
        private Class<?> clazz;

        PropertyType(Class cls) {
            this.clazz = cls;
        }

        public Class<?> getValue() {
            return this.clazz;
        }
    }

    public PropertyFilter() {
        this.matchType = null;
        this.matchValue = null;
        this.propertyClass = null;
        this.propertyNames = null;
    }

    public PropertyFilter(String filterName, String value) {
        this.matchType = null;
        this.matchValue = null;
        this.propertyClass = null;
        this.propertyNames = null;
        String firstPart = StringUtil.substringBefore(filterName, "_");
        String matchTypeCode = StringUtil.substring(firstPart, 0, firstPart.length() - 1);
        String propertyTypeCode = StringUtil.substring(firstPart, firstPart.length() - 1, firstPart.length());
        try {
            this.matchType = (MatchType) Enum.valueOf(MatchType.class, matchTypeCode);
            try {
                this.propertyClass = ((PropertyType) Enum.valueOf(PropertyType.class, propertyTypeCode)).getValue();
                String propertyNameStr = StringUtil.substringAfter(filterName, "_");
                Assert.isTrue(StringUtil.isNotBlank(propertyNameStr), "filter名称" + filterName + "没有按规则编写,无法得到属性名称.");
                this.propertyNames = StringUtil.splitByWholeSeparator(propertyNameStr, OR_SEPARATOR);
                this.matchValue = ConvertUtils.convertStringToObject(value, this.propertyClass);
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性值类型.", e);
            }
        } catch (RuntimeException e2) {
            throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性比较类型.", e2);
        }
    }

    public static List<PropertyFilter> buildFromHttpRequest(HttpServletRequest request) {
        return buildFromHttpRequest(request, "filter");
    }

    public static List<PropertyFilter> buildFromHttpRequest(HttpServletRequest request, String filterPrefix) {
        List<PropertyFilter> filterList = new ArrayList<>();
        Map<String, Object> filterParamMap = ServletUtils.getParametersStartingWith(request, String.valueOf(filterPrefix) + "_");
        Iterator<Map.Entry<String, Object>> it = filterParamMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String filterName = entry.getKey();
            String value = (String) entry.getValue();
            if (StringUtil.isNotBlank(value)) {
                PropertyFilter filter = new PropertyFilter(filterName, value);
                filterList.add(filter);
            }
        }
        return filterList;
    }

    public Class<?> getPropertyClass() {
        return this.propertyClass;
    }

    public MatchType getMatchType() {
        return this.matchType;
    }

    public Object getMatchValue() {
        return this.matchValue;
    }

    public String[] getPropertyNames() {
        return this.propertyNames;
    }

    public String getPropertyName() {
        Assert.isTrue(this.propertyNames.length == 1, "There are not only one property in this filter.");
        return this.propertyNames[0];
    }

    public boolean hasMultiProperties() {
        return this.propertyNames.length > 1;
    }
}
