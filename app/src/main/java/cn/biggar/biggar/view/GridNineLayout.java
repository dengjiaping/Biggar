package cn.biggar.biggar.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.ImageViewerActivity;
import cn.biggar.biggar.activity.update.ContentDetailActivity;
import cn.biggar.biggar.bean.VideoImageBean;
import cn.biggar.biggar.listener.OnGridNineItemClickListener;
import cn.biggar.biggar.utils.Utils;

/**
 * Created by Chenwy on 2017/11/8.
 */

public class GridNineLayout extends RelativeLayout {
    private Context mContext;
    private RecyclerView rvPicture;
    private RelativeLayout rlVideo;
    private MyAdapter myAdapter;
    private int screenWidth;
    private ImageView ivVideoCover;
    private int videoWidth;
    private int videoHeight;
    private VideoImageBean videoImageBean;

    public GridNineLayout(Context context) {
        super(context);
        init(context);
    }

    public GridNineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GridNineLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.grid_nine_layout, this);
        screenWidth = Utils.getScreenWidth(mContext);
        videoWidth = screenWidth - Utils.dip2px(context, 40 + 12 + 12 + 12);
        videoHeight = (int) (170f * videoWidth / 303f);
        rvPicture = (RecyclerView) findViewById(R.id.rv_picture);
        rlVideo = (RelativeLayout) findViewById(R.id.rl_video);
        ivVideoCover = (ImageView) findViewById(R.id.iv_video_cover);
        myAdapter = new MyAdapter(mContext, new ArrayList<String>(), screenWidth);
        rvPicture.setLayoutManager(new GridLayoutManager(mContext, 3));
        rvPicture.setAdapter(myAdapter);
        rvPicture.setFocusableInTouchMode(false);
        rvPicture.requestFocus();
        rvPicture.addOnItemTouchListener(new OnGridNineItemClickListener(rvPicture) {
            @Override
            public void onItemClick(View view, int position) {
                if (position == -1) {
                    if (videoImageBean != null) {
                        mContext.startActivity(new Intent(mContext, ContentDetailActivity.class)
                                .putExtra("id", videoImageBean.getID())
                                .putExtra("typeVal", videoImageBean.getE_TypeVal()));
                    }
                } else {
                    if (videoImageBean != null
                            && videoImageBean.E_Photo != null
                            && videoImageBean.E_Photo.size() > 0) {
                        mContext.startActivity(ImageViewerActivity.startIntent(mContext, videoImageBean.E_Photo
                                , position, false));
                        ((Activity) mContext).overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    public void setDatas(VideoImageBean videoImageBean) {
        this.videoImageBean = videoImageBean;
        String eTypeVal = videoImageBean.getE_TypeVal();
        //视频
        if (eTypeVal.equals("0")) {
            rlVideo.setVisibility(VISIBLE);
            rvPicture.setVisibility(GONE);
            Glide.with(mContext).load(Utils.getRealUrl(videoImageBean.getE_Img1()))
                    .apply(new RequestOptions()
                            .centerCrop()
                            .skipMemoryCache(false)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(ivVideoCover);
            rlVideo.getLayoutParams().width = videoWidth;
            rlVideo.getLayoutParams().height = videoHeight;
        }
        //图片
        else if (eTypeVal.equals("1")) {
            rlVideo.setVisibility(GONE);
            rvPicture.setVisibility(VISIBLE);
            List<String> ePhoto = videoImageBean.E_Photo;
            if (ePhoto != null && ePhoto.size() > 0) {
                myAdapter.setNewData(ePhoto);
            }
        }
    }


    private static class MyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        private int screenWidth;
        private Context context;
        private int leftWidth;
        private int rightWidth;
        private int itemImageWidthAndHeight;
        private int margin;
        private int rightPadding;

        public MyAdapter(Context context, List<String> data, int screenWidth) {
            super(R.layout.item_grid_picture, data);
            this.screenWidth = screenWidth;
            this.context = context;
            margin = Utils.dip2px(context, 4);
            rightPadding = Utils.dip2px(context, 12);
            leftWidth = Utils.dip2px(context, 40 + 12 + 12);
            rightWidth = screenWidth - leftWidth - rightPadding;
            itemImageWidthAndHeight = (int) (rightWidth / 3f);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            ImageView ivPicture = helper.getView(R.id.iv_picture);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(itemImageWidthAndHeight, itemImageWidthAndHeight);
            int layoutPosition = helper.getLayoutPosition();
            if (layoutPosition == 0 || layoutPosition == 3 || layoutPosition == 6) {
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = 0;
                layoutParams.topMargin = margin;
                layoutParams.bottomMargin = 0;
            } else {
                layoutParams.leftMargin = margin;
                layoutParams.rightMargin = 0;
                layoutParams.topMargin = margin;
                layoutParams.bottomMargin = 0;
            }

            ivPicture.setLayoutParams(layoutParams);
            Glide.with(mContext)
                    .load(Utils.getRealUrl(item))
                    .apply(new RequestOptions()
                            .centerCrop()
                            .skipMemoryCache(false)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(ivPicture);

        }
    }
}
