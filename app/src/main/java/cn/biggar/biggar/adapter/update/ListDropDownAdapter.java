package cn.biggar.biggar.adapter.update;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.biggar.biggar.R;

/**
 * Created by Chenwy on 2017/5/31.
 */

public class ListDropDownAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ListDropDownAdapter(List<String> data) {
        super(R.layout.item_drop, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_tag_name, item);
    }
}
