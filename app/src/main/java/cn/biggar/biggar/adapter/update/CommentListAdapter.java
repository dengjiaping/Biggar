package cn.biggar.biggar.adapter.update;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.biggar.biggar.R;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.bean.update.ContentBean;
import cn.biggar.biggar.utils.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chenwy on 2017/9/4.
 */

public class CommentListAdapter extends BaseQuickAdapter<ContentBean.CommentList, BaseViewHolder> {
    public CommentListAdapter(List<ContentBean.CommentList> data) {
        super(R.layout.item_comments, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, ContentBean.CommentList bean) {
        Glide.with(mContext).load(Utils.getRealUrl(bean.E_HeadImg)).apply(new RequestOptions().centerCrop()).into((CircleImageView) viewHolder.getView(R.id.avatar_iv));

        viewHolder.setText(R.id.name_tv, bean.E_CreateUser);
        viewHolder.setText(R.id.content_tv, bean.E_Content);
        viewHolder.setText(R.id.likenumber_tv, Utils.getBreviaryValue(bean.E_Clicks));
        viewHolder.setImageResource(R.id.like_iv, bean.E_Fabulous != null && bean.E_Fabulous.equals("1") ? R.mipmap.pinkhand : R.mipmap.hand);
        viewHolder.setTextColor(R.id.likenumber_tv, ContextCompat.getColor(mContext, bean.E_Fabulous != null && bean.E_Fabulous.equals("1") ? R.color.colorPrimary : R.color.app_text_color_q));
        viewHolder.addOnClickListener(R.id.avatar_iv).addOnClickListener(R.id.like_ll);
        if (!TextUtils.isEmpty(bean.E_CreateDate)) {
            viewHolder.setText(R.id.date_tv, bean.E_CreateDate);
        } else {
            viewHolder.setText(R.id.date_tv, "刚刚");
        }
    }
}
