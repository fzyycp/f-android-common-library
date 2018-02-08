package cn.faury.android.library.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;

import java.io.Serializable;
import java.util.Map;

import cn.faury.android.library.common.R;


/**
 * Activity管理类
 */
public class ActivityUtils {

    /**
     * 启动Activity
     *
     * @param context  上下文
     * @param activity 要启动的Activity
     * @param exts     传递参数
     */
    public static void startActivity(Activity context, Class<? extends Activity> activity, Map<String, Serializable> exts) {
        Intent intent = new Intent(context, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (exts != null) {
            for (Map.Entry<String, Serializable> entry : exts.entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.f_library_common_in_from_right, R.anim.f_library_common_out_to_left);

    }

    /**
     * 启动Activity
     *
     * @param context     上下文
     * @param activity    要启动的Activity
     * @param requestCode 请求码
     * @param exts        传递参数
     */
    public static void startActivityForResult(Activity context, int requestCode, Class<? extends Activity> activity, Map<String, Serializable> exts) {
        Intent intent = new Intent(context, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (exts != null) {
            for (Map.Entry<String, Serializable> entry : exts.entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }
        context.startActivityForResult(intent, requestCode);
        context.overridePendingTransition(R.anim.f_library_common_in_from_right, R.anim.f_library_common_out_to_left);

    }

    /**
     * 关闭Activity
     *
     * @param context 要关闭的activity
     */
    public static void finishActivityWithAnim(Activity context) {
        if (context == null) {
            return;
        }
        try {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && imm.isActive() && context.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception ignored) {
        }

        context.finish();
        context.overridePendingTransition(R.anim.f_library_common_in_from_left, R.anim.f_library_common_out_to_right);
    }
}
