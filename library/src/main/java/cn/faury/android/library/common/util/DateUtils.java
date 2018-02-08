package cn.faury.android.library.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 */

public class DateUtils {
    /**
     * 获取当前时间
     *
     * @return 当前时间对象
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 获取当前时间字符串
     *
     * @param pattern 格式化对象
     * @return 当前时间
     */
    public static String getCurrentDateString(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        return sdf.format(getCurrentDate());
    }

    /**
     * 获取当前时间字符串
     *
     * @return 当前时间
     */
    public static String getCurrentDateString() {
        return getCurrentDateString("yyyy-MM-dd HH:mm:ss");
    }

}
