package cn.faury.android.library.common.sqlite.bean;

/**
 * 数据库表信息
 */

public class DatabaseInfo {

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
     * 构造函数
     *
     * @param dbName    数据库名
     * @param dbVersion 版本号
     * @param dir       数据库目录
     */
    public DatabaseInfo(String dbName, int dbVersion, String dir) {
        this.dbName = dbName;
        this.dbVersion = dbVersion;
        this.dir = dir;
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
