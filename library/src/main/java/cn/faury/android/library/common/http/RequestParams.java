package cn.faury.android.library.common.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 */

public class RequestParams implements Serializable {

    /**
     * URL参数列表
     */
    private ConcurrentHashMap<String, String> stringParams;
    private ConcurrentHashMap<String, StreamWrapper> streamParams;
    private ConcurrentHashMap<String, FileWrapper> fileParams;
    private ConcurrentHashMap<String, List<FileWrapper>> fileArrayParams;

    /**
     * 自动关闭输入流
     */
    private boolean autoCloseInputStreams;

    /**
     * 请求编码
     */
    private String contentEncoding = "UTF-8";

    public RequestParams() {
        this(null);
    }

    public RequestParams(final String key, final String value) {
        this(new HashMap<String, String>() {
            {
                this.put(key, value);
            }
        });
    }

    public RequestParams(final Map<String, String> source) {
        this.stringParams = new ConcurrentHashMap<>();
        this.streamParams = new ConcurrentHashMap<>();
        this.fileParams = new ConcurrentHashMap<>();
        this.fileArrayParams = new ConcurrentHashMap<>();
        this.contentEncoding = "UTF-8";
        if (source != null) {
            Iterator<Map.Entry<String, String>> iterator = source.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                this.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public void put(final String key, final String value) {
        if (key != null && value != null) {
            this.stringParams.put(key, value);
        }
    }

    public void put(String key, File[] files) throws FileNotFoundException {
        this.put(key, (File[]) files, (String) null, (String) null);
    }

    public void put(String key, File[] files, String contentType, String customFileName) throws FileNotFoundException {
        if (key != null) {
            List<FileWrapper> fileWrappers = new ArrayList();
            File[] var6 = files;
            int var7 = files.length;
            int var8 = 0;

            while (true) {
                if (var8 >= var7) {
                    this.fileArrayParams.put(key, fileWrappers)
                    ;
                    break;
                }

                File file = var6[var8];
                if (file == null || !file.exists()) {
                    throw new FileNotFoundException();
                }

                fileWrappers.add(new FileWrapper(file, contentType, customFileName));
                ++var8;
            }
        }

    }

    public void put(String key, File file) throws FileNotFoundException {
        this.put(key, (File) file, (String) null, (String) null);
    }

    public void put(String key, String customFileName, File file) throws FileNotFoundException {
        this.put(key, (File) file, (String) null, customFileName);
    }

    public void put(String key, File file, String contentType) throws FileNotFoundException {
        this.put(key, (File) file, contentType, (String) null);
    }

    public void put(String key, File file, String contentType, String customFileName) throws FileNotFoundException {
        if (file != null && file.exists()) {
            if (key != null) {
                this.fileParams.put(key, new FileWrapper(file, contentType, customFileName));
            }

        } else {
            throw new FileNotFoundException();
        }
    }

    public void put(String key, InputStream stream) {
        this.put(key, (InputStream) stream, (String) null);
    }

    public void put(String key, InputStream stream, String name) {
        this.put(key, (InputStream) stream, name, (String) null);
    }

    public void put(String key, InputStream stream, String name, String contentType) {
        this.put(key, stream, name, contentType, this.autoCloseInputStreams);
    }

    public void put(String key, InputStream stream, String name, String contentType, boolean autoClose) {
        if (key != null && stream != null) {
            this.streamParams.put(key, StreamWrapper.newInstance(stream, name, contentType, autoClose));
        }

    }

    public void setAutoCloseInputStreams(boolean flag) {
        this.autoCloseInputStreams = flag;
    }

    /**
     * 获取参数字符串形式
     *
     * @return 参数字符串
     */
    public String getQueryString() {
        StringBuffer query = new StringBuffer();
        if (this.stringParams != null) {
            Iterator<Map.Entry<String, String>> params = this.stringParams.entrySet().iterator();
            while (params.hasNext()) {
                Map.Entry<String, String> param = params.next();
                query.append(String.format("%s=%s", param.getKey(), param.getValue()));
                if (params.hasNext()) {
                    query.append("&");
                }
            }
        }
        return query.toString();
    }

    public ConcurrentHashMap<String, String> getStringParams() {
        return stringParams;
    }

    public ConcurrentHashMap<String, StreamWrapper> getStreamParams() {
        return streamParams;
    }

    public ConcurrentHashMap<String, FileWrapper> getFileParams() {
        return fileParams;
    }

    public ConcurrentHashMap<String, List<FileWrapper>> getFileArrayParams() {
        return fileArrayParams;
    }

    public static class StreamWrapper {
        public final InputStream inputStream;
        public final String name;
        public final String contentType;
        public final boolean autoClose;

        public StreamWrapper(InputStream inputStream, String name, String contentType, boolean autoClose) {
            this.inputStream = inputStream;
            this.name = name;
            this.contentType = contentType;
            this.autoClose = autoClose;
        }

        static StreamWrapper newInstance(InputStream inputStream, String name, String contentType, boolean autoClose) {
            return new StreamWrapper(inputStream, name, contentType == null ? "application/octet-stream" : contentType, autoClose);
        }
    }

    public static class FileWrapper implements Serializable {
        public final File file;
        public final String contentType;
        public final String customFileName;

        public FileWrapper(File file, String contentType, String customFileName) {
            this.file = file;
            this.contentType = contentType;
            this.customFileName = customFileName;
        }
    }

}
