package cn.faury.android.library.common.sqlite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Locale;

import cn.faury.android.library.common.helper.Logger;

/**
 * 数据库表信息
 */

public abstract class BaseTableDao implements ITableContentDao, IDatabaseCallback {
    /**
     * TAG
     */
    public static final String TAG = BaseTableDao.class.getName();

    /**
     * 默认数据库存储位置
     */
    public static final String DB_FILE_DIR_NAME = "database";
    public static final String FILE_NAME_CREATE = "create.sql";
    public static final String FILE_NAME_UPDATE_PATTEN = "update.%d.%d.sql";

    /**
     * 数据库信息
     */
    private BaseDatabaseDao dbDao;

    /**
     * 上下文
     */
    private Context context;

    /**
     * 构造函数
     */
    public BaseTableDao() {
    }

    /**
     * 构造函数
     */
    public BaseTableDao(Context context) {
        this.context = context;
    }

    /**
     * 绑定数据库
     *
     * @param dbDao 数据库操作器
     */
    public void bindDatabaseDao(BaseDatabaseDao dbDao) {
        this.dbDao = dbDao;
    }

    /**
     * 获取数据库操作群
     */
    public BaseDatabaseDao getDatabaseDao() {
        return this.dbDao;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.tryCreateFromFile(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.tryUpdateFromFile(db, oldVersion, newVersion);
    }

    /**
     * 插入数据
     *
     * @param values 数据.
     * @return -1表示失败，其他表示返回的id
     */
    @Override
    public long insert(ContentValues values) {
        long id = -1;
        try {
            id = getDatabase().insert(getTableName(), null, values);
        } catch (Exception e) {
            Logger.e(TAG, "insert exception:" + e.getMessage(), e);
        }
        return id;
    }

    /**
     * 删除数据
     *
     * @param selection     条件表达式
     * @param selectionArgs 条件表达式参数值
     * @return -1表示失败，其他表示影响行数
     */
    @Override
    public int delete(String selection, String[] selectionArgs) {
        int count = -1;
        try {
            count = getDatabase().delete(getTableName(), selection, selectionArgs);
        } catch (Exception e) {
            Logger.e(TAG, "delete exception:" + e.getMessage(), e);
        }
        return count;
    }

    /**
     * 更新数据
     *
     * @param values        数据.
     * @param selection     条件表达式
     * @param selectionArgs 条件表达式参数值
     * @return -1表示失败，其他表示影响行数
     */
    @Override
    public int update(ContentValues values, String selection, String[] selectionArgs) {
        int count = -1;
        try {
            count = getDatabase().update(getTableName(), values, selection, selectionArgs);
        } catch (Exception e) {
            Logger.e(TAG, "update exception:" + e.getMessage(), e);
        }
        return count;
    }

    /**
     * 查询
     *
     * @param projection    要查询的列，null表示所有
     * @param selection     条件表达式
     * @param selectionArgs 条件表达式参数值
     * @param sortOrder     排序字段（ORDER BY clause）
     * @return Cursor或者null.
     */
    @Override
    public Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        try {
            cursor = getDatabase().query(true, getTableName(), projection, selection, selectionArgs, null, null, sortOrder, null);
        } catch (Exception e) {
            Logger.e(TAG, "query exception:" + e.getMessage(), e);
        }
        return cursor;
    }

    /**
     * 获取数据库操作对象
     *
     * @return 数据库操作对象
     */
    @Override
    public SQLiteDatabase getDatabase() {
        if (this.dbDao != null && this.dbDao.getManager() != null) {
            return this.dbDao.getManager().openOrCreateDatabase(this.dbDao.getDbName());
        }
        return null;
    }

    // 从asset构建新建脚本，格式【database/表名/create.sql】
    private void tryCreateFromFile(SQLiteDatabase db) {
        if (this.context != null) {
            try {
                String[] files = this.context.getAssets().list(DB_FILE_DIR_NAME + File.separator + this.getTableName());
                if (files != null && files.length > 0) {
                    for (String file : files) {
                        if (FILE_NAME_CREATE.equalsIgnoreCase(file)) {
                            this.execAssetSqlFile(db, DB_FILE_DIR_NAME + File.separator + this.getTableName() + File.separator + FILE_NAME_CREATE);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                Logger.d(TAG, "tryCreateFromFile exception", e);
            }
        }
    }

    // 从asset构建更新脚本，格式【database/表名/update.旧版本.新版本.sql】
    private void tryUpdateFromFile(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (this.context != null && newVersion > oldVersion) {
            try {
                String[] files = this.context.getAssets().list(DB_FILE_DIR_NAME + File.separator + this.getTableName());
                if (files != null && files.length > 0) {
                    Arrays.sort(files);
                    for (int idx = oldVersion; idx < newVersion; idx++) {
                        for (String file : files) {
                            String update = String.format(Locale.getDefault(),FILE_NAME_UPDATE_PATTEN, idx, idx + 1);
                            if (update.equalsIgnoreCase(file)) {
                                this.execAssetSqlFile(db, DB_FILE_DIR_NAME + File.separator + this.getTableName() + File.separator + update);
                                break;
                            }
                        }
                    }

                }
            } catch (Exception e) {
                Logger.d(TAG, "tryUpdateFromFile exception", e);
            }
        }
    }

    // 执行asset下sql文件
    private void execAssetSqlFile(SQLiteDatabase db, String filepath) {
        BufferedReader br = null;
        try {
            db.beginTransaction();
            InputStream is = this.context.getAssets().open(filepath);
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder sql = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sql.append(line+"\r\n");
                if (line.trim().endsWith(";")) {//遇到分号先执行
                    db.execSQL(sql.toString());
                    sql.setLength(0);
                }
            }
            if (sql.length() > 0) {
                db.execSQL(sql.toString());
            }
            db.setTransactionSuccessful();
            is.close();
        } catch (Exception e) {
            Logger.d(TAG, "execute asset sql file exception", e);
        } finally {
            db.endTransaction();
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ignored) {

                }
            }
        }
    }
}
