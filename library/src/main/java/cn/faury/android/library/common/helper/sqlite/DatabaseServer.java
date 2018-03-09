package cn.faury.android.library.common.helper.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.faury.android.library.common.helper.sqlite.bean.AbstractDatabaseBean;
import cn.faury.android.library.common.helper.sqlite.dao.AbstractTableDao;

/**
 * 数据库服务器
 */

public class DatabaseServer extends SQLiteOpenHelper {
    /**
     * 数据库对象缓存
     */
    private final Map<String, SQLiteDatabase> databases = new LinkedHashMap<>();

    /**
     * 单一实例
     */
    private static DatabaseServer instance;

    /**
     * 上下文
     */
    private Context context;

    /**
     * 管理数据库表：生命周期托管在DatabaseServer上
     */
    private InnerDatabaseManagerBean innerDatabaseManagerBean;

    /**
     * 数据库对象
     */
    private Map<String, AbstractDatabaseBean> databaseDaoMap;

    /**
     * 构造函数
     */
    private DatabaseServer(Context context) {
        super(context, InnerDatabaseManagerBean.DB_NAME, null, InnerDatabaseManagerBean.DB_VERSION);
        this.context = context;
        this.innerDatabaseManagerBean = new InnerDatabaseManagerBean();
        this.databaseDaoMap = new HashMap<>();
    }

    /**
     * 注册数据库管理器
     *
     * @param context 应用上下文
     */
    public static DatabaseServer register(Context context, OnInitDatabaseListener listener) {
        if (instance == null) {
            synchronized (DatabaseServer.class) {
                if (instance == null) {
                    instance = new DatabaseServer(context.getApplicationContext());
                }
            }
        }
        if (listener != null) {
            List<AbstractDatabaseBean> dbList = listener.getInitDatabases();
            if (dbList != null && dbList.size() > 0) {
                // 查询服务器中数据库是否注册
                InnerManagerTableDao managerTable = instance.innerDatabaseManagerBean.getTableInfo(InnerManagerTableDao.TABLE_NAME);
                if (managerTable == null) {// 服务器管理表创建失败
                    throw new NullPointerException("database create exception,pelease confirm the permission");
                }
                for (AbstractDatabaseBean dbInfo : dbList) {
                    Cursor cursor = managerTable.query(null, InnerManagerTableDao.TB_COL_NAME + "=?", new String[]{dbInfo.getDbName()}, null);
                    if (cursor == null) {// 数据库不存在
                        instance.databaseDaoMap.put(dbInfo.getDbName(), dbInfo);
                        ContentValues values = new ContentValues();
                        values.put(InnerManagerTableDao.TB_COL_NAME, dbInfo.getDbName());
                        values.put(InnerManagerTableDao.TB_COL_VERSION, dbInfo.getDbVersion());
                        values.put(InnerManagerTableDao.TB_COL_DIR, dbInfo.getDir());
                        managerTable.insert(values);
                        dbInfo.onCreate(instance.getDatabase(dbInfo.getDbName()));
                    } else {// 数据库存在
                        InnerManagerTableRecordBean bean = new InnerManagerTableRecordBean(cursor);
                        if (bean.getDbVersion() < dbInfo.getDbVersion()) {// 更新版本
                            ContentValues values = new ContentValues();
                            values.put(InnerManagerTableDao.TB_COL_VERSION, dbInfo.getDbVersion());
                            managerTable.update(values, InnerManagerTableDao.TB_COL_NAME + "=?", new String[]{dbInfo.getDbName()});
                            dbInfo.onUpgrade(instance.getDatabase(dbInfo.getDbName()), bean.getDbVersion(), dbInfo.getDbVersion());
                        }
                    }
                }
            }
        }
        return instance;
    }

    /**
     * 获取注册的数据库管理器，必须先注册
     *
     * @return 数据库服务器
     */
    public static DatabaseServer getInstance() {
        if (instance == null) {
            throw new NullPointerException("database manager not register");
        }
        return instance;
    }

    /**
     * 创建表
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.innerDatabaseManagerBean.onCreate(db);
    }

    /**
     * 更新表
     *
     * @param db         SQLiteDatabase
     * @param oldVersion 旧版本
     * @param newVersion 新版本
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.innerDatabaseManagerBean.onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * 打开数据库
     *
     * @param dbName 数据库名
     * @return 数据库对象
     */
    public SQLiteDatabase getDatabase(String dbName) {
        SQLiteDatabase db = databases.get(dbName);
        if (db == null || !db.isOpen()) {// 数据库对象不存在或者未打开
            AbstractDatabaseBean databaseInfo = getDatabaseBean(dbName);
            if (databaseInfo == null) {
                throw new IllegalArgumentException("database " + dbName + " do not exist");
            }
            db = SQLiteDatabase.openOrCreateDatabase(new File(databaseInfo.getDir(), databaseInfo.getDbName()), null);
            databases.put(dbName, db);
        }
        return db;
    }

    public AbstractDatabaseBean getDatabaseBean(String dbName) {
        return instance.databaseDaoMap.get(dbName);
    }

    /**
     * 初始化数据库管理器中的所有数据库
     */
    public interface OnInitDatabaseListener {
        List<AbstractDatabaseBean> getInitDatabases();
    }

    /**
     * 内部数据库服务器使用的数据库
     */
    private class InnerDatabaseManagerBean extends AbstractDatabaseBean {

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
        public InnerDatabaseManagerBean() {
            super(DB_NAME, DB_VERSION, true, "");
        }

        /**
         * 子类注册表信息
         *
         * @param tablesList 表集合
         */
        @Override
        protected void onConfigTablesList(List<AbstractTableDao> tablesList) {
            tablesList.add(new InnerManagerTableDao(this));
        }
    }

    /**
     * 内部数据库服务器使用的数据库表
     */
    private class InnerManagerTableDao extends AbstractTableDao {
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

        public InnerManagerTableDao(AbstractDatabaseBean databaseInfo) {
            super(databaseInfo, TABLE_NAME);
        }

        /**
         * 创建数据库时执行
         *
         * @param db SQLiteDatabase
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + AbstractDatabaseBean.TB_NAME +
                    "(" + AbstractDatabaseBean.TB_COL_NAME + " TEXT NOT NULL PRIMARY KEY ON CONFLICT REPLACE" +
                    ", " + AbstractDatabaseBean.TB_COL_VERSION + " INTEGER DEFAULT 1" +
                    ", " + AbstractDatabaseBean.TB_COL_DIR + " TEXT)");
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
