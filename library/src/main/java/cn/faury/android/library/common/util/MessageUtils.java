package cn.faury.android.library.common.util;

import android.os.Handler;
import android.os.Message;

/**
 * 消息助手
 */

public final class MessageUtils {
    /**
     * 发送消息
     *
     * @param handler 发送消息句柄
     * @param what    消息类型
     * @param obj     消息参数
     * @return 发送结果
     */
    public static boolean sendMessage(Handler handler, int what, Object obj) {
        if (handler == null) {
            return false;
        }
        Message message = new Message();
        message.what = what;
        message.obj = obj;
        return handler.sendMessage(message);
    }

    /**
     * 发送消息
     *
     * @param handler     发送消息句柄
     * @param what        消息类型
     * @param obj         消息参数
     * @param delayMillis 延时毫秒数
     * @return 发送结果
     */
    public static boolean sendMessageDelayed(Handler handler, int what, Object obj, long delayMillis) {
        if (handler == null) {
            return false;
        }
        Message message = new Message();
        message.what = what;
        message.obj = obj;
        return handler.sendMessageDelayed(message, delayMillis);
    }
}
