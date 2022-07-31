package org.springframework.base.system.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.time.DateFormatUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/DateUtils.class */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    private static String[] parsePatterns = {"yyyy-MM-dd", ExcelUtil.DATE_FORMAT, "yyyy-MM-dd HH:mm", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm"};

    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    public static String formatDate(Date date, Object... pattern) {
        String formatDate;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    public static String formatDateTime(Date date) {
        return formatDate(date, ExcelUtil.DATE_FORMAT);
    }

    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    public static String getDateTime() {
        return formatDate(new Date(), ExcelUtil.DATE_FORMAT);
    }

    public static String getDateTimeStringNotTime(Date date) {
        return formatDate(new Date(), "yyyyMMdd");
    }

    public static String getDateTimeString(Date date) {
        return formatDate(new Date(), "yyyyMMddHHmmss");
    }

    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / 86400000;
    }

    public static Date getDateStart(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        try {
            date = sdf.parse(String.valueOf(formatDate(date, "yyyy-MM-dd")) + " 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDateEnd(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        try {
            date = sdf.parse(String.valueOf(formatDate(date, "yyyy-MM-dd")) + " 23:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static boolean isDate(String timeString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        try {
            format.parse(timeString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String dateFormat(Date timestamp) {
        SimpleDateFormat format = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        return format.format(timestamp);
    }

    public static Timestamp getSysTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    public static Date getSysDate() {
        return new Date();
    }

    public static String getDateRandom() {
        String s = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        return s;
    }

    public static void main(String[] args) throws ParseException {
    }
}
