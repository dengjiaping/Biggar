package cn.biggar.biggar.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;

import cn.biggar.biggar.app.Constants;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import per.sue.gear2.utils.StorageUtils;


/**
 * Created by mx on 2016/10/25.
 * 缓存管理
 */
public class StorageManager {

    private static final String TAG = "StorageManager";


    private final String SLPASH_FILE_NAME = "bg_slpash_topic.jpg";
    private final String TMP_MUSIC_PATH = "/music";


    private static StorageManager ourInstance = new StorageManager();

    public static StorageManager getInstance() {
        return ourInstance;
    }

    private StorageManager() {
    }


    public File getSlpashBGPath(Context context) {
        return new File(StorageUtils.getCacheDirectory(context), SLPASH_FILE_NAME);
    }


    private String getFileByUrl(String url) {
        return url.substring(url.lastIndexOf("/"));
    }

    /**
     * 获取 对应 子目录的  绝对路径
     *
     * @param context
     * @param name
     * @return
     */
    private String getAbsolutePathByName(Context context, String name) {
        String path = context.getExternalCacheDir().getAbsolutePath() + name;
        String storageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(storageState)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + name;
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * 创建 二维码图片缓存文件
     *
     * @param context
     * @return
     */
    public File createQrCodeImgFile(Context context) {
        File file = new File(getAbsolutePathByName(context, Constants.TMP_QRCODE_IMAGE_PATH));
        File ImageFile = new File(file, new StringBuilder(System.currentTimeMillis() + "").append(".jpg").toString());
        return ImageFile;
    }

    /**
     * 创建 个人相册文件
     *
     * @param context
     * @return
     */
    public File createAlbumImgFile(Context context) {
        File file = new File(getAbsolutePathByName(context, Constants.TMP_PHOTO_ALBUM_IMAGE_PATH));
        File ImageFile = new File(file, new StringBuilder(System.currentTimeMillis() + "").append(".jpg").toString());
        return ImageFile;
    }

    /**
     * 创建 图片缓存文件
     *
     * @param context
     * @return
     */
    public File createImgFile(Context context) {
        return createImgFile(context, null);
    }

    /**
     * 创建 图片缓存文件
     *
     * @param context
     * @return
     */
    public File createImgFile(Context context, String name) {
        File file = new File(getAbsolutePathByName(context, Constants.TMP_IMAGE_PATH));
        File ImageFile = new File(file, new StringBuilder(TextUtils.isEmpty(name) ? System.currentTimeMillis() + "" : name).append(".jpg").toString());
        return ImageFile;
    }


    /**
     * 获取音乐存储目录
     *
     * @param context
     * @return
     */
    public String getMusicPath(Context context) {
        String path = context.getExternalCacheDir().getAbsolutePath() + TMP_MUSIC_PATH;
        String storageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(storageState)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + TMP_MUSIC_PATH;
        }

        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        return path;
    }


    /**
     * 删除 缓存图片
     *
     * @param context
     */
    public boolean delImageCache(Context context) {
        File file = new File(getAbsolutePathByName(context, Constants.TMP_IMAGE_PATH));
        return FileUtils.deleteFile(file);
    }

    /**
     * 获取图片缓存大小
     *
     * @return
     */
    public String getImageCacheSize(Context context) {
        long defaultSize = FileUtils.calculateFolderSize(new File(getAbsolutePathByName(context, Constants.TMP_IMAGE_PATH)));
        long glideCacheSize = getGlideCacheSize(context);
        long allSize = defaultSize + glideCacheSize;
        return getFormatSize(allSize);
    }

    public long getGlideCacheSize(Context context) {
        try {
            return getFolderSize(new File(context.getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    private long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte==0){
            return "0.0MB";
        }
//        if (kiloByte < 1) {
//            return size + "Byte";
//        }

        double megaByte = kiloByte / 1024;
//        if (megaByte < 1) {
//            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
//            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
//        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }
}
