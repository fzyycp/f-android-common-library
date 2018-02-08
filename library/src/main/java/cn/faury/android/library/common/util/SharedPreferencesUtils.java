package cn.faury.android.library.common.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences数据库管理
 */
public class SharedPreferencesUtils {

    /**
     * 保存数据
     *
     * @param ctx    上下文
     * @param dbName 数据库名
     * @param key    键
     * @param value  值
     * @return true：保存成功 false：失败
     */
    public static boolean save(Context ctx, String dbName, String key, int value) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(dbName, Context.MODE_PRIVATE);
        return sharedPreferences.edit().putInt(key, value).commit();
    }

    /**
     * 保存数据
     *
     * @param ctx    上下文
     * @param dbName 数据库名
     * @param key    键
     * @param value  值
     * @return true：保存成功 false：失败
     */
    public static boolean save(Context ctx, String dbName, String key, long value) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(dbName, Context.MODE_PRIVATE);
        return sharedPreferences.edit().putLong(key, value).commit();

    }

    /**
     * 保存数据
     *
     * @param ctx    上下文
     * @param dbName 数据库名
     * @param key    键
     * @param value  值
     * @return true：保存成功 false：失败
     */
    public static boolean save(Context ctx, String dbName, String key, String value) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(dbName, Context.MODE_PRIVATE);
        return sharedPreferences.edit().putString(key, value).commit();

    }

    /**
     * 保存数据
     *
     * @param ctx    上下文
     * @param dbName 数据库名
     * @param key    键
     * @param value  值
     * @return true：保存成功 false：失败
     */
    public static boolean save(Context ctx, String dbName, String key, boolean value) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(dbName, Context.MODE_PRIVATE);
        return sharedPreferences.edit().putBoolean(key, value).commit();
    }

    /**
     * 获取数据
     *
     * @param ctx      上下文
     * @param dbName   数据库名
     * @param key      键
     * @param defValue 默认值
     * @return 数据
     */
    public static int get(Context ctx, String dbName, String key, int defValue) {
        SharedPreferences preferences = ctx.getSharedPreferences(dbName, Context.MODE_PRIVATE);
        return preferences.getInt(key, defValue);
    }

    /**
     * 获取数据
     *
     * @param ctx      上下文
     * @param dbName   数据库名
     * @param key      键
     * @param defValue 默认值
     * @return 数据
     */
    public static long get(Context ctx, String dbName, String key, long defValue) {
        SharedPreferences preferences = ctx.getSharedPreferences(dbName, Context.MODE_PRIVATE);
        return preferences.getLong(key, defValue);
    }

    /**
     * 获取数据
     *
     * @param ctx      上下文
     * @param dbName   数据库名
     * @param key      键
     * @param defValue 默认值
     * @return 数据
     */
    public static String get(Context ctx, String dbName, String key, String defValue) {
        SharedPreferences preferences = ctx.getSharedPreferences(dbName, Context.MODE_PRIVATE);
        return preferences.getString(key, defValue);
    }

    /**
     * 获取数据
     *
     * @param ctx      上下文
     * @param dbName   数据库名
     * @param key      键
     * @param defValue 默认值
     * @return 数据
     */
    public static boolean get(Context ctx, String dbName, String key, boolean defValue) {
        SharedPreferences preferences = ctx.getSharedPreferences(dbName, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, defValue);
    }

    /**
     * 删除对应数据库
     *
     * @param ctx    上下文
     * @param dbName 数据库名
     * @return 是否清除成功
     */
    public static boolean clearDB(Context ctx, String dbName) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(dbName, Context.MODE_PRIVATE);
        return sharedPreferences.edit().clear().commit();
    }
}
