package cn.faury.android.library.common.helper.sqlite.bean;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.faury.android.library.common.helper.sqlite.dao.AbstractTableDao;
import cn.faury.android.library.common.helper.sqlite.dao.DatabaseCallback;
import cn.faury.android.library.common.util.CollectionsUtils;
import cn.faury.android.library.common.util.StringUtils;


/**
 * 数据库表信息
 */

public abstract class AbstractDatabaseBean implements DatabaseCallback {

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

    /**
     * 数据库名
     */
    private String dbName;

    /**
     * 数据库版本号
     */
    private int dbVersion = 1;

    /**
     * 数据库存储目录
     */
    private String dir;

    /**
     * 表集合
     */
    private Map<String, AbstractTableDao> tablesMap = new HashMap<>();

    /**
     * 构造函数
     *
     * @param dbName    数据库名
     * @param dbVersion 版本号
     * @param dir       数据库目录
     */
    public AbstractDatabaseBean(String dbName, int dbVersion, String dir) {
        this.dbName = dbName;
        this.dbVersion = dbVersion;
        this.dir = dir;
        initTablesMap();
    }

    private void initTablesMap() {

        List<AbstractTableDao> tablesList = new ArrayList<>();

        // 子类注册表信息
        onConfigTablesList(tablesList);

        if (CollectionsUtils.isEmpty(tablesList)) {
            return;
        }

        for (AbstractTableDao tableInfo : tablesList) {
            if (tableInfo == null) {
                continue;
            }
            String tableName = tableInfo.getTableName();

            if (StringUtils.isEmpty(tableName)) {
                continue;
            }

            if (tablesMap.containsKey(tableName)) {
                continue;
            }

            tablesMap.put(tableName, tableInfo);
        }
    }

    /**
     * 子类注册表信息
     *
     * @param tablesList 表集合
     */
    protected abstract void onConfigTablesList(List<AbstractTableDao> tablesList);

    public String getDbName() {
        return dbName;
    }

    public int getDbVersion() {
        return dbVersion;
    }

    public String getDir() {
        return dir;
    }

    /**
     * 获取数据库中的表对象
     *
     * @param tableName 表名
     * @return 表对象
     */
    public <T extends AbstractTableDao> T getTableInfo(String tableName) {
        return (T) tablesMap.get(tableName);
    }

    /**
     * 创建数据库时执行
     *
     * @param db SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Collection<AbstractTableDao> tablesList = tablesMap.values();
        if (CollectionsUtils.isEmpty(tablesList)) {
            return;
        }
        for (AbstractTableDao tableInfo : tablesList) {
            if (tableInfo == null) {
                continue;
            }
            tableInfo.onCreate(db);
        }
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
        Collection<AbstractTableDao> tablesList = tablesMap.values();
        if (CollectionsUtils.isEmpty(tablesList)) {
            return;
        }
        for (AbstractTableDao tableInfo : tablesList) {
            if (tableInfo == null) {
                continue;
            }
            tableInfo.onUpgrade(db, oldVersion, newVersion);
        }
    }
}
