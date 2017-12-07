package com.luck.picture.lib.model;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import com.luck.picture.lib.R;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import com.luck.picture.lib.tools.DebugUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * author：luck
 * project：LocalMediaLoader
 * package：com.luck.picture.ui
 * email：893855882@qq.com
 * data：16/12/31
 */

public class LocalMediaLoader {
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    private static final String DURATION = "duration";
    private static final int AUDIO_DURATION = 500;// 过滤掉小于500毫秒的录音
    private int type = PictureConfig.TYPE_IMAGE;
    private FragmentActivity activity;
    //    private boolean isGif;
    private long videoS = 0;

    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.SIZE,
            DURATION,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.WIDTH,
            MediaStore.Files.FileColumns.HEIGHT,
    };

    private static final String ORDER_BY = MediaStore.Files.FileColumns._ID + " DESC";

    public LocalMediaLoader(FragmentActivity activity, int type, boolean isGif, long videoS) {
        this.activity = activity;
        this.type = type;
//        this.isGif = isGif;
        this.videoS = videoS;
    }

    public void loadAllMedia(final LocalMediaLoadListener imageLoadListener) {
        activity.getSupportLoaderManager().initLoader(type, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                        CursorLoader cursorLoader = null;
                        String selection;
                        String[] selectionArgs;
                        switch (id) {
                            case PictureConfig.TYPE_ALL:
                                String condition = videoS > 0 ? DURATION + " <= " + videoS + " and "
                                        + DURATION + "> 0" :
                                        DURATION + "> 0";

                                selection =
                                        "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                                                + " OR "
                                                + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?" +
                                                " and " + condition + ")";

                                selectionArgs = new String[]{
                                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
                                };

                                cursorLoader = new CursorLoader(
                                        activity,
                                        QUERY_URI,
                                        PROJECTION,
                                        selection,
                                        selectionArgs,
                                        ORDER_BY);
                                break;
                            case PictureConfig.TYPE_IMAGE:
                                selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?";
                                selectionArgs = new String[]{
                                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                                };
                                cursorLoader = new CursorLoader(
                                        activity,
                                        QUERY_URI,
                                        PROJECTION,
                                        selection,
                                        selectionArgs,
                                        ORDER_BY);
                                break;
                            case PictureConfig.TYPE_VIDEO:
                                selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?";
                                selectionArgs = new String[]{
                                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
                                };
                                cursorLoader = new CursorLoader(
                                        activity,
                                        QUERY_URI,
                                        PROJECTION,
                                        selection,
                                        selectionArgs,
                                        ORDER_BY);
                                break;
                            case PictureConfig.TYPE_AUDIO:
                                selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?";
                                selectionArgs = new String[]{
                                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO),
                                };
                                cursorLoader = new CursorLoader(
                                        activity,
                                        QUERY_URI,
                                        PROJECTION,
                                        selection,
                                        selectionArgs,
                                        ORDER_BY);
                                break;
                        }
                        return cursorLoader;
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                        try {
                            List<LocalMediaFolder> imageFolders = new ArrayList<LocalMediaFolder>();
                            LocalMediaFolder allImageFolder = new LocalMediaFolder();
                            List<LocalMedia> latelyImages = new ArrayList<>();
                            if (data != null) {
                                int count = data.getCount();
                                if (count > 0) {
                                    data.moveToFirst();
                                    do {
                                        String path = data.getString
                                                (data.getColumnIndexOrThrow(PROJECTION[1]));
                                        // 如原图路径不存在或者路径存在但文件不存在,就结束当前循环
                                        if (TextUtils.isEmpty(path) || !new File(path).exists()
                                                ) {
                                            continue;
                                        }
                                        String pictureType = data.getString
                                                (data.getColumnIndexOrThrow(PROJECTION[6]));
                                        boolean eqImg = pictureType.startsWith(PictureConfig.IMAGE);
                                        int duration = eqImg ? 0 : data.getInt
                                                (data.getColumnIndexOrThrow(PROJECTION[5]));
                                        int w = eqImg ? data.getInt
                                                (data.getColumnIndexOrThrow(PROJECTION[7])) : 0;
                                        int h = eqImg ? data.getInt
                                                (data.getColumnIndexOrThrow(PROJECTION[8])) : 0;
                                        DebugUtil.i("media mime type:", pictureType);
                                        LocalMedia image = new LocalMedia
                                                (path, duration, type, pictureType, w, h);
                                        LocalMediaFolder folder = getImageFolder(path, imageFolders);
                                        List<LocalMedia> images = folder.getImages();
                                        images.add(image);
                                        folder.setImageNum(folder.getImageNum() + 1);
                                        latelyImages.add(image);
                                        int imageNum = allImageFolder.getImageNum();
                                        allImageFolder.setImageNum(imageNum + 1);
                                    } while (data.moveToNext());

                                    if (latelyImages.size() > 0) {
                                        sortFolder(imageFolders);
                                        imageFolders.add(0, allImageFolder);
                                        allImageFolder.setFirstImagePath
                                                (latelyImages.get(0).getPath());
                                        String title = type == PictureMimeType.ofAudio() ?
                                                activity.getString(R.string.picture_all_audio)
                                                : activity.getString(R.string.picture_camera_roll);
                                        allImageFolder.setName(title);
                                        allImageFolder.setImages(latelyImages);
                                    }
                                    imageLoadListener.loadComplete(imageFolders);
                                } else {
                                    // 如果没有相册
                                    imageLoadListener.loadComplete(imageFolders);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> loader) {
                    }
                });
    }

    private void sortFolder(List<LocalMediaFolder> imageFolders) {
        // 文件夹按图片数量排序
        Collections.sort(imageFolders, new Comparator<LocalMediaFolder>() {
            @Override
            public int compare(LocalMediaFolder lhs, LocalMediaFolder rhs) {
                if (lhs.getImages() == null || rhs.getImages() == null) {
                    return 0;
                }
                int lsize = lhs.getImageNum();
                int rsize = rhs.getImageNum();
                return lsize == rsize ? 0 : (lsize < rsize ? 1 : -1);
            }
        });
    }

    private LocalMediaFolder getImageFolder(String path, List<LocalMediaFolder> imageFolders) {
        File imageFile = new File(path);
        File folderFile = imageFile.getParentFile();

        for (LocalMediaFolder folder : imageFolders) {
            if (folder.getName().equals(folderFile.getName())) {
                return folder;
            }
        }
        LocalMediaFolder newFolder = new LocalMediaFolder();
        newFolder.setName(folderFile.getName());
        newFolder.setPath(folderFile.getAbsolutePath());
        newFolder.setFirstImagePath(path);
        imageFolders.add(newFolder);
        return newFolder;
    }

    public interface LocalMediaLoadListener {
        void loadComplete(List<LocalMediaFolder> folders);
    }
}