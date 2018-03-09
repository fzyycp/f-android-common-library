package cn.faury.android.library.common.helper.sqlite;

import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库管理回调
 */

public interface DatabaseCallback {
    /**
     * 创建数据库时执行
     *
     * @param db SQLiteDatabase
     */
    void onCreate(SQLiteDatabase db);

    /**
     * 更新数据库时执行
     *
     * @param db         SQLiteDatabase
     * @param oldVersion 旧版本
     * @param newVersion 新版本
     */
    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

}
