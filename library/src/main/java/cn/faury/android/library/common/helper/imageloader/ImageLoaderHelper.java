package cn.faury.android.library.common.helper.imageloader;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

import cn.faury.android.library.common.core.FCommonGlobalConstant;
import cn.faury.android.library.common.util.FileUtils;
import cn.faury.android.library.common.util.StorageUtils;

/**
 * ImageLoader帮助类
 */

public class ImageLoaderHelper {

    /**
     * 获取默认的ImageLoader配置
     *
     * @param context 上下文
     * @return 配置对象
     */
    public static ImageLoaderConfiguration getDefaultConfig(Context context) {
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(3)
                .memoryCache(new MyLruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSizePercentage(13);

        try {//如果能够写入磁盘，则创建缓存目录
            File file = new File(StorageUtils.getStorageFile(), FCommonGlobalConstant.DIR_IMAGE_CACHE);
            if (!file.exists()) {
                FileUtils.createFolder(file);
            }
            builder.diskCache(new LruDiskCache(file, new HashCodeFileNameGenerator() // 磁盘缓存
                    , 50 * 1024 * 1024))
                    .diskCacheFileCount(100).writeDebugLogs();

        } catch (Exception ignored) {
        }
        return builder.build();
    }

    /**
     * 获取默认的ImageLoader
     *
     * @param context 上下文
     * @return ImageLoader对象
     */
    public static ImageLoader getDefaultImageLoader(Context context) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(getDefaultConfig(context));
        return imageLoader;
    }

    /**
     * 获取默认的DisplayImageOptions
     *
     * @return DisplayImageOptions对象
     */
    public static DisplayImageOptions getDefaultDisplayImageOptions() {
        return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
    }
}
