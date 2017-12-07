package cn.biggar.biggar.adapter.update;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.biggar.biggar.R;
import cn.biggar.biggar.bean.update.DistriEarningsBean;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.utils.Utils;

/**
 * Created by Chenwy on 2017/7/18.
 */

public class DistriEarningAdapter extends BaseQuickAdapter<DistriEarningsBean.DistriEarnings, BaseViewHolder> {
    private String uid;
    public DistriEarningAdapter(String uid, List<DistriEarningsBean.DistriEarnings> data) {
        super(R.layout.item_distri_earnings, data);
        this.uid = uid;
    }

    @Override
    protected void convert(BaseViewHolder helper, DistriEarningsBean.DistriEarnings item) {
        LinearLayout llRootView = helper.getView(R.id.ll_root_view);
        if (helper.getLayoutPosition() < getData().size() - 1) {
            llRootView.setPadding(Utils.dip2px(mContext, 16), 0, 0, Utils.dip2px(mContext, 15));
        } else {
            llRootView.setPadding(Utils.dip2px(mContext, 16), 0, 0, 0);
        }

        TextView tvUserName = helper.getView(R.id.tv_username);
        if (uid != null && uid.equals(item.E_MembID)){
            tvUserName.setText("我");
        }else {
            tvUserName.setText(item.E_UserName);
        }

        helper.setText(R.id.tv_goods_name,item.E_Name)
                .setText(R.id.tv_rate_price,"￥"+item.E_RatePrice3)
                .setText(R.id.tv_num,"X"+item.E_Nums);

        final ImageView ivImage = helper.getView(R.id.iv_image);
        Glide.with(mContext).asBitmap().load(Utils.getRealUrl(item.E_Img1))
                .apply(new RequestOptions().centerCrop())
                .into(new BitmapImageViewTarget(ivImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.
                                        create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(Utils.dip2px(mContext, 10));
                        ivImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }
}
