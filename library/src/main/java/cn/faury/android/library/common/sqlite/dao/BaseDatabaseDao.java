package cn.faury.android.library.common.sqlite.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.faury.android.library.common.helper.Logger;
import cn.faury.android.library.common.sqlite.DatabaseManager;
import cn.faury.android.library.common.sqlite.bean.DatabaseInfo;
import cn.faury.android.library.common.util.CollectionsUtils;
import cn.faury.android.library.common.util.StringUtils;


/**
 * 数据库表信息
 */

public abstract class BaseDatabaseDao implements IDatabaseCallback {

    /**
     * 标签
     */
    protected static final String TAG = BaseDatabaseDao.class.getName();

    private DatabaseInfo databaseInfo;

    private DatabaseManager manager;

    /**
     * 表集合
     */
    private Map<String, BaseTableDao> tablesMap = new HashMap<>();

    /**
     * 构造函数
     *
     * @param bean    数据库信息
     */
    public BaseDatabaseDao(DatabaseInfo bean) {
        this.databaseInfo = bean;
        initTablesMap();
    }

    /**
     * 构造函数
     *
     * @param dbName    数据库名
     * @param dbVersion 版本号
     * @param dir       数据库目录
     */
    public BaseDatabaseDao(String dbName, int dbVersion, String dir) {
        this(new DatabaseInfo(dbName, dbVersion, dir));
    }

    /**
     * 绑定数据库管理器
     * @param manager 数据库管理器
     */
    public void bindManager(DatabaseManager manager) {
        this.manager = manager;
    }

    private void initTablesMap() {

        List<BaseTableDao> tablesList = new ArrayList<>();

        // 子类注册表信息
        onConfigTablesList(tablesList);

        if (CollectionsUtils.isEmpty(tablesList)) {
            return;
        }

        for (BaseTableDao tableInfo : tablesList) {
            if (tableInfo == null) {
                continue;
            }
            tableInfo.bindDatabaseDao(this);
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
    protected abstract void onConfigTablesList(List<BaseTableDao> tablesList);

    /**
     * 获取数据库中的表对象
     *
     * @param tableName 表名
     * @return 表对象
     */
    public <T extends BaseTableDao> T getTableDao(String tableName) {
        return (T) tablesMap.get(tableName);
    }

    /**
     * 获取所在数据库管理器
     *
     * @return 数据库管理器
     */
    public DatabaseManager getManager() {
        return this.manager;
    }

    public String getDbName() {
        return databaseInfo.getDbName();
    }

    public int getDbVersion() {
        return databaseInfo.getDbVersion();
    }

    public String getDir() {
        return databaseInfo.getDir();
    }

    /**
     * 创建数据库时执行
     *
     * @param db SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Logger.v(TAG, "[database=" + this.getDbName() + " version=" + getDbVersion() + "] create success");
        Collection<BaseTableDao> tablesList = tablesMap.values();
        if (!CollectionsUtils.isEmpty(tablesList)) {
            for (BaseTableDao tableInfo : tablesList) {
                if (tableInfo == null) {
                    continue;
                }
                try {
                    tableInfo.onCreate(db);
                    Logger.v(TAG, "[database=" + this.getDbName() + " table=" + tableInfo.getTableName() + "] create success");
                } catch (Exception e) {
                    Logger.e(TAG, "onCreate SQL Exception:" + e.getMessage(), e);
                }
            }
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
        Logger.v(TAG, "[database=" + getDbName() + " version=" + getDbVersion() + "] upgrade success");
        Collection<BaseTableDao> tablesList = tablesMap.values();
        if (!CollectionsUtils.isEmpty(tablesList)) {
            for (BaseTableDao tableInfo : tablesList) {
                if (tableInfo == null) {
                    continue;
                }
                tableInfo.onUpgrade(db, oldVersion, newVersion);
                Logger.v(TAG, "[database=" + this.getDbName() + " table=" + tableInfo.getTableName() + "] upgrade success");
            }
        }
    }
}
