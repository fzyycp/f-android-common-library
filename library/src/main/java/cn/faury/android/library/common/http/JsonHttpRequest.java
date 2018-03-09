package cn.faury.android.library.common.http;

import android.content.Context;

import java.io.IOException;
import java.util.Map;

import cn.faury.android.library.common.core.FCommonGlobalConfigure;
import cn.faury.android.library.common.helper.Logger;
import cn.faury.android.library.common.util.JsonHashMapUtils;
import cn.faury.android.library.common.util.JsonUtils;
import cn.faury.android.library.common.util.StringUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * JSON网络请求
 */

public class JsonHttpRequest extends HttpRequest {
    /**
     * 日志tag
     */
    private final String TAG = FCommonGlobalConfigure.TAG + " - JsonHttpRequest";

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public JsonHttpRequest(Context context) {
        super(context);
    }

    /**
     * JSON请求
     *
     * @param method  请求方法
     * @param url     请求的地址
     * @param params  请求的参数
     * @param handler 处理返回结果
     */
    protected void _jsonRequest(String method, String url, Map<String, String> params, final ResponseHandler handler) {
        _request(method, url, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.e(TAG, "onFailure: ", e);
                if (handler != null) {
                    handler.onFailure(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Logger.v(TAG, "onResponse: " + response);
                if (handler != null) {
                    if (response.code() == 200) {
                        String resultJson = response.body().string();
                        if (StringUtils.isEmpty(resultJson)) {
                            handler.onSuccess(new JsonHashMapUtils());
                        } else {
                            handler.onSuccess(JsonUtils.fromJson(resultJson));
                        }
                    } else {
                        handler.onFailure(response.code());
                    }
                }
            }
        });
    }

    /**
     * 发起get请求
     *
     * @param url     请求的地址
     * @param params  请求的参数
     * @param handler 处理返回结果
     */
    public void get(String url, Map<String, String> params, final ResponseHandler handler) {
        _jsonRequest("GET", url, params, handler);
    }

    /**
     * 发起Post请求
     *
     * @param url     请求的地址
     * @param params  请求的参数
     * @param handler 处理返回结果
     */
    public void post(String url, Map<String, String> params, final ResponseHandler handler) {
        _jsonRequest("POST", url, params, handler);
    }

    /**
     * JSON请求处理器
     */
    public interface ResponseHandler {

        /**
         * 网络请求成功
         *
         * @param result 返回的JSON数据
         */
        void onSuccess(JsonHashMapUtils result);

        /**
         * 网络请求失败
         *
         * @param e 连接异常
         */
        void onFailure(IOException e);

        /**
         * 网络请求失败
         *
         * @param code 服务请求返回非200code
         */
        void onFailure(int code);
    }
}
