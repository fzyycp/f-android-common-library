package cn.faury.android.library.common.helper;

import android.util.Log;

/**
 * 自定义日志辅助器
 */

public final class Logger {

    /**
     * 日志等级
     */
    public final static class LEVEL {

        /**
         * Priority constant for the println method; use Log.v.
         */
        public static final int VERBOSE = Log.VERBOSE;

        /**
         * Priority constant for the println method; use Log.d.
         */
        public static final int DEBUG = Log.DEBUG;

        /**
         * Priority constant for the println method; use Log.i.
         */
        public static final int INFO = Log.INFO;

        /**
         * Priority constant for the println method; use Log.w.
         */
        public static final int WARN = Log.WARN;

        /**
         * Priority constant for the println method; use Log.e.
         */
        public static final int ERROR = Log.ERROR;

        /**
         * Priority constant for the println method.
         */
        public static final int ASSERT = Log.ASSERT;
    }

    /**
     * 开发模式
     */
    private static int _level = LEVEL.WARN;

    /**
     * 日志信息前缀
     */
    public static String prefix = "【Logger-%s】%s";

    /**
     * 日志级别
     *
     * @param level 日志级别，参考{@link LEVEL}
     */
    public static void setLevel(int level) {
        switch (level) {
            case LEVEL.VERBOSE:
            case LEVEL.DEBUG:
            case LEVEL.INFO:
            case LEVEL.WARN:
            case LEVEL.ERROR:
            case LEVEL.ASSERT:
                _level = level;
                break;
            default:
                _level = LEVEL.DEBUG;
                break;
        }
    }

    /**
     * 输出verbose信息
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void v(String tag, String msg) {
        if (LEVEL.VERBOSE >= _level) {
            Log.v(tag, String.format(prefix, "VERBOSE", msg));
        }
    }

    /**
     * 输出输出verbose信息信息
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void v(String tag, String msg, Throwable tr) {
        if (LEVEL.VERBOSE >= _level) {
            Log.v(tag, String.format(prefix, "VERBOSE", msg), tr);
        }
    }

    /**
     * 输出debug信息
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void d(String tag, String msg) {
        if (LEVEL.DEBUG >= _level) {
            Log.d(tag, String.format(prefix, "DEBUG", msg));
        }
    }

    /**
     * 输出debug信息
     *
     * @param tag 标签
     * @param msg 消息
     * @param tr  异常信息
     */
    public static void d(String tag, String msg, Throwable tr) {
        if (LEVEL.DEBUG >= _level) {
            Log.d(tag, String.format(prefix, "DEBUG", msg), tr);
        }
    }

    /**
     * 输出info信息
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void i(String tag, String msg) {
        if (LEVEL.INFO >= _level) {
            Log.i(tag, String.format(prefix, "INFO", msg));
        }
    }

    /**
     * 输出info信息
     *
     * @param tag 标签
     * @param msg 消息
     * @param tr  异常信息
     */
    public static void i(String tag, String msg, Throwable tr) {
        if (LEVEL.INFO >= _level) {
            Log.i(tag, String.format(prefix, "INFO", msg), tr);
        }
    }

    /**
     * 输出warn信息
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void w(String tag, String msg) {
        if (LEVEL.WARN >= _level) {
            Log.w(tag, String.format(prefix, "WARN", msg));
        }
    }

    /**
     * 输出Log.ERROR &ge; _level信息
     *
     * @param tag 标签
     * @param msg 消息
     * @param tr  异常信息
     */
    public static void w(String tag, String msg, Throwable tr) {
        if (LEVEL.WARN >= _level) {
            Log.w(tag, String.format(prefix, "WARN", msg), tr);
        }
    }

    /**
     * 输出error信息
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void e(String tag, String msg) {
        if (LEVEL.ERROR >= _level) {
            Log.e(tag, String.format(prefix, "ERROR", msg));
        }
    }

    /**
     * 输出error信息
     *
     * @param tag 标签
     * @param msg 消息
     * @param tr  异常信息
     */
    public static void e(String tag, String msg, Throwable tr) {
        if (LEVEL.ERROR >= _level) {
            Log.e(tag, String.format(prefix, "ERROR", msg), tr);
        }
    }
}
