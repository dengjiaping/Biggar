package cn.biggar.biggar.adapter.update;

import android.support.annotation.Nullable;

import cn.biggar.biggar.R;
import cn.biggar.biggar.bean.update.XbBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Chenwy on 2017/6/1.
 */

public class XbAdapter extends BaseQuickAdapter<XbBean, BaseViewHolder> {
    private String type;
    public XbAdapter(@Nullable List<XbBean> data,String type) {
        super(R.layout.item_xingbi, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, XbBean item) {
        helper.setText(R.id.tv_name, item.E_AddMess)
                .setText(R.id.tv_time, item.E_CreateDate);
        if (type.equals("xb")){
            String ePoints = item.E_Points;
            float v = Float.parseFloat(ePoints);
            if (v > 0f) {
                helper.setText(R.id.tv_num, "+" + ePoints);
            } else {
                helper.setText(R.id.tv_num, ePoints);
            }
        }else if (type.equals("xz")){
            String e_pointVal = item.E_PointVal;
            helper.setText(R.id.tv_num, "+" + e_pointVal);
        }
    }
}
