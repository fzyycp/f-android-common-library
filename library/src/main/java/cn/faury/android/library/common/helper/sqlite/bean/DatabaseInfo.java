package cn.faury.android.library.common.helper.sqlite.bean;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

import cn.faury.android.library.common.helper.sqlite.DatabaseCallback;
import cn.faury.android.library.common.util.StringUtils;

/**
 * 数据库信息
 */

public class DatabaseInfo implements DatabaseCallback {

    /**
     * 管理数据库表名
     */
    public static final String TB_NAME = "tb_database_info";
    /**
     * 字段名:数据库文件名
     */
    public static final String TB_COL_NAME = "DB_NAME";
    /**
     * 字段名：数据库版本号
     */
    public static final String TB_COL_VERSION = "DB_VERSION";
    /**
     * 字段名：数据库存储目录
     */
    public static final String TB_COL_DIR = "DB_DIR";

    private String name;
    private int version = 1;
    private String dir;

    /**
     * 构造函数
     * @param cursor 数据库记录
     */
    public DatabaseInfo(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            // name
            int columnIndex = cursor.getColumnIndex(TB_COL_NAME);
            if (columnIndex != -1) {
                name = cursor.getString(columnIndex);
            } else {
                throw new NullPointerException("cursor illegal!");
            }
            // version
            columnIndex = cursor.getColumnIndex(TB_COL_VERSION);
            if (columnIndex != -1) {
                version = cursor.getInt(columnIndex);
            }
            // dir
            columnIndex = cursor.getColumnIndex(TB_COL_DIR);
            if (columnIndex != -1) {
                dir = cursor.getString(columnIndex);
            }
        } else {
            throw new NullPointerException("cursor illegal!");
        }
    }

    /**
     * 构造函数
     *
     * @param name    数据库名
     * @param version 版本号
     * @param dir     数据库目录
     */
    public DatabaseInfo(String name, int version, String dir) {
        this.name = name;
        this.version = version;
        this.dir = dir;
    }

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }

    public String getDir() {
        return dir;
    }

    /**
     * 创建数据库时执行
     *
     * @param db SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    /**
     * 更新数据库时执行
     *
     * @param db         SQLiteDatabase
     * @param oldVersion 旧版本
     * @param newVersion 新版本
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
