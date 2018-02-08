package cn.faury.android.library.common.util;

import java.util.UUID;

/**
 * UUID生成类
 */
public class UUIDUtils {

    /**
     * 获取常用的UUID
     * @return UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取常用的UUID,不带破折号
     * @return UUID
     */
    public static String getUUIDNoDash() {
        String uuid = getUUID();
        return uuid.replace("-","");
    }
}
