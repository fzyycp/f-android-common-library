package cn.faury.android.library.common.sqlite.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 表操作DAO
 */

public interface ITableContentDao {

    /**
     * 插入数据
     *
     * @param values 数据.
     * @return -1表示失败，其他表示返回的id
     */
    long insert(ContentValues values);

    /**
     * 插入数据，冲突时处理方案
     *
     * @param values            数据
     * @param conflictAlgorithm 冲突处理
     * @return -1表示失败，其他表示返回的id
     */
    long insertWithOnConflict(ContentValues values, int conflictAlgorithm);

    /**
     * 删除数据
     *
     * @param selection     条件表达式
     * @param selectionArgs 条件表达式参数值
     * @return -1表示失败，其他表示影响行数
     */
    int delete(String selection, String[] selectionArgs);

    /**
     * 更新数据
     *
     * @param values        数据.
     * @param selection     条件表达式
     * @param selectionArgs 条件表达式参数值
     * @return -1表示失败，其他表示影响行数
     */
    int update(ContentValues values, String selection, String[] selectionArgs);

    /**
     * 查询
     *
     * @param projection    要查询的列，null表示所有
     * @param selection     条件表达式
     * @param selectionArgs 条件表达式参数值
     * @param sortOrder     排序字段（ORDER BY clause）
     * @return Cursor或者null.
     */
    Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder);

    /**
     * 获取数据库操作对象
     *
     * @return 数据库操作对象
     */
    SQLiteDatabase getDatabase();

    /**
     * 获取表名
     *
     * @return 表名
     */
    String getTableName();

}
