package cn.faury.android.library.common.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.faury.android.library.common.core.PermissionException;
import cn.faury.android.library.common.helper.Logger;
import cn.faury.android.library.common.sqlite.dao.BaseDatabaseDao;
import cn.faury.android.library.common.sqlite.dao.BaseTableDao;
import cn.faury.android.library.common.util.CollectionsUtils;
import cn.faury.android.library.common.util.FileUtils;
import cn.faury.android.library.common.util.PackageManagerUtil;
import cn.faury.android.library.common.util.StorageUtils;


/**
 * 数据库服务器
 */

public class DatabaseManager {
    /**
     * 日志标签
     */
    private final String TAG = DatabaseManager.class.getName();

    /**
     * 数据库对象缓存
     */
    private final Map<String, SQLiteDatabase> databases = new LinkedHashMap<>();

    /**
     * 管理数据库表
     */
    private InnerDatabaseManagerDao dbDao;

    /**
     * 数据库对象
     */
    private Map<String, BaseDatabaseDao> databaseDaoMap;

    /**
     * 构造函数
     */
    private DatabaseManager(String dir) {
        this.dbDao = new InnerDatabaseManagerDao(dir);
        this.dbDao.bindManager(this);

        this.databaseDaoMap = new HashMap<>();
        this.databaseDaoMap.put(this.dbDao.getDbName(), this.dbDao);
    }

    /**
     * 打开数据库
     *
     * @param dbName 数据库名
     * @return 数据库对象
     */
    public SQLiteDatabase openOrCreateDatabase(String dbName) {
        SQLiteDatabase db = databases.get(dbName);
        if (db == null || !db.isOpen()) {// 数据库对象不存在或者未打开
            BaseDatabaseDao dbDao = getDatabaseDao(dbName);
            if (dbDao == null) {
                throw new IllegalArgumentException("database " + dbName + " do not exist");
            }
            if (!FileUtils.isFileExist(dbDao.getDir() + File.separator + dbDao.getDbName())) {
                try {
                    FileUtils.createFile(dbDao.getDir() + File.separator + dbDao.getDbName(), FileUtils.Mode.ABSOLUTE_PATH_AND_NOTHING);
                } catch (IOException e) {
                    Logger.w(TAG, "file create exception", e);
                    e.printStackTrace();
                }
            }
            if (!FileUtils.isFileExist(dbDao.getDir() + File.separator + dbDao.getDbName())) {
                throw new PermissionException(PackageManagerUtil.PERMISSION_WRITE_EXTERNAL_STORAGE);
            }
            db = SQLiteDatabase.openOrCreateDatabase(new File(dbDao.getDir(), dbDao.getDbName()), null);
            databases.put(dbName, db);
        }
        return db;
    }

    /**
     * 获取数据库操作对象
     *
     * @param dbName 数据库名
     * @return 操作对象
     */
    public <T extends BaseDatabaseDao> T getDatabaseDao(String dbName) {
        return (T) this.databaseDaoMap.get(dbName);
    }

    /**
     * 初始化器
     */
    public static class Builder {

        /**
         * 上下文
         */
        private String managerDir;

        private List<BaseDatabaseDao> dbList = new ArrayList<>();

        /**
         * 构造函数
         *
         * @param context
         */
        public Builder(Context context) {
            this.managerDir = StorageUtils.getFauryPackageDir(context);
        }

        /**
         * 配置管理数据库目录
         *
         * @param dir 管理数据库目录
         * @return 当前对象
         */
        public Builder configDir(String dir) {
            if (FileUtils.isFilePath(dir)) {
                this.managerDir = dir;
            }
            return this;
        }

        /**
         * 配置数据库
         *
         * @param databaseDao 数据库回调
         * @return 当前对象
         */
        public Builder configDatabase(BaseDatabaseDao databaseDao) {
            if (databaseDao != null) {
                this.dbList.add(databaseDao);
            }
            return this;
        }

        /**
         * 配置数据库
         *
         * @param databaseDao 多个数据库回调
         * @return 当前对象
         */
        public Builder configDatabase(Collection<BaseDatabaseDao> databaseDao) {
            if (!CollectionsUtils.isEmpty(databaseDao)) {
                this.dbList.addAll(databaseDao);
            }
            return this;
        }

