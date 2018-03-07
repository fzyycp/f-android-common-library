package cn.faury.android.library.common.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.faury.android.library.common.core.FCommonGlobalConstant;
import cn.faury.android.library.common.helper.Logger;
import cn.faury.android.library.common.util.FileUtils;
import cn.faury.android.library.common.util.StorageUtils;
import cn.faury.android.library.common.util.StringUtils;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dns;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 通用Http请求处理
 */

public class HttpRequest {
    /**
     * 日志tag
     */
    private final String TAG = FCommonGlobalConstant.TAG + " - HttpRequest";

    /**
     * 设置client对象
     */
    private OkHttpClient client = null;

    /**
     * 获取DNS解析服务对象
     * @return DNS解析服务
     */
    public Dns getDns(){
        return null;
    }

    /**
     * 获取超时时间
     * @return 超时时间
     */
    public long getTimeout(){
        return 15;
    }

    /**
     * 超时时间单位
     * @return 时间单位
     */
    public TimeUnit getTimeoutUnit(){
        return TimeUnit.SECONDS;
    }

    /**
     * 获取网络缓存大小
     * @return 缓存大小
     */
    public long getCacheSize(){
        return 10 * 1024 * 1024;
    }

    /**
     * 获取缓存文件
     * @return 缓存文件
     */
    public File getCacheFile(){
        return new File(StorageUtils.getStorageFile(), FCommonGlobalConstant.DIR_HTTP_CACHE);
    }

    /**
     * 获取网络请求对象
     *
     * @return 网络请求对象
     */
    public synchronized OkHttpClient getClient() {
        if (client == null) {
            synchronized (HttpRequest.class) {
                if (client == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .connectTimeout(getTimeout(), getTimeoutUnit());
                    if (this.getDns()!=null){
                        builder.dns(this.getDns());
                    }
                    try {//如果能够写入磁盘，则创建缓存目录
                        File file = getCacheFile();
                        if (!file.exists()) {
                            FileUtils.createFolder(file);
                        }
                        builder.cache(new Cache(file.getAbsoluteFile(), getCacheSize()));
                    } catch (Exception ignored) {
                    }
                    client = builder.build();
                }
            }
        }

        return client;
    }

    /**
     * 网络请求
     *
     * @param method   请求方法
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 回调
     */
    protected void _request(final String method, final String url, final Map<String, String> params, final Callback callback) {
        Logger.d(TAG, String.format("_request: method=%s,url=%s,params=%s", method, url, params));
        if (url == null) {
            return;
        }
        String _url = url;
        Request.Builder builder = new Request.Builder();

        // 添加查询参数
        if ("GET".equalsIgnoreCase(method)) { // get请求
            String queryString = "";
            if (params != null && params.size() > 0) {
                RequestParams requestParams = new RequestParams(params);
                queryString = requestParams.getQueryString();
            }
            if (!StringUtils.isEmpty(queryString)) {
                if (_url.indexOf("?") > 0) {
                    _url = _url + "&" + queryString;
                } else {
                    _url = _url + "?" + queryString;
                }
            }
            builder.get();
        } else {// 当做POST处理
            FormBody.Builder formBody = new FormBody.Builder();
            // 传入的参数
            if (params != null && params.size() > 0) {
                Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> i = iterator.next();
                    formBody.add(i.getKey(), i.getValue());
                }
            }
            builder.post(formBody.build());
        }
        Request request = builder.url(_url).build();
        Call call = getClient().newCall(request);
        if (call != null) {
            call.enqueue(callback);
        }
    }

    /**
     * 发起get请求
     *
     * @param url      请求的地址
     * @param params   请求的参数
     * @param callback 回调
     */
    public void get(final String url, final Map<String, String> params, final Callback callback) {
        _request("GET", url, params, callback);
    }

    /**
     * 发起Post请求
     *
     * @param url      请求的地址
     * @param params   请求的参数
     * @param callback 回调
     */
    public void post(final String url, final Map<String, String> params, final Callback callback) {
        _request("POST", url, params, callback);
    }

    /**
     * 下载
     *
     * @param url              下载文件地址
     * @param toPath           文件保存路径，全路径，如果是以/结尾，则取url最后一段名作为文件名
     * @param downloadListener 下载监听器
     */
    public void download(final String url, final String toPath, final OnDownloadListener downloadListener) {
        Logger.d(TAG, String.format("download: url=%s,toPath=%s", url, toPath));
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(toPath)) {
            downloadListener.onDownloadFailed("下载地址或保存路径不可以为空",null);
        } else {
            Request builder = new Request.Builder().url(url).build();
            getClient().newCall(builder).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    downloadListener.onDownloadFailed(null,e);
                }

                @Override
                public void onResponse(Call call, Response response)  {
                    if(response.body() == null){
                        downloadListener.onDownloadFailed("下载资源不存在",null);
                    }
                    try {
                        // 创建目标文件
                        String _toPath = toPath;
                        if (_toPath.endsWith("/")) {
                            _toPath = _toPath + FileUtils.getNameFromUrl(url);
                        }
                        String outPath = FileUtils.createFile(_toPath, FileUtils.Mode.RELATIVE_PATH_AND_COVER);
                        if(StringUtils.isEmpty(outPath)){
                            downloadListener.onDownloadFailed("下载文件保存路径为空",null);
                        } else {
                            InputStream is = response.body().byteStream();
                            long total = response.body().contentLength();
                            downloadListener.beforeDownloading(total);

                            FileOutputStream fos = new FileOutputStream(new File(outPath));
                            long sum = 0;
                            byte[] buff = new byte[2048];
                            int len = 0;
                            while ((len = is.read(buff)) != -1) {
                                fos.write(buff, 0, len);
                                sum += len;
                                downloadListener.onDownloading(sum);
                            }
                            fos.flush();
                            downloadListener.onDownloadSuccess(outPath);
                        }
                    } catch (IOException e) {
                        downloadListener.onDownloadFailed(null,e);
                    }
                }
            });
        }
    }

    /**
     * 下载监听器
     */
    public interface OnDownloadListener {
        /**
         * 开始下载前
         * @param total 总大小
         */
        void beforeDownloading(final long total);

        /**
         * 下载中
         *
         * @param progress 下载进度
         */
        void onDownloading(final long progress);

        /**
         * 下载成功
         * @param outPath 文件保存绝对路径
         */
        void onDownloadSuccess(String outPath);

        /**
         * 下载失败
         *
         * @param message 错误消息
         * @param e 异常类型
         */
        void onDownloadFailed(final String message, final Exception e);
    }
}
