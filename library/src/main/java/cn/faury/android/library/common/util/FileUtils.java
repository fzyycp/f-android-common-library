package cn.faury.android.library.common.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 */
public class FileUtils {
    /**
     * 文件大小单位换算
     */
    public static final BigDecimal UNIT = new BigDecimal(1024);

    public enum Mode {
        /**
         * 传入文件绝对路径，如果存在则覆盖
         */
        ABSOLUTE_PATH_AND_COVER
        /**
         * 传入文件绝对路径，如果存在则不处理
         */
        , ABSOLUTE_PATH_AND_NOTHING
        /**
         * 传入文件相对路径，如果存在则不处理
         */
        , RELATIVE_PATH_AND_COVER
        /**
         * 传入文件相对路径，如果存在则不处理
         */
        , RELATIVE_PATH_AND_NOTHING;
    }

    /**
     * 获取文件或者文件夹绝对路径
     *
     * @param path 文件或文件夹路径
     * @return 绝对路径
     */
    public static File getAbsoluteFile(final String path) {
        if (StringUtils.isNotEmpty(path) && StringUtils.isNotEmpty(StorageUtils.getStorageDirectory())
                && path.startsWith(StorageUtils.getStorageDirectory())) {
            return new File(path);
        } else {
            return new File(StorageUtils.getStorageFile(), path);
        }
    }

    /// ============== create file and dirctory==================

    /**
     * 创建文件
     *
     * @param file 文件路径（文件夹则忽略）
     * @param mode 操作模式
     * @return 返回创建成功文件的绝对路径
     * @throws IOException IO异常
     */
    public static String createFile(File file, Mode mode) throws IOException {
        if (file == null || StringUtils.isEmpty(file.getAbsolutePath()) || file.isDirectory()) {
            return null;
        }
        // 相对路径
        if (mode == Mode.RELATIVE_PATH_AND_COVER || mode == Mode.RELATIVE_PATH_AND_NOTHING) {
            file = new File(StorageUtils.getStorageFile(), file.getAbsolutePath());
        }
        // 先创建目录
        createFolder(file.getParentFile(), Mode.ABSOLUTE_PATH_AND_NOTHING);
        // 覆盖则先删除文件
        if (mode == Mode.RELATIVE_PATH_AND_COVER || mode == Mode.ABSOLUTE_PATH_AND_COVER) {
            deleteFile(file);
        }
        // 创建文件
        file.createNewFile();
        return file.getAbsolutePath();
    }

    /**
     * 创建文件
     *
     * @param filePath 文件路径（文件夹则忽略）
     * @param mode     操作模式
     * @return 返回创建成功文件的绝对路径
     * @throws IOException IO异常
     */
    public static String createFile(String filePath, Mode mode) throws IOException {
        return createFile(new File(filePath), mode);
    }

    /**
     * 创建文件，存在则覆盖
     *
     * @param file 文件绝对路径（文件夹则忽略）
     * @return 返回创建成功文件的绝对路径
     * @throws IOException IO异常
     */
    public static String createFile(File file) throws IOException {
        return createFile(file, Mode.ABSOLUTE_PATH_AND_COVER);
    }

    /**
     * 创建文件，存在则覆盖
     *
     * @param filePath 文件绝对路径（文件夹则忽略）
     * @return 返回创建成功文件的绝对路径
     * @throws IOException IO异常
     */
    public static String createFile(String filePath) throws IOException {
        return createFile(new File(filePath));
    }

    /**
     * 创建文件夹
     *
     * @param folder 文件夹路径
     * @param mode   创建模式
     * @return 成功创建后文件夹路径
     */
    public static File createFolder(File folder, Mode mode) {
        if (folder == null || StringUtils.isEmpty(folder.getAbsolutePath()) || folder.isFile()) {
            return null;
        }
        // 相对路径
        if (mode == Mode.RELATIVE_PATH_AND_COVER || mode == Mode.RELATIVE_PATH_AND_NOTHING) {
            folder = new File(StorageUtils.getStorageFile(), folder.getAbsolutePath());
        }
        // 覆盖已有的
        if (mode == Mode.RELATIVE_PATH_AND_COVER || mode == Mode.ABSOLUTE_PATH_AND_COVER) {
            deleteFolder(folder);
        }
        folder.mkdirs();
        return folder.getAbsoluteFile();
    }

    /**
     * 创建文件夹
     *
     * @param folder 文件夹路径
     * @param mode   创建模式
     * @return 成功创建后文件夹路径
     */
    public static File createFolder(String folder, Mode mode) {
        return createFolder(new File(folder), mode);
    }