        // 初始化或更新管理数据库文件
        private synchronized void createOrUpgradeManagerDatabaseFile(DatabaseManager instance) {
            InnerDatabaseManagerDao dbDao = instance.dbDao;
            InnerManagerTableDao tbDao = instance.dbDao.getTableDao(InnerManagerTableDao.TABLE_NAME);

            SQLiteDatabase db = instance.openOrCreateDatabase(dbDao.getDbName());
            // 判断数据库信息表是否存在
            Cursor cursor = db.rawQuery("SELECT COUNT(*) as count FROM sqlite_master where type='table' and name='" + InnerManagerTableDao.TABLE_NAME + "'", null);
            if (cursor != null && cursor.moveToFirst() && cursor.getInt(0) > 0) {// 表存在
                InnerManagerTableRecordBean recordBean = tbDao.queryByName(dbDao.getDbName());
                if (recordBean == null) {
                    dbDao.onCreate(db);
                    tbDao.insert(dbDao);
                } else if (recordBean.getDbVersion() < dbDao.getDbVersion()) {
                    dbDao.onUpgrade(db, recordBean.getDbVersion(), dbDao.getDbVersion());
                    tbDao.updateDbVersionByName(dbDao.getDbName(), dbDao.getDbVersion());
                }
            } else {
                dbDao.onCreate(db);
                tbDao.insert(dbDao);
            }
        }

        /**
         * 构建对象
         */
        public DatabaseManager build() {
            // 初始化实例对象
            DatabaseManager instance = new DatabaseManager(this.managerDir);

            // 注册管理数据库及表
            InnerManagerTableDao tbDao = instance.dbDao.getTableDao(InnerManagerTableDao.TABLE_NAME);
            if (tbDao == null) {// 服务器管理表创建失败
                throw new NullPointerException("database create exception,pelease confirm the permission");
            }
            createOrUpgradeManagerDatabaseFile(instance);

            if (dbList.size() > 0) {
                for (BaseDatabaseDao dbInfo : dbList) {
                    if (dbInfo == null) {
                        continue;
                    }
                    if (InnerDatabaseManagerDao.DB_NAME.equals(dbInfo.getDbName())) {
                        throw new IllegalArgumentException("illegal argument db name(" + InnerDatabaseManagerDao.DB_NAME + ")");
                    }
                    dbInfo.bindManager(instance);

                    instance.databaseDaoMap.put(dbInfo.getDbName(), dbInfo);
                    // 查询服务器中数据库是否注册
                    InnerManagerTableRecordBean recordBean = tbDao.queryByName(dbInfo.getDbName());
                    if (recordBean == null) {// 数据库不存在
                        tbDao.insert(dbInfo);
                        dbInfo.onCreate(instance.openOrCreateDatabase(dbInfo.getDbName()));
                    } else if (recordBean.getDbVersion() < dbInfo.getDbVersion()) {// 更新版本
                        tbDao.updateDbVersionByName(dbInfo.getDbName(), dbInfo.getDbVersion());
                        dbInfo.onUpgrade(instance.openOrCreateDatabase(dbInfo.getDbName()), recordBean.getDbVersion(), dbInfo.getDbVersion());
                    }
                }
            }
            return instance;
        }
    }

    /**
     * 内部数据库服务器使用的数据库
     */
    private class InnerDatabaseManagerDao extends BaseDatabaseDao {

        /**
         * 管理数据库的库名
         */
        private static final String DB_NAME = "faury_database_manager.db";

        /**
         * 管理库版本号
         */
        private static final int DB_VERSION = 1;

        /**
         * 构造函数
         */
        public InnerDatabaseManagerDao(String dir) {
            super(DB_NAME, DB_VERSION, dir);
        }

        /**
         * 子类注册表信息
         *
         * @param tablesList 表集合
         */
        @Override
        protected void onConfigTablesList(List<BaseTableDao> tablesList) {
            tablesList.add(new InnerManagerTableDao());
        }

