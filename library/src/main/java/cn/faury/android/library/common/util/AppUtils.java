package cn.faury.android.library.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * APP工具类
 */

public class AppUtils {

    /**
     * 判断用户是否安装APP客户端
     */
    public static boolean isAppAvilible(Context context,String packageName) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        return isAppAvilible(context,"com.tencent.mm");
    }

    /**
     * 判断 用户是否安装QQ客户端
     */
    public static boolean isQQClientAvailable(Context context) {
        return isAppAvilible(context,"com.tencent.qqlite") || isAppAvilible(context,"com.tencent.mobileqq");
    }

}