    /**
     * 创建文件夹
     *
     * @param folder 文件夹绝对路径
     * @return 成功创建后文件夹路径
     */
    public static File createFolder(File folder) {
        return createFolder(folder, Mode.ABSOLUTE_PATH_AND_COVER);
    }

    /**
     * 创建文件夹
     *
     * @param folder 文件夹绝对路径
     * @return 成功创建后文件夹路径
     */
    public static File createFolder(String folder) {
        return createFolder(new File(folder));
    }

    // ============== delete file and dirctory==================

    /**
     * 删除文件
     *
     * @param file 要删除的文件绝对路径
     * @return 是否删除成功
     */
    public static boolean deleteFile(File file) {
        if (file == null || StringUtils.isEmpty(file.getAbsolutePath())) {
            return false;
        }
        if (file.exists() && file.isFile()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件
     *
     * @param filePath 要删除的文件绝对路径
     * @return 是否删除成功
     */
    public static boolean deleteFile(String filePath) {
        if (StringUtils.isNotEmpty(filePath)) {
            return deleteFile(new File(filePath));
        }
        return false;
    }

    /**
     * 删除文件夹（不能包含子文件夹或子文件）
     *
     * @param folder 要删除的文件绝对路径
     * @return 是否删除成功
     */
    public static boolean deleteFolder(File folder) {
        if (folder == null || StringUtils.isEmpty(folder.getAbsolutePath())) {
            return false;
        }
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null && files.length > 0) {
                return false;
            }
            return folder.delete();
        }
        return false;
    }

    /**
     * 删除文件夹（不能包含子文件夹或子文件）
     *
     * @param folderPath 要删除的文件绝对路径
     * @return 是否删除成功
     */
    public static boolean deleteFolder(String folderPath) {
        if (StringUtils.isNotEmpty(folderPath)) {
            return deleteFolder(new File(folderPath));
        }
        return false;
    }

