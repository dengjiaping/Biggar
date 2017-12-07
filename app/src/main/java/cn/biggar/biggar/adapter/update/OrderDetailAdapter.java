package cn.biggar.biggar.adapter.update;


import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.biggar.biggar.R;
import cn.biggar.biggar.bean.update.OrderDetailGoodsBean;

/**
 * Created by Chenwy on 2017/11/21.
 */

public class OrderDetailAdapter extends BaseQuickAdapter<OrderDetailGoodsBean, BaseViewHolder> {
    private boolean isDefault;

    public OrderDetailAdapter(boolean isDefault, List<OrderDetailGoodsBean> data) {
        super(R.layout.item_order_detail, data);
        this.isDefault = isDefault;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDetailGoodsBean item) {
        helper.setVisible(R.id.line, helper.getLayoutPosition() > 1)
                .setTextColor(R.id.tv_price, isDefault
                        ? ContextCompat.getColor(mContext, R.color.colorPrimary)
                        : ContextCompat.getColor(mContext, R.color.app_3));
    }
}
