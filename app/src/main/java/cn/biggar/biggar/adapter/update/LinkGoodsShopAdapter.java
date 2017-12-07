package cn.biggar.biggar.adapter.update;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.bean.BrandBean;
import cn.biggar.biggar.utils.Utils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Chenwy on 2017/6/6.
 */

public class LinkGoodsShopAdapter extends BaseQuickAdapter<BrandBean, BaseViewHolder> {
    private boolean isShowSell;

    public LinkGoodsShopAdapter(boolean isShowSell, List<BrandBean> data) {
        super(R.layout.item_link_shop, data);
        this.isShowSell = isShowSell;
    }

    @Override
    protected void convert(BaseViewHolder helper, BrandBean item) {
        ImageView logo = helper.getView(R.id.iv_logo);
        Glide.with(mContext)
                .load(Utils.getRealUrl(item.getE_Logo()))
                .apply(new RequestOptions().centerCrop())
                .into(logo);
        helper.setText(R.id.tv_brand_name, item.getE_BrandCnName());
        helper.setGone(R.id.right_line, helper.getLayoutPosition() % 2 == 0).setGone(R.id.ll_can_sell, isShowSell);
        if (isShowSell) {
            helper.setText(R.id.tv_num, item.getE_Num() + "件");
        }
    }
}
