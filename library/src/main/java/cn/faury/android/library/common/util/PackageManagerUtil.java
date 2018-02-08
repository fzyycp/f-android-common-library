package cn.faury.android.library.common.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.File;
import java.util.Locale;

public final class PackageManagerUtil {
    private final static int kSystemRootStateUnknow = -1;
    private final static int kSystemRootStateDisable = 0;
    private final static int kSystemRootStateEnable = 1;
    private static int systemRootState = kSystemRootStateUnknow;

    /**
     * 读写SD卡权限
     */
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    /**
     * 摄像头权限
     */
    public static final String PERMISSION_CAMERA = "android.permission.CAMERA";
    public static final String PERMISSION_WRITE_SETTINGS = "android.permission.WRITE_SETTINGS";
    public static final String PERMISSION_INTERNET = "android.permission.INTERNET";
    public static final String PERMISSION_ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    public static final String PERMISSION_ACCESS_WIFI_STATE = "android.permission.ACCESS_WIFI_STATE";
    public static final String PERMISSION_ACCESS_NETWORK_STATE = "android.permission.ACCESS_NETWORK_STATE";
    public static final String PERMISSION_CHANGE_WIFI_STATE = "android.permission.CHANGE_WIFI_STATE";
    public static final String PERMISSION_READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    public static final String PERMISSION_WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";
    public static final String PERMISSION_READ_CONTACTS = "android.permission.READ_CONTACTS";

    /**
     * 获取APP版本号
     *
     * @param ctx 上下文
     * @return APP版本号
     */
    public static final String getAppVersion(final Context ctx) {
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取项目名称APP名
     *
     * @param ctx 上下文
     * @return APP名
     */
    public static final String getAppName(final Context ctx) {
        PackageManager pm = ctx.getPackageManager();
        return ctx.getApplicationInfo().loadLabel(pm).toString();
    }

    /**
     * 获取手机系统版本号
     *
     * @return 手机系统版本号
     */
    public static final String getSystemVersionRelease() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机系统编译版本号
     *
     * @return 手机系统编译版本号
     */
    public static final int getSystemVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 检查是否有权限
     *
     * @param ctx        上下文
     * @param permission 权限
     * @return 是否有权限
     */
    public static final boolean hasPermission(final Context ctx, String permission) {
        PackageManager pm = ctx.getPackageManager();
        return PackageManager.PERMISSION_GRANTED == pm.checkPermission(permission, ctx.getPackageName());
    }

    /**
     * 获取手机IMEI（唯一标识）
     *
     * @param ctx        上下文
     * @return IMEI
     */
    public static String getIMEI(final Context ctx) {
        String imei = "Unknown";
        try {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(ctx.TELEPHONY_SERVICE);
            if (tm != null) {
                imei = tm.getDeviceId();
            }
        } catch (SecurityException ignored) {

        }
        return imei;
    }

    /**
     * 获取手机名称
     *
     * @return 手机名称
     */
    public static String getDeviceName() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取设备型号
     *
     * @return 设备型号 deviceName
     */
    public static String getDeviceModel() {
        return new Build().MODEL;
    }

    /**
     * 获取手机是否已root
     *
     * @return true：已root；false：未root
     */
    public static boolean isRootSystem() {
        if (systemRootState == kSystemRootStateEnable) {
            return true;
        } else if (systemRootState == kSystemRootStateDisable) {
            return false;
        }
        File f = null;
        final String kSuSearchPaths[] = {"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/"};
        try {
            for (int i = 0; i < kSuSearchPaths.length; i++) {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists()) {
                    systemRootState = kSystemRootStateEnable;
                    return true;
                }
            }
        } catch (Exception e) {
        }
        systemRootState = kSystemRootStateDisable;
        return false;
    }

    /**
     * 获取设备语言
     *
     * @return 设备语言
     */
    public static String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取设备国家
     *
     * @return 设备国家
     */
    public static String getCountry() {
        return Locale.getDefault().getCountry();
    }

    /**
     * 获取MetaData数据
     *
     * @param ctx 上下文
     * @param key key
     * @return 配置的值
     */
    public static String getMetaData(final Context ctx, final String key) {
        try {
            ApplicationInfo appInfo = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
