package cn.faury.android.library.common.core;

import java.io.File;

/**
 * 全局常量
 */
public final class FCommonGlobalConfigure {

    /**
     * 统一log前缀
     */
    public static final String TAG = "cn.faury.android.library.common";

    /**
     * faury在储存器上的根目录
     */
    public static final String DIR_FAURY = ".cn.faury.android" + File.separator + ".nomedia";

    /**
     * library在储存器上的根目录
     */
    public static final String DIR_LIBRARY = DIR_FAURY + File.separator + "library";

    /**
     * 图片缓存路径
     */
    public static final String DIR_IMAGE_CACHE = DIR_LIBRARY + File.separator + "image";

    /**
     * 网络请求缓存
     */
    public static final String DIR_HTTP_CACHE = DIR_LIBRARY + File.separator + "http";

    /**
     * 日志目录
     */
    public static final String DIR_LOG = DIR_LIBRARY + File.separator + "log";

    /**
     * 临时目录
     */
    public static final String DIR_TEMP = DIR_LIBRARY + File.separator + "temp";

    /**
     * 下载目录
     */
    public static final String DIR_DOWNLOAD = DIR_LIBRARY + File.separator + "download";

    /**
     * 数据库管理目录
     */
    public static final String DIR_SQLITE = DIR_LIBRARY + File.separator + "sqlite";

}
