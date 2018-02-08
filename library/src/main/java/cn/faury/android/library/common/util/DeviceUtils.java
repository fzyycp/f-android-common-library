package cn.faury.android.library.common.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 设备工具类
 */
public class DeviceUtils {

    /**
     * 得到当前的手机网络类型
     *
     * @param ctx 上下文
     * @return 手机网络类型
     */
    public static String getCurrentNetType(final Context ctx) {
        String type = "";
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info == null) {
                type = "";
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                type = "wifi";
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                int subType = info.getSubtype();
                if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS
                        || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                    type = "2g";
                } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                        || subType == TelephonyManager.NETWORK_TYPE_EVDO_A
                        || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                        || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                    type = "3g";
                } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
                    type = "4g";
                }
            }
        }
        return type;
    }

    /**
     * 判断是否处于WIFI 环境
     *
     * @param ctx 上下文
     * @return 是否WIFI
     */
    public static boolean isWifiNet(final Context ctx) {
        return "wifi".equals(getCurrentNetType(ctx));
    }

    /**
     * 判断是否处于手机运营商网络环境
     *
     * @param ctx 上下文
     * @return 是否运营商网络
     */
    public static boolean isMobileNet(final Context ctx) {
        return "2g".equals(getCurrentNetType(ctx))
                || "3g".equals(getCurrentNetType(ctx))
                || "4g".equals(getCurrentNetType(ctx));
    }

    /**
     * 判断网络连接是否连接
     *
     * @param ctx 上下文
     * @return 网络是否联通
     */
    public static boolean isNetworkAvailable(final Context ctx) {
        boolean available = false;
        // 得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 去进行判断网络是否连接
        if (manager != null && manager.getActiveNetworkInfo() != null) {
            available = manager.getActiveNetworkInfo().isAvailable();
        }
        return available;
    }

    /**
     * 获取设备当前的IP地址
     *
     * @return 设备IP
     */
    public static String getLocalIP() {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        if (inetAddress instanceof Inet4Address) {
                            ip = inetAddress.getHostAddress();
                        } else if (inetAddress instanceof Inet6Address) {
                            ip = inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return ip;
    }

    /**
     * 获取设备MAC地址
     *
     * @param ctx 上下文
     * @return 设备MAC地址
     */
    public static String getMacAddress(final Context ctx) {
        String address = "";
        WifiManager wifi = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi != null) {
            WifiInfo info = wifi.getConnectionInfo();
            if (info != null) {
                address = info.getMacAddress();
            }
        }
        return address == null ? "" : address;
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param ctx 上下文
     * @return 是否开启GPS
     */
    public static boolean isGPSOpen(final Context ctx) {
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 强制帮用户打开GPS
     *
     * @param ctx 上下文
     * @return 打开GPS
     */
    public static boolean openGPS(final Context ctx) {
        boolean success = false;
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(ctx, 0, GPSIntent, 0).send();
            success = true;
        } catch (PendingIntent.CanceledException e) {
            success = false;
        }
        return success;
    }
}
