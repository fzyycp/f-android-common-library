package cn.faury.android.library.common.helper.sqlite.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.faury.android.library.common.helper.Logger;
import cn.faury.android.library.common.helper.sqlite.DatabaseServer;
import cn.faury.android.library.common.helper.sqlite.bean.AbstractDatabaseBean;
import cn.faury.android.library.common.helper.sqlite.dao.DatabaseCallback;
import cn.faury.android.library.common.helper.sqlite.dao.TableContentDao;

/**
 * 数据库表信息
 */

public abstract class AbstractTableDao implements TableContentDao, DatabaseCallback {
    /**
     * TAG
     */
    public static final String TAG = AbstractTableDao.class.getName();

    /**
     * 数据库信息
     */
    private AbstractDatabaseBean databaseInfo;

    /**
     * 表名
     */
    private String tableName;

    public AbstractTableDao(AbstractDatabaseBean databaseInfo, String tableName) {
        this.databaseInfo = databaseInfo;
        this.tableName = tableName;
    }

    @Override
    public long insert(ContentValues values) {
        long id = -1;
        try {
            id = getDatabase().insert(tableName, null, values);
        } catch (Exception e) {
            Logger.e(TAG, "insert exception:" + e.getMessage(), e);
        }
        return id;
    }

    @Override
    public int delete(String selection, String[] selectionArgs) {
        int count = -1;
        try {
            count = getDatabase().delete(tableName, selection, selectionArgs);
        } catch (Exception e) {
            Logger.e(TAG, "delete exception:" + e.getMessage(), e);
        }
        return count;
    }

    @Override
    public int update(ContentValues values, String selection, String[] selectionArgs) {
        int count = -1;
        try {
            count = getDatabase().update(tableName, values, selection, selectionArgs);
        } catch (Exception e) {
            Logger.e(TAG, "update exception:" + e.getMessage(), e);
        }
        return count;
    }

    @Override
    public Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        try {
            cursor = getDatabase().query(true, tableName, null, selection, selectionArgs, null, null, sortOrder, null);
        } catch (Exception e) {
            Logger.e(TAG, "query exception:" + e.getMessage(), e);
        }
        return cursor;
    }

    /**
     * get the database object
     *
     * @return database object
     */
    @Override
    public SQLiteDatabase getDatabase() {
        return DatabaseServer.getInstance().getDatabase(this.databaseInfo.getDbName());
    }

    @Override
    public String getTableName() {
        return tableName;
    }
}
