package com.lcorekit.l.live.utils;

import android.text.format.Formatter;

import com.lcorekit.l.live.App;

import java.io.File;
import java.io.FileInputStream;

/**
 * Author：Koterwong，Data：2016/5/4.
 * Description:
 */
public class FileUtils {

    /**
     * 获取缓存目录
     */
    public static String getCacheFilePath() {
        StringBuilder builder = new StringBuilder();
        builder
                .append(App.getCotext().getCacheDir().getAbsolutePath())
                .append(File.separator)
                .append(App.CACHE_NAME)
                .append(File.separator);
        return builder.toString();
    }

    public static String getCacheSize() {
        return getAutoFileOrFilesSize(getCacheFilePath());
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Formatter.formatFileSize(App.getCotext(), blockSize);
    }

    /**
     * 遍历文件目录，获取目录下总大小
     *
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File files[] = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                size = size + getFileSizes(files[i]);
            } else {
                size = size + getFileSize(files[i]);
            }
        }
        return size;
    }

    /**
     * 获取指定文件大小
     *
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
        }
        return size;
    }
}
