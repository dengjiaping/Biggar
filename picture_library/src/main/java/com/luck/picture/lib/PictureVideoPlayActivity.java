package com.luck.picture.lib;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.AttrsUtils;
import com.luck.picture.lib.tools.LightStatusBarUtils;
import com.luck.picture.lib.tools.ToolbarUtil;

import java.io.File;
import java.io.Serializable;

public class PictureVideoPlayActivity extends PictureBaseActivity implements View.OnClickListener {
    private ImageView picture_left_back;
    private ImageView iv_play;
    private TextView tv_finish;
    private ImageView preview;
    private LocalMedia video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_activity_video_play);
        int status_color = AttrsUtils.getTypeValueColor(this, R.attr.picture_status_color);
        ToolbarUtil.setColorNoTranslucent(this, status_color);
        LightStatusBarUtils.setLightStatusBar(this, previewStatusFont);
        video = (LocalMedia) getIntent().getSerializableExtra("video");
        picture_left_back = (ImageView) findViewById(R.id.picture_left_back);
        tv_finish = (TextView) findViewById(R.id.tv_finish);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        picture_left_back.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
        preview = (ImageView) findViewById(R.id.video_view);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.image_placeholder);
        Glide.with(this)
                .asBitmap()
                .apply(options)
                .load(video.getPath()).into(preview);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.picture_left_back) {
            finish();
        } else if (id == R.id.iv_play) {

            Intent it = new Intent(Intent.ACTION_VIEW);
            Uri uri;
            if (Build.VERSION.SDK_INT >= 24) {
                uri = FileProvider.getUriForFile(this, "cn.biggar.biggar.provider", new File(video.getPath()));
                it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                String bpath = "file://" + video.getPath();
                uri = Uri.parse(bpath);
            }
            it.setDataAndType(uri, "video/avi");
            startActivity(it);
        } else if (id == R.id.tv_finish) {
            setResult(RESULT_OK, new Intent().putExtra("video", video));
            finish();
        }
    }
}
