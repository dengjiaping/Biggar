package cn.biggar.biggar.adapter.update;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.biggar.biggar.R;

/**
 * Created by Chenwy on 2017/6/9.
 */

public class PictureDetailAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public PictureDetailAdapter(@Nullable List<String> data) {
        super(R.layout.item_picture_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        RequestOptions options = new RequestOptions();
        options.skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.gear_image_default);
        Glide.with(mContext).load(item)
                .apply(options)
                .into((ImageView) helper.getView(R.id.iv_image));
    }
}
