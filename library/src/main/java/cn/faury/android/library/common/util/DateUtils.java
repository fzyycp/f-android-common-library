package cn.faury.android.library.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日期工具类
 */

public class DateUtils {

    private static final Map<String, ThreadLocal<SimpleDateFormat>> DATE_FORMAT_CACHE = new ConcurrentHashMap<>();

    /**
     * 获取日期格式化对象
     *
     * @param pattern 模式
     * @return 格式化对象
     */
    public static SimpleDateFormat getSimpleDateFormat(String pattern) {
        ThreadLocal<SimpleDateFormat> threadLocal = DATE_FORMAT_CACHE.get(pattern);
        if (threadLocal == null) {
            threadLocal = new ThreadLocal<>();
            DATE_FORMAT_CACHE.put(pattern, threadLocal);
        }
        SimpleDateFormat sdf = threadLocal.get();
        if (sdf == null) {
            sdf = new SimpleDateFormat(pattern, Locale.getDefault());
            threadLocal.set(sdf);
        }
        return sdf;
    }

    /**
     * 获取字符串形式日期
     *
     * @param date    日期
     * @param pattern 模式
     * @return 日期字符串
     */
    public static String getDateString(Date date, String pattern) {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间对象
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 获取通用日期模式
     *
     * @return 模式
     */
    public static String getDefaultDatePattern() {
        return "yyyy-MM-dd HH:mm:ss";
    }

    /**
     * 获取当前时间字符串
     *
     * @param pattern 格式化对象
     * @return 当前时间
     */
    public static String getCurrentDateString(String pattern) {
        return getDateString(getCurrentDate(), pattern);
    }

    /**
     * 获取当前时间字符串
     *
     * @return 当前时间
     */
    public static String getCurrentDateString() {
        return getCurrentDateString(getDefaultDatePattern());
    }

    /**
     * 日期字符串转换为日期格式
     *
     * @param dateStr 日期字符串
     * @param pattern 模式
     * @return 日期对象
     */
    public static Date string2Date(String dateStr, String pattern) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }

        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     * 日期字符串转换为日期格式
     *
     * @param dateStr 日期字符串
     * @return 日期对象
     */
    public static Date string2Date(String dateStr) {
        return string2Date(dateStr, getDefaultDatePattern());
    }
}
