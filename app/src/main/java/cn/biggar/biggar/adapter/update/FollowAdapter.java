package cn.biggar.biggar.adapter.update;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.bean.ConcersBean;
import cn.biggar.biggar.utils.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Chenwy on 2017/5/26.
 */

public class FollowAdapter extends BaseQuickAdapter<ConcersBean, BaseViewHolder> {
    private boolean isShowPlus = false;

    public FollowAdapter(@LayoutRes int layoutResId, @Nullable List<ConcersBean> data, boolean isShowPlus) {
        super(layoutResId, data);
        this.isShowPlus = isShowPlus;
    }

    @Override
    protected void convert(BaseViewHolder helper, ConcersBean item) {
        helper.setGone(R.id.rl_plus, isShowPlus);
        helper.setText(R.id.play_username_tv, item.getE_CreateUser());
        helper.setText(R.id.play_browsenumber_tv, item.getE_Plays());
        helper.setText(R.id.play_title, item.getE_Name());
        helper.addOnClickListener(R.id.play_user_iv).addOnClickListener(R.id.iv_plus);

        RequestOptions options = new RequestOptions();
        options.centerCrop()
                .placeholder(R.drawable.gear_image_default)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(mContext).asBitmap().load(Utils.getRealUrl(item.getE_Img1()))
                .apply(options)
                .into((ImageView) helper.getView(R.id.video_conver_iv));

        Glide.with(mContext).asBitmap().load(Utils.getRealUrl(item.getE_HeadImg()))
                .apply(options)
                .into((CircleImageView) helper.getView(R.id.play_user_iv));
    }
}
