package cn.biggar.biggar.adapter.update;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.biggar.biggar.R;
import cn.biggar.biggar.bean.update.DistriManagerBean;
import cn.biggar.biggar.utils.Utils;

/**
 * Created by Chenwy on 2017/7/17.
 */

public class DistriManagerAdapter extends BaseQuickAdapter<DistriManagerBean, BaseViewHolder> {
    public DistriManagerAdapter(List<DistriManagerBean> data) {
        super(R.layout.item_distri_manager, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DistriManagerBean item) {
        helper.setText(R.id.tv_title, helper.getLayoutPosition() == 0 ? "我的收益" : "成员列表")
                .setText(R.id.tv_tips, helper.getLayoutPosition() == 0 ? "今日" : "（人）")
                .setText(R.id.tv_content, helper.getLayoutPosition() == 0 ? "￥" + item.E_Money : item.E_Num);
        if (helper.getLayoutPosition() == 0) {
            RelativeLayout rlContainer = helper.getView(R.id.rl_container);
            rlContainer.setPadding(0, Utils.dip2px(mContext, 6), 0, 0);
        }
    }
}
