package cn.faury.android.library.common.sqlite.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
     * 数据库信息
     */
    private BaseDatabaseDao dbDao;

    /**
     * 构造函数
     */
    public BaseTableDao() {
    }

    /**
     * 绑定数据库
     * @param dbDao 数据库操作器
     */
    public void bindDatabaseDao(BaseDatabaseDao dbDao){
        this.dbDao = dbDao;
    }

    /**
     * 获取数据库操作群
     */
    public BaseDatabaseDao getDatabaseDao(){
        return this.dbDao;
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
            cursor = getDatabase().query(true, getTableName(), null, selection, selectionArgs, null, null, sortOrder, null);
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
        if (this.dbDao!=null && this.dbDao.getManager()!=null){
            return this.dbDao.getManager().openOrCreateDatabase(this.dbDao.getDbName());
        }
        return null;
    }
}
