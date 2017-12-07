package cn.biggar.biggar.adapter.update;

import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.bean.update.MoneyBean;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Chenwy on 2017/6/6.
 */

public class MoneyAdapter extends BaseQuickAdapter<MoneyBean.Detail, BaseViewHolder> {
    private String uid;

    public MoneyAdapter(List<MoneyBean.Detail> data, String uid) {
        super(R.layout.item_lq, data);
        this.uid = uid;
    }

    @Override
    protected void convert(BaseViewHolder helper, MoneyBean.Detail item) {
        String name = uid.equals(item.user_id) ? "您" : item.E_Name;
        String num = item.num;
        String goods = item.E_Title;
        SpannableString spannableString = new SpannableString(name + "卖出了" + num + "件" + goods + "，您成功获得了佣金");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorPrimary));
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorPrimary));
        spannableString.setSpan(colorSpan, 0, name.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan2, name.length() + 3, name.length() + num.length() + 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        helper.setText(R.id.tv_msg, spannableString)
                .setText(R.id.tv_date, item.date)
                .setText(R.id.tv_money, "+" + item.E_Price);
    }
}