    /**
     * 删除文件夹（级联删除子文件和子文件夹）
     *
     * @param folder 要删除的文件绝对路径
     * @return 是否删除成功
     */
    public static boolean deleteFolderCascade(File folder) {
        if (folder == null || StringUtils.isEmpty(folder.getAbsolutePath())) {
            return false;
        }
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file != null && file.isFile()) {
                        deleteFile(file);
                    } else {
                        deleteFolderCascade(file);
                    }
                }
            }
            return folder.delete();
        }
        return false;
    }

    /**
     * 删除文件夹（不能包含子文件夹或子文件）
     *
     * @param folderPath 文件夹路径
     * @return 是否删除成功
     */
    public static boolean deleteFolderCascade(String folderPath) {
        if (StringUtils.isNotEmpty(folderPath)) {
            return deleteFolderCascade(new File(folderPath));
        }
        return false;
    }

    // ===========find file in order to extendsions at the end=================

    /**
     * 过滤文件
     *
     * @param dir        要查询的目录
     * @param extensions 文件扩展名
     * @param files      返回结果
     */
    public static void fileFilter(File dir, List<File> files, String... extensions) {

        if (!dir.isDirectory()) {
            return;
        }
        File[] allFiles = dir.listFiles();

        // 存在子文件或子文件夹
        if (allFiles != null) {
            File[] allExtensionFiles = dir.listFiles(new FileFilter(extensions));
            if (allExtensionFiles != null) {
                for (File single : allExtensionFiles) {
                    files.add(single);
                }
            }

            // 递归子文件夹
            for (File single : allFiles) {
                if (single.isDirectory()) {
                    fileFilter(single, files, extensions);
                }
            }
        }
    }

    // ===========copy assert to a storage=================

    /**
     * 从asset文件夹拷贝文件到目标文件(文件已存在则放弃拷贝)
     *
     * @param ctx               上下文
     * @param strAssetsFilePath 源文件
     * @param strDesFilePath    目标文件
     * @return 是否拷贝成功
     */
    public static boolean assetsCopyData(Context ctx, String strAssetsFilePath, String strDesFilePath) {

        boolean isSuccess = false;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        File file = new File(strDesFilePath);
        if (!file.exists()) {
            try {
                // 创建目标文件
                file.createNewFile();
                Runtime.getRuntime().exec("chmod 766 " + file);

                // 复制文件
                inputStream = ctx.getAssets().open(strAssetsFilePath);
                outputStream = new FileOutputStream(file);

                int nLen = 0;
                byte[] buff = new byte[1024 * 1];
                while ((nLen = inputStream.read(buff)) > 0) {
                    outputStream.write(buff, 0, nLen);
                }
                outputStream.flush();
                isSuccess = true;
            } catch (IOException e) {
                isSuccess = false;
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                }
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                }
            }
        }

        return isSuccess;
    }

    // ===========copy single file.=================

    /**
     * 单个文件拷贝
     *
     * @param src        源文件
     * @param dst        目标文件
     * @param isOverride 是否覆盖存在的文件
     * @return 是否拷贝成功
     * @throws IOException IO异常
     */
    public static boolean copyFile(File src, File dst, boolean isOverride) throws IOException {

        if ((!src.exists()) || src.isDirectory() || dst.isDirectory()) {
            return false;
        }
        boolean isSuccess = false;

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(src);
            isSuccess = copyFile(inputStream, dst, isOverride);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
            }
        }
        return isSuccess;
    }

    /**
     * 从输入流复制文件
     *
     * @param inputStream 输入流
     * @param dst         目标文件
     * @param isOverride  是否覆盖存在的文件
     * @return 是否拷贝成功
     * @throws IOException 异常
     */
    public static boolean copyFile(InputStream inputStream, File dst, boolean isOverride) throws IOException {
        if (inputStream == null || dst == null || dst.isDirectory()) {
            return false;
        }
        // 文件存在且不覆盖则退出
        if (dst.exists() && !isOverride) {
            return false;
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(dst);
            int readLen = 0;
            byte[] buf = new byte[1024];
            while ((readLen = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, readLen);
            }
            outputStream.flush();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
            }
        }
        return true;
    }

    /**
     * 单个文件拷贝
     *
     * @param src        源文件
     * @param dst        目标文件
     * @param isOverride 是否覆盖存在的文件
     * @return 是否拷贝成功
     * @throws IOException 异常
     */
    public static boolean copyFile(String src, String dst, boolean isOverride) throws IOException {
        return copyFile(new File(src), new File(dst), isOverride);
    }

    // ===========copy folder to storage=================

    /**
     * 文件夹拷贝
     *
     * @param srcDir     源文件夹
     * @param destDir    目标文件夹
     * @param autoCreate 自动创建目录
     * @param isOverride 是否覆盖存在的文件
     * @return 是否拷贝成功
     * @throws IOException 异常
     */
    public static boolean copyFolder(File srcDir, File destDir, boolean autoCreate, boolean isOverride) throws IOException {

        if (srcDir == null || destDir == null || !srcDir.exists()) {
            return false;
        }
        if (srcDir.isFile() || destDir.isFile()) {
            return false;// 判断是否是目录
        }

        if (!destDir.exists()) {
            if (autoCreate) {
                destDir.mkdirs();
            } else {
                return false;// 判断目标目录是否存在
            }
        }

        File[] srcFiles = srcDir.listFiles();
        int len = srcFiles.length;
        for (int i = 0; i < len; i++) {
            if (srcFiles[i].isFile()) {
                // 获得目标文件
                File destFile = new File(destDir.getPath() + "//" + srcFiles[i].getName());
                copyFile(srcFiles[i], destFile, isOverride);
            } else if (srcFiles[i].isDirectory()) {
                File theDestDir = new File(destDir.getPath() + "//" + srcFiles[i].getName());
                copyFolder(srcFiles[i], theDestDir, autoCreate, isOverride);
            }
        }
        return true;
    }

    /**
     * 文件夹拷贝
     *
     * @param srcDir     源文件夹
     * @param destDir    目标文件夹
     * @param autoCreate 自动创建目录
     * @param isOverride 是否覆盖存在的文件
     * @return 是否拷贝成功
     * @throws IOException 异常
     */
    public static boolean copyFolder(String srcDir, String destDir, boolean autoCreate, boolean isOverride) throws IOException {
        return copyFolder(new File(srcDir), new File(destDir), autoCreate, isOverride);
    }

    // ===========move single file to storage=================

    /**
     * 移动文件
     *
     * @param src        源文件
     * @param dst        目标文件
     * @param isOverride 是否覆盖存在的文件
     * @return 是否成功
     */
    public static boolean moveFile(File src, File dst, boolean isOverride) {
        boolean isSuccess = false;
        try {
            isSuccess = copyFile(src, dst, isOverride);
            if (isSuccess) {
                deleteFile(src);
            }
        } catch (IOException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    /**
     * 移动文件
     *
     * @param src        源文件
     * @param dst        目标文件
     * @param isOverride 是否覆盖存在的文件
     * @return 是否成功
     */
    public static boolean moveFile(String src, String dst, boolean isOverride) {
        return moveFile(new File(src), new File(dst), isOverride);
    }

    // ===========move folder to storage=================

    /**
     * 移动文件夹.
     *
     * @param srcDir     源文件夹
     * @param destDir    目标文件夹
     * @param autoCreate 自动创建目录
     * @param isOverride 是否覆盖存在的文件
     * @return 是否移动成功
     */
    public static boolean moveFolder(File srcDir, File destDir, boolean autoCreate, boolean isOverride) {

        boolean isSuccess = false;
        try {
            isSuccess = copyFolder(srcDir, destDir, autoCreate, isOverride);
            if (isSuccess) {
                deleteFolder(srcDir);
            }
        } catch (IOException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    /**
     * 移动文件夹.
     *
     * @param srcDir     源文件夹
     * @param destDir    目标文件夹
     * @param autoCreate 自动创建目录
     * @param isOverride 是否覆盖存在的文件
     * @return 是否移动成功
     */
    public static boolean moveFolder(String srcDir, String destDir, boolean autoCreate, boolean isOverride) {
        return moveFolder(new File(srcDir), new File(destDir), autoCreate, isOverride);
    }

    // ===========get private directory=================

    /**
     * 获取私有目录
     *
     * @param ctx 上下文
     * @return 私有目录
     */
    public static File getPrivateDir(Context ctx) {
        return ctx.getFilesDir();
    }

    /**
     * 获取文件大小
     *
     * @param file 文件
     * @return 文件大小
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file != null && file.isFile() && file.exists()) {
            size = file.length();
        }
        return size;
    }

    /**
     * 获取文件夹大小
     *
     * @param dir 文件夹
     * @return 文件夹大小
     */
    public static long getDirSize(File dir) {
        long size = 0;
        if (dir != null && dir.isDirectory()) {
            File flist[] = dir.listFiles();
            if (flist != null && flist.length > 0) {
                for (int i = 0; i < flist.length; i++) {
                    if (flist[i].isDirectory()) {
                        size += getDirSize(flist[i]);
                    } else {
                        size += getFileSize(flist[i]);
                    }
                }
            }
        }
        return size;
    }

    /**
     * 输入流拷贝到输出流
     *
     * @param is 输入流
     * @param os 输出流
     */
    public static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            int count = -1;
            do {
                count = is.read(bytes, 0, buffer_size);
                if (count > 0) {
                    os.write(bytes, 0, count);
                }
            } while (count > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 格式化单位
     *
     * @param size 大小（Byte单位）
     * @return 格式化后大小
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + " Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " TB";
    }

    /**
     * 格式化url，去除？后参数
     *
     * @param url 要格式化的URL
     * @return 格式化的url
     */

    public static String formatUrl(String url) {

        String[] string = url.split("\\?");
        if (string.length > 0) {
            return string[0];
        }
        return url;
    }

    /**
     * 从URL中获取文件名
     * <pre>
     *     1，如果url为空，则随机通过uuid进行md5计算生成
     *     2，如果url以/结尾，则通过对url进行md5计算生成
     *     3，取最后一个/后面的部分作为文件名
     * </pre>
     *
     * @param url url地址
     * @return 获取的文件名
     */
    public static String getNameFromUrl(final String url) {
        if (StringUtils.isNotEmpty(url)) {
            String _url = url.trim();
            if (_url.endsWith("/")) {
                return MD5Utils.getMD5Str(_url);
            } else {
                return _url.substring(_url.lastIndexOf("/") + 1);
            }
        }
        return MD5Utils.getMD5Str(UUIDUtils.getUUID());
    }

    /**
     * 判断路径是否存在
     *
     * @param path 路径
     * @return 是否存在
     */
    public static boolean exists(String path) {
        if (!isFilePath(path)) {
            return false;
        }
        File file = getAbsoluteFile(path);
        return file != null && file.exists();
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath the check file path
     * @return true means exist
     */
    public static boolean isFileExist(String filePath) {

        if (!isFilePath(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return file != null && file.exists() && file.isFile();
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath the check file path
     * @return true means exist
     */
    public static boolean isFolderExist(String filePath) {

        if (!isFilePath(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return file != null && file.exists() && file.isDirectory();
    }

    /**
     * 格式化文件大小显示，转换为Byte、KB、MB、GB、TB
     *
     * @param size     文件大小（单位Byte）
     * @param decimals 小数点位数
     * @return 格式化后的文件大小
     */
    public static String formatFileSize(Long size, int decimals) {
        if (decimals < 0) {
            decimals = 2;
        }
        String result;
        if (size < UNIT.longValue()) {
            result = new BigDecimal(size).setScale(decimals, BigDecimal.ROUND_HALF_EVEN).toString() + "B";
        } else if (size < UNIT.multiply(UNIT).longValue()) {
            result = covertFileSizeKB(size, decimals).toString() + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            result = covertFileSizeMB(size, decimals).toString() + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024L) {
            result = covertFileSizeGB(size, decimals).toString() + "GB";
        } else {
            result = covertFileSizeTB(size, decimals).toString() + "TB";
        }
        return result;
    }

    /**
     * 格式化文件大小到KB单位
     *
     * @param size     文件大小（单位Byte）
     * @param decimals 小数点位数
     * @return 新单位下的文件大小
     */
    public static BigDecimal covertFileSizeKB(Long size, int decimals) {
        if (decimals < 0) {
            decimals = 2;
        }
        return new BigDecimal(size).divide(UNIT, decimals, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 格式化文件大小到MB单位
     *
     * @param size     文件大小（单位Byte）
     * @param decimals 小数点位数
     * @return 新单位下的文件大小
     */
    public static BigDecimal covertFileSizeMB(Long size, int decimals) {
        if (decimals < 0) {
            decimals = 2;
        }
        return covertFileSizeKB(size, decimals).divide(UNIT, decimals, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 格式化文件大小到GB单位
     *
     * @param size     文件大小（单位Byte）
     * @param decimals 小数点位数
     * @return 新单位下的文件大小
     */
    public static BigDecimal covertFileSizeGB(Long size, int decimals) {
        if (decimals < 0) {
            decimals = 2;
        }
        return covertFileSizeMB(size, decimals).divide(UNIT, decimals, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 格式化文件大小到TB单位
     *
     * @param size     文件大小（单位Byte）
     * @param decimals 小数点位数
     * @return 新单位下的文件大小
     */
    public static BigDecimal covertFileSizeTB(Long size, int decimals) {
        if (decimals < 0) {
            decimals = 2;
        }
        return covertFileSizeGB(size, decimals).divide(UNIT, decimals, BigDecimal.ROUND_HALF_EVEN);
    }


    /**
     * 创建父目录
     *
     * @param filePath 文件路径
     * @return 是否成功
     */
    public static boolean createFileParentDir(String filePath) {
        File file = new File(filePath);
        if (file != null) {
            if (file.exists()) {
                return true;// parent dir exist
            } else {
                File parentFile = file.getParentFile();
                if (parentFile != null) {
                    if (parentFile.exists()) {
                        return true;// parent dir exist
                    } else {
                        return parentFile.mkdirs();// create parent dir
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取文件后缀名
     *
     * @param filePath 文件路径
     * @return 后缀名
     */
    public static String getFileSuffix(String filePath) {
        if (StringUtils.isNotEmpty(filePath)) {
            int start = filePath.lastIndexOf(".");
            if (start != -1) {
                return filePath.substring(start + 1);
            }
        }
        return null;
    }

    /**
     * 判断字符串是否为文件路径
     *
     * @param path file path
     * @return true means the path is file path
     */
    public static boolean isFilePath(String path) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        if (path.startsWith(File.separator)) {
            return true;
        }
        return false;
    }

    /**
     * 文件路径是否可写
     *
     * @param path 文件路径
     * @return 是否可写
     */
    public static boolean canWrite(String path) {
        // if sdcard,needs the permission:  android.permission.WRITE_EXTERNAL_STORAGE
        if (isSDCardPath(path)) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * 判断文件路径是否为SD卡路径
     *
     * @param path 文件路径
     * @return 是否SD卡
     */
    public static boolean isSDCardPath(String path) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        String sdRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (path.startsWith(sdRootPath)) {
            return true;
        }
        return false;
    }

    /**
     * 获取可用空间
     *
     * @param fileDirPath 目录路径
     * @return 可用空间，-1表示失败
     */
    public static long getAvailableSpace(String fileDirPath) {
        try {
            File file = new File(fileDirPath);
            if (!file.exists()) {
                file.mkdirs();// create to make sure it is not error below
            }
            final StatFs stats = new StatFs(fileDirPath);
            long result = (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 文件过滤器
     */
    public static class FileFilter implements FilenameFilter {

        private List<String> extensions = new ArrayList<>();

        public FileFilter(String... extensions) {
            for (String extension : extensions) {
                if (extension != null) {
                    this.extensions.add(extension);
                }
            }
        }

        @Override
        public boolean accept(File dir, String filename) {
            boolean accept = false;
            if (filename != null && filename.trim().length() > 0) {
                for (String extension : extensions) {
                    if (filename.endsWith(extension)) {
                        accept = true;
                        break;
                    }
                }
            }
            return accept;
        }
    }
}
