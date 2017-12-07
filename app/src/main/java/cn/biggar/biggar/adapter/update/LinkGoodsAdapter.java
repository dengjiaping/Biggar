package cn.biggar.biggar.adapter.update;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.ImageView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.bean.update.DistriGoodsBean;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Chenwy on 2017/6/7.
 */

public class LinkGoodsAdapter extends BaseQuickAdapter<DistriGoodsBean, BaseViewHolder> {

    public LinkGoodsAdapter(List<DistriGoodsBean> data) {
        super(R.layout.item_link_goods, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DistriGoodsBean item) {
        String eRate3 = TextUtils.isEmpty(item.E_Rate3) ? "0" : item.E_Rate3;
        helper.setText(R.id.tv_goods_name, item.E_Name)
                .setText(R.id.tv_sell_price, "￥" + item.E_SellPrice)
                .setText(R.id.tv_rate, eRate3 + "%")
                .setImageResource(R.id.iv_check, item.isCheck ? R.mipmap.checked_3x : R.mipmap.unchecked_3x);

        helper.setGone(R.id.ll_rate, !eRate3.equals("0"));
        ImageView ivGoodsImage = helper.getView(R.id.iv_goods_image);
        Glide.with(mContext).load(item.E_Img1).apply(new RequestOptions().centerCrop()).into(ivGoodsImage);
    }
}