        /**
         * 创建数据库时执行
         *
         * @param db SQLiteDatabase
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            super.onCreate(db);
            Logger.v(TAG, "[database=" + DB_NAME + " version=" + DB_VERSION + "] create success");
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
            super.onUpgrade(db, oldVersion, newVersion);
            Logger.v(TAG, "[database=" + DB_NAME + " version=" + DB_VERSION + "] upgrade success");
        }
    }

    /**
     * 内部数据库服务器使用的数据库表
     */
    private class InnerManagerTableDao extends BaseTableDao {
        /**
         * 表名
         */
        public static final String TABLE_NAME = "tb_database_info";

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

        public InnerManagerTableDao() {
            super();
        }

        /**
         * 根据数据库名查询是否存在记录
         *
         * @param dbName 数据库名
         * @return 数据库记录
         */
        public InnerManagerTableRecordBean queryByName(String dbName) {
            Cursor cursor = this.query(null, TB_COL_NAME + "=?", new String[]{dbName}, null);
            if (cursor != null && cursor.moveToFirst()) {
                return new InnerManagerTableRecordBean(cursor);
            }
            return null;
        }

        /**
         * 更新版本号
         *
         * @param dbName    数据库名
         * @param dbVersion 新的版本号
         * @return 更新结果
         */
        public long updateDbVersionByName(String dbName, int dbVersion) {
            ContentValues values = new ContentValues();
            values.put(TB_COL_VERSION, dbVersion);
            return this.update(values, InnerManagerTableDao.TB_COL_NAME + "=?", new String[]{dbName});
        }

        /**
         * 插入一条记录
         *
         * @param dbInfo 数据库信息
         * @return 插入结果
         */
        public long insert(BaseDatabaseDao dbInfo) {
            return this.insert(dbInfo.getDbName(), dbInfo.getDbVersion(), dbInfo.getDir());
        }

        /**
         * 插入一条记录
         *
         * @param dbName    数据库名
         * @param dbVersion 数据库版本号
         * @param dir       数据库存储目录
         * @return 插入结果
         */
        public long insert(String dbName, int dbVersion, String dir) {
            ContentValues values = new ContentValues();
            values.put(TB_COL_NAME, dbName);
            values.put(TB_COL_VERSION, dbVersion);
            values.put(TB_COL_DIR, dir);
            return this.insert(values);
        }

        /**
         * 创建数据库时执行
         *
         * @param db SQLiteDatabase
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + getTableName() +
                    "(" + TB_COL_NAME + " TEXT NOT NULL PRIMARY KEY ON CONFLICT REPLACE" +
                    ", " + TB_COL_VERSION + " INTEGER DEFAULT 1" +
                    ", " + TB_COL_DIR + " TEXT)");
            Logger.v(TAG, "table " + getTableName() + " create success");
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
            Logger.v(TAG, "table " + getTableName() + " upgrade success");
        }

        /**
         * get table name
         *
         * @return table name
         */
        @Override
        public String getTableName() {
            return TABLE_NAME;
        }
    }

    /**
     * 内部数据库服务器使用的表记录
     */
    private static class InnerManagerTableRecordBean {

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

        public InnerManagerTableRecordBean(Cursor cursor) {
            if (cursor != null && !cursor.isClosed()) {
                // dbName
                int columnIndex = cursor.getColumnIndex(InnerManagerTableDao.TB_COL_NAME);
                if (columnIndex != -1) {
                    dbName = cursor.getString(columnIndex);
                } else {
                    throw new NullPointerException("cursor illegal!");
                }
                // dbVersion
                columnIndex = cursor.getColumnIndex(InnerManagerTableDao.TB_COL_VERSION);
                if (columnIndex != -1) {
                    dbVersion = cursor.getInt(columnIndex);
                }
                // dir
                columnIndex = cursor.getColumnIndex(InnerManagerTableDao.TB_COL_DIR);
                if (columnIndex != -1) {
                    dir = cursor.getString(columnIndex);
                }
            } else {
                throw new NullPointerException("cursor illegal!");
            }
        }

        public String getDbName() {
            return dbName;
        }

        public int getDbVersion() {
            return dbVersion;
        }

        public String getDir() {
            return dir;
        }
    }
}
