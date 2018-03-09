package cn.faury.android.library.common.helper.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

import cn.faury.android.library.common.helper.sqlite.bean.DatabaseInfo;
import cn.faury.android.library.common.util.StorageUtils;

/**
 * 数据库管理器
 */

public class DatabaseManager extends SQLiteOpenHelper {
    /**
     * 上下文
     */
    private Context context;
    /**
     * 管理数据库的库名
     */
    private static final String DB_NAME = "database_manager.db";

    /**
     * 管理库版本号
     */
    private static final int DB_VERSION = 1;

    /**
     * 构造函数
     */
    private DatabaseManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    /**
     * 创建表
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DatabaseInfo.TB_NAME +
                "(" + DatabaseInfo.TB_COL_NAME + " TEXT NOT NULL PRIMARY KEY ON CONFLICT REPLACE" +
                ", " + DatabaseInfo.TB_COL_VERSION + " INTEGER DEFAULT 1" +
                ", " + DatabaseInfo.TB_COL_DIR + " TEXT)");
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

    }

    public SQLiteDatabase getReadableDatabase(String dbName) {
        DatabaseInfo databaseInfo = getDatabaseInfo(dbName);
        if(databaseInfo == null){
            databaseInfo = new DatabaseInfo(dbName,1, StorageUtils.getStoragePrivateDir(this.context));
        }
        SQLiteDatabase database = SQLiteDatabase.openDatabase(new File(databaseInfo.getDir(),databaseInfo.getName()),null);
    }

    public SQLiteDatabase getWritableDatabase(String dbName) {

    }

    /**
     * 获取数据信息对象
     * @param dbName 数据库名
     * @return 数据库对象
     */
    public DatabaseInfo getDatabaseInfo(String dbName) {
        DatabaseInfo info = null;
        Cursor cursor = null;
        try {
            cursor = this.getReadableDatabase().query(true, DatabaseInfo.TB_NAME, null
                    , DatabaseInfo.TB_COL_NAME + "=?", new String[]{dbName}, null, null, null, "1");
            if (cursor != null && cursor.moveToFirst()) {
                info = new DatabaseInfo(cursor);
            }
        } catch (Exception e) {
            info = null;
        } finally {
            // close the cursor
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return info;
    }

    public static class Builder {

        private Context context;
        private DatabaseManager manager;

        public Builder(Context context) {
            this.context = context;
            this.manager = new DatabaseManager(context);
        }


        public Builder addDatabase(DatabaseInfo dbInfo, DatabaseCallback dbCb) {
            DatabaseInfo info = this.manager.getDatabaseInfo(dbInfo.getName());
            if(info!=null){
                dbInfo
            }

            ContentValues values = new ContentValues();
            values.put(DatabaseInfo.TB_COL_NAME, dbInfo.getName());
            values.put(DatabaseInfo.TB_COL_VERSION, dbInfo.getVersion());
            values.put(DatabaseInfo.TB_COL_DIR, dbInfo.getDir());
            this.manager.getWritableDatabase().insert(DatabaseInfo.TB_NAME, null, values);
            return this;
        }
    }
}
