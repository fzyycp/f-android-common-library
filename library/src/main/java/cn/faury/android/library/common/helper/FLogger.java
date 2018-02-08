package cn.faury.android.library.common.helper;

import android.util.Log;

/**
 * 自定义日志辅助器
 */

public final class FLogger {
    /**
     * 开发模式
     */
    public static boolean DEV_MOD = false;

    /**
     * 输出debug信息
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void d(String tag, String msg) {
        if (DEV_MOD) {
            Log.d(tag, "【DEV_MOD】 " + msg);
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
        if (DEV_MOD) {
            Log.d(tag, "【DEV_MOD】 " + msg, tr);
        }
    }

    /**
     * 输出info信息
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void i(String tag, String msg) {
        if (DEV_MOD) {
            Log.i(tag, "【DEV_MOD】 " + msg);
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
        if (DEV_MOD) {
            Log.i(tag, "【DEV_MOD】 " + msg, tr);
        }
    }

    /**
     * 输出error信息
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void e(String tag, String msg) {
        if (DEV_MOD) {
            Log.e(tag, "【DEV_MOD】 " + msg);
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
        if (DEV_MOD) {
            Log.i(tag, "【DEV_MOD】 " + msg, tr);
        }
    }
}
