package cn.biggar.biggar.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

    /**
     * 保存路径的文件夹名称
     */
    public static  String DIR_NAME = "biggar_video";

    /**
     * 给指定的文件名按照时间命名
     */
    private static  SimpleDateFormat OUTGOING_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    /**
     * 得到指定的Video保存路径
     * @return
     */
    public static File getDoneVideoPath() {
        File dir = new File(Environment.getExternalStorageDirectory()
                + File.separator + DIR_NAME);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    /**
     * 得到输出的Video保存路径
     * @return
     */
    public static String newOutgoingVideoFilePath() {
        String str = OUTGOING_DATE_FORMAT.format(new Date());
        String fileName = getDoneVideoPath() + File.separator
                + "video_" + str + ".mp4";
        return fileName;
    }

    /**
     * 获取视频封面地址
     * @return
     */
    public static String newOutgoingCoverFilePath() {
        String str = OUTGOING_DATE_FORMAT.format(new Date());
        String fileName = getDoneVideoPath() + File.separator
                + "cover_s" + str + ".png";
        return fileName;
    }

    /**
     * 获取视频缩略图
     * @return
     */
    public static String newOutgoingThumFilePath() {
        String str = OUTGOING_DATE_FORMAT.format(new Date());
        String fileName = getDoneVideoPath() + File.separator
                + "first_s" + str + ".png";
        return fileName;
    }


    /**
     * 复制文件
     * @param sourcePath
     * @param destPath
     * @return
     */
    public static boolean  copyFileByChannels(String sourcePath, String destPath)  {
        boolean isSuccess = true;
        File source = new File(sourcePath);
        File dest = new File(destPath);
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {

            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            inputChannel.close();
            outputChannel.close();

        } catch (IOException e) {
            isSuccess = false;
      }
        return isSuccess;

    }
    /**

     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)

     */

    public static int dip2px(Context mContext, float dpValue) {

        final float scale = mContext.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);

    }




    /***
     * 清空指定文件�?文件
     * @return 清空成功的话返回true, 否则返回false
     */

    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            File[] childs = file.listFiles();
            if (childs == null || childs.length <= 0) {
                // 空文件夹删掉
                return file.delete();
            } else {
                // 非空，遍历删除子文件
                for (int i = 0; i < childs.length; i++) {
                    deleteFile(childs[i]);
                }
                return deleteFile(file);
            }
        } else {
            return file.delete();
        }
    }
    /***
     * 计算文件夹大�?
     * @param mFile 目录或文�?
     * @return 文件或目录的大小
     */
    public static long calculateFolderSize(File mFile) {
        // 判断文件是否存在
        if (!mFile.exists()) {
            return 0;
        }

        // 如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大�?
        if (mFile.isDirectory()) {
            File[] files = mFile.listFiles();
            long size = 0;
            for (File f : files) {
                size += calculateFolderSize(f);
            }
            return size;
        } else {
            return mFile.length();
        }
    }


    /**
     * 指定文件 是否存在
     * @param path
     * @return
     */
    public static boolean existsFile(String path){
        if (!TextUtils.isEmpty(path)){
            File file=new File(path);
            if (file.exists()){
                return true;
            }
        }
        return false;
    }
}
